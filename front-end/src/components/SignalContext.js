import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { getSignals } from '../api/SignalApi';
import { useAuth } from './AuthContext';

const SignalContext = createContext();

export const SignalProvider = ({ children }) => {
  const { token, userRole, userId, logout } = useAuth();
  const [signals, setSignals] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchAllSignals = useCallback(async () => {
    if (!token) return;

    setLoading(true);
    let allSignals = [];
    let page = 1;
    let hasMore = true;

    try {
      while (hasMore) {
        let params = { page, isHeartbeat: false };
        if (!userRole.includes('ADMIN')) {
          params.ownerId = userId;
        }
        const response = await getSignals(params, token);
        if (response.status === 200) {
          const data = response.data;
          if (data.length === 0) {
            hasMore = false;
          } else {
            allSignals = [...allSignals, ...data];
            page++;
          }
        } else {
          hasMore = false;
        }
      }
      setSignals(allSignals);
    } catch (error) {
      console.error('Error fetching signals:', error);
      if (error.response && error.response.status === 401) {
        logout();
      }
    }
    setLoading(false);
  }, [token, userRole, userId, logout]);

  useEffect(() => {
    fetchAllSignals();
  }, [fetchAllSignals]);

  const addSignal = (newSignal) => {
    setSignals((prevSignals) => [newSignal, ...prevSignals]);
  };

  const updateSignals = (newSignals) => {
    setSignals(newSignals);
  };

  return (
    <SignalContext.Provider value={{ signals, addSignal, updateSignals, setSignals, fetchAllSignals, loading }}>
      {children}
    </SignalContext.Provider>
  );
};

export const useSignal = () => useContext(SignalContext);

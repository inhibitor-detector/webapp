import React, { useState, useEffect } from 'react';
import AlertContainer from './AlertContainer';
import { useAuth } from './AuthContext';
import { getSignals } from '../api/SignalApi';
import { getDetectorById } from '../api/DetectorApi';
import { useSignal } from './SignalContext';

const InhibitionDetected = () => {
  const [open, setOpen] = useState(false);
  const { userRole, userId, token, logout } = useAuth();
  const [lastId, setLastId] = useState(
    () => Number(localStorage.getItem('lastId')) || -1
  );
  const [signal, setSignal] = useState([]);
  const [detector, setDetector] = useState([]);
  const { addSignal, updateSignals } = useSignal();
  const [isInitialLoad, setIsInitialLoad] = useState(true);

  const getNewSignals = async () => {
    try {
      let params = { isHeartbeat: false };
      if (userRole && !userRole.includes('ADMIN')) {
        params.ownerId = userId;
      }
      const response = await getSignals(params, token);
      return response.data;
    } catch (error) {
      if (error.response && error.response.status === 401) {
        logout();
      }
      console.error('Error fetching signals:', error);
      return null;
    }
  };

  const fetchSignals = async () => {
    const data = await getNewSignals();
    if (data && data.length > 0 && !localStorage.getItem('lastId')) {
      const lastId = Math.max(...data.map(signal => signal.id));
      updateSignals(data);
      setLastId(lastId);
      localStorage.setItem('lastId', lastId);
      setIsInitialLoad(false);
    }
  };

  const checkForNewSignals = async () => {
    const data = await getNewSignals();
    if (data && data.length > 0) {
      const actualId = Math.max(...data.map(signal => signal.id));
      if (actualId !== lastId) {
        setOpen(true);
        setLastId(actualId);
        setSignal(data[0]);
        addSignal(data[0]);
        localStorage.setItem('lastId', actualId);

        try {
          const detectorResponse = await getDetectorById(data[0].detectorId, token);
          if (detectorResponse.status === 200) {
            setDetector(detectorResponse.data);
          }
        } catch (error) {
          if (error.response && error.response.status === 401) {
            logout();
          }
          console.error('Error fetching detector data:', error);
        }
      }
    }
  };

  useEffect(() => {
    if (!token) {
      setLastId(-1);
      localStorage.removeItem('lastId');
      setIsInitialLoad(true);
      return;
    }

    const fetchAndCheckSignals = async () => {
      if(isInitialLoad) {
        await fetchSignals();
        setIsInitialLoad(false);
      }
    };

    fetchAndCheckSignals();

    const interval = setInterval(() => {
      checkForNewSignals();
    }, 10000);

    return () => clearInterval(interval);
    // eslint-disable-next-line
  }, [token, userRole, userId, lastId, logout]);

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div>
      <AlertContainer open={open} onClose={handleClose} detector={detector} signal={signal} token={token}/>
    </div>
  );
};

export default InhibitionDetected;

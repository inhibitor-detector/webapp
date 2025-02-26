import React, { createContext, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [userRole, setUserRole] = useState(localStorage.getItem('role'));
  const [userId, setUserId] = useState(localStorage.getItem('userId'));
  const navigate = useNavigate();

  const saveToken = (newToken) => {
    setToken(newToken);
  };

  const saveUserRole = (role) => {
    localStorage.setItem('role', role);
    setUserRole(role);
  };

  const saveUserId = (id) => {
    localStorage.setItem('userId', id);
    setUserId(id);
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    localStorage.removeItem('lastId');
    setToken(null);
    setUserRole(null);
    setUserId(null);
    navigate("/");
  };

  return (
    <AuthContext.Provider value={{ token, userRole, userId, saveToken, saveUserRole, saveUserId, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

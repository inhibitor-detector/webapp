import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [userRole, setUserRole] = useState(localStorage.getItem('role'));
  const [userId, setUserId] = useState(localStorage.getItem('userId'));

  const saveToken = (newToken) => {
    setToken(newToken);
  };

  const saveUserRole = (role) => {
    setUserRole(role);
  };

  const saveUserId = (id) => {
    setUserId(id);
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, userRole, userId, saveToken, saveUserRole, saveUserId, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

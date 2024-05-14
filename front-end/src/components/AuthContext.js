import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [userRole, setUserRole] = useState('ADMINS');

  const saveToken = (newToken) => {
    setToken(newToken);
  };

  const saveUserRole = (role) => {
    setUserRole(role);
  };

  const logout = () => {
    localStorage.removeItem('token');
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, userRole, saveToken, saveUserRole, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

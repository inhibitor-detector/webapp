import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [userRole, setUserRole] = useState('ADMINS');
  const [userId, setUserId] = useState(null);
  const [exp, setExp] = useState(null);

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
    setToken(null);
    setCookie('username', '', 1);
    setCookie('password', '', 1);
  };

  const setCookie = (name, value, days) => {
    const date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    const expires = "expires=" + date.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/";
  };

  return (
    <AuthContext.Provider value={{ token, userRole, userId, exp, saveToken, saveUserRole, saveUserId, logout, setExp }}>
      {children}
    </AuthContext.Provider>
  );
};

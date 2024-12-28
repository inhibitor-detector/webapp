import React, { useEffect } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider, useAuth } from './components/auth/AuthContext';

const InitializeApp = () => {
  const { saveToken } = useAuth();

  useEffect(() => {
    const tokenFromStorage = localStorage.getItem('token');
    if (tokenFromStorage) {
      saveToken(tokenFromStorage);
    }
  }, [saveToken]);

  return null;
};

createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <InitializeApp />
        <App />
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
);

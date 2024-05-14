import React, { useEffect } from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider, useAuth } from './components/AuthContext'; 

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

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <InitializeApp />
        <App />
      </AuthProvider>
    </BrowserRouter> 
  </React.StrictMode>,
  document.getElementById('root')
);

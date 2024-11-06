import React, { useState } from 'react';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import './Signin.css';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './AuthContext';
import Paper from '@mui/material/Paper';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import IconButton from '@mui/material/IconButton';
import InputAdornment from '@mui/material/InputAdornment';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import ErrorIcon from '@mui/icons-material/Error';

const defaultTheme = createTheme();

export default function SignIn() {
  const navigate = useNavigate();
  const { saveToken, saveUserId, saveUserRole, setExp } = useAuth();
  const [showPassword, setShowPassword] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const saveRoles = async (token, userId) => {
    const response = await axios.get(`http://localhost:80/users/${userId}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    saveUserRole(response.data.roles);
    localStorage.setItem('role', response.data.roles);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    try {
      const response = await axios.get('http://localhost:80/', { auth: { username: data.get('username'), password: data.get('password') } });
      if (response.status === 200) {
        const token = response.headers.authorization.split(' ')[1];
        const decodedToken = jwtDecode(token);
        saveUserId(decodedToken.userId);
        saveRoles(token, decodedToken.userId);
        saveToken(token);
        setExp(decodedToken.exp);
        setCookie('username', data.get('username'), 1);
        setCookie('password', data.get('password'), 1);
        setCookie('userId', decodedToken.userId, 1);
        localStorage.setItem('token', token);
        navigate("/Detectores");
      } else {
        console.log("Error signing in");
        setErrorMessage("Usuario o contraseña incorrectos.");
      }
    } catch (error) {
      console.error('Error:', error);
      setErrorMessage("Usuario o contraseña incorrectos.");
    }
  };

  const setCookie = (name, value, days) => {
    const date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    const expires = "expires=" + date.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/";
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center'
          }}
        >
          <Paper elevation={3} sx={{ padding: 4, border: '1px solid #8bc34a' }}>
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '200px' }}>
              <img src="/logo.png" alt="" style={{ width: '100px', height: '100px', alignSelf: 'center' }} />
            </div>
            <Typography component="h1" variant="h5" sx={{ textAlign: 'center' }}>
              Detector Inhibidores
            </Typography>
            <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
              <TextField
                margin="normal"
                required
                fullWidth
                id="username"
                label="Usuario"
                name="username"
                autoComplete="username"
                autoFocus
                className='textField'
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="password"
                label="Contraseña"
                type={showPassword ? 'text' : 'password'}
                id="password"
                autoComplete="current-password"
                className='textField'
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() => setShowPassword(!showPassword)}
                      >
                        {showPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  )
                }}
              />
              {errorMessage && (
                <Typography variant="body2" color="red" align="center" sx={{ mt: 1, display: 'flex', alignItems: 'center' }}>
                  <ErrorIcon sx={{ fontSize: 'inherit', marginRight: '0.5em' }} /> {errorMessage}
                </Typography>
              )}

              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{
                  backgroundColor: '#8bc34a',
                  '&:hover': {
                    backgroundColor: '#689f38',
                  },
                  mt: 2
                }}
              >
                Iniciar Sesión
              </Button>
            </Box>
          </Paper>
        </Box>
      </Container>
    </ThemeProvider>
  );
}

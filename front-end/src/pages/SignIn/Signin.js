import ErrorIcon from '@mui/icons-material/Error';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import CssBaseline from '@mui/material/CssBaseline';
import IconButton from '@mui/material/IconButton';
import InputAdornment from '@mui/material/InputAdornment';
import Paper from '@mui/material/Paper';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import { jwtDecode } from 'jwt-decode';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authenticateUser, getUserById } from '../../api/UserApi';
import { useAuth } from '../../components/AuthContext';
import './Signin.css';

const defaultTheme = createTheme();

export default function SignIn() {
  const navigate = useNavigate();
  const { saveToken, saveUserId, saveUserRole } = useAuth();
  const [showPassword, setShowPassword] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const saveRoles = async (token, userId) => {
    const userData = await getUserById(userId, token);
    saveUserRole(userData.roles);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    try {
      const response = await authenticateUser(data.get('username'), data.get('password'));
      if (response.status === 200) {
        const token = response.headers.authorization.split(' ')[1];
        const decodedToken = jwtDecode(token);
        saveUserId(decodedToken.userId);
        await saveRoles(token, decodedToken.userId);
        saveToken(token);
        localStorage.setItem('token', token);
        navigate("/Detectores");
      }
    } catch (error) {
      setErrorMessage(error.message);
    }
  };

  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      handleSubmit(event);
    }
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box className='box-container'>
          <Paper elevation={3} sx={{ padding: 4, border: '1px solid #8bc34a' }}>
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '200px' }}>
              <img src="/logo.png" alt="" style={{ width: '100px', height: '100px', alignSelf: 'center' }} />
            </div>
            <Typography component="h1" variant="h5" sx={{ textAlign: 'center' }}>
              Detector Inhibidores
            </Typography>
            <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }} onKeyDown={handleKeyPress}>
              <TextField
                margin="normal"
                required
                fullWidth
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
                autoComplete="current-password"
                className='textField'
                slotProps={{
                  input: {
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          onClick={() => setShowPassword(!showPassword)}
                        >
                          {showPassword ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                      </InputAdornment>
                    )
                  }
                }}
              />
              {errorMessage && (
                <Typography variant="body2" className="error-message">
                  <ErrorIcon sx={{ fontSize: 'inherit', marginRight: '0.5em' }} /> {errorMessage}
                </Typography>
              )}
              <Button type="submit" fullWidth variant="contained" className='sign-in-button'>
                Iniciar Sesión
              </Button>
            </Box>
          </Paper>
        </Box>
      </Container>
    </ThemeProvider>
  );
}

import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import './Signin.css';
import { useNavigate } from 'react-router-dom';
import Paper from '@mui/material/Paper';

const defaultTheme = createTheme();

export default function SignIn() {
  const navigate = useNavigate();
  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    console.log({
      email: data.get('email'),
      password: data.get('password'),
    });
    navigate("/Detectores")
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
              <img src="/logo.png" style={{ width: '100px', height: '100px', alignSelf: 'center' }} />
            </div>
              <Typography component="h1" variant="h5" sx={{ textAlign: 'center' }}>
                Sign in
              </Typography>
              <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                <TextField
                  margin="normal"
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  autoFocus
                  className='textField'
                />
                <TextField
                  margin="normal"
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                  className='textField'
                />
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx= {{ backgroundColor: '#8bc34a', 
                  '&:hover': {
                    backgroundColor: '#689f38', // Cambia el color al hacer hover
                  }}}
                >
                  Iniciar Cesion
                </Button>
              </Box>
          </Paper>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
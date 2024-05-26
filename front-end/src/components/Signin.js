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

const defaultTheme = createTheme();

export default function SignIn() {
  const navigate = useNavigate();
  const { saveToken } = useAuth();
  const { saveUserId } = useAuth();
  const { saveUserRole } = useAuth();

  const saveRoles = async (token, userId) => {
    const response = await axios.get(`http://localhost:8000/users/${userId}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    saveUserRole(response.data.roles);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    try {
      const response = await axios.get('http://localhost:8000/', { auth: {username: data.get('username'), password: data.get('password') }});
      if (response.status === 200) {
        const token = response.headers.authorization.split(' ')[1];
        const decodedToken = jwtDecode(token);
        console.log(decodedToken)
        saveUserId(decodedToken.userId)
        saveRoles(token, decodedToken.userId);
        saveToken(token);
        localStorage.setItem('token', token);
        console.log("Signed in")
        navigate("/Detectores")
      } else {
        console.log("Error signing in");
      }
    } catch (error) {
      console.error('Error:', error);
    }
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
                    backgroundColor: '#689f38',
                  }}}
                >
                  Iniciar Sesion
                </Button>
              </Box>
          </Paper>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
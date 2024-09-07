import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

export async function refreshToken(exp, setExp, saveToken) {
  try {
    const now = Date.now() / 1000;
    const expiresIn = exp - now;
    if (expiresIn < 60) {
      const username = getCookie('username');
      const pass = getCookie('password');
      if (username && pass) {
        const response = await axios.get('http://localhost:8000/', { auth: { username, password: pass } });
        if (response.status === 200) {
          const token = response.headers.authorization.split(' ')[1];
          saveToken(token);
          const decodedToken = jwtDecode(token);
          setExp(decodedToken.exp);
          localStorage.setItem('token', token);
        }
      }
    }

  } catch (error) {
    console.error('Error:', error);
  }
}

function getCookie(name) {
  const cookieName = name + "=";
  const decodedCookie = decodeURIComponent(document.cookie);
  const cookieArray = decodedCookie.split(';');
  for (let i = 0; i < cookieArray.length; i++) {
    let cookie = cookieArray[i].trim();
    if (cookie.startsWith(cookieName)) {
      return cookie.substring(cookieName.length, cookie.length);
    }
  }
  return "";
}
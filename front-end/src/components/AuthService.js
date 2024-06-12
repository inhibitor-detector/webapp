import axios from 'axios';

const INTERVAL_TIME = 20000;

export async function loginPeriodically(username, pass, saveToken) {
  try {
    const response = await axios.get('http://localhost:8000/', { auth: { username: username, password: pass } });
    if (response.status === 200) {
      const token = response.headers.authorization.split(' ')[1];
      saveToken(token);
      localStorage.setItem('token', token);
      console.log("Logged in");
    } else {
      console.log("Error logging in");
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

export function startLoginInterval(saveToken) {
  const username = getCookie('username');
  const pass = getCookie('password');

  if(username !== null && pass !== null) {
    return setInterval(() => {
      loginPeriodically(username, pass, saveToken);
    }, INTERVAL_TIME);
  }
}

export function stopLoginInterval(intervalId) {
  clearInterval(intervalId);
}

function getCookie(name) {
  const cookieName = name + "=";
  const decodedCookie = decodeURIComponent(document.cookie);
  const cookieArray = decodedCookie.split(';');
  for (let i = 0; i < cookieArray.length; i++) {
    let cookie = cookieArray[i];
    while (cookie.charAt(0) === ' ') {
      cookie = cookie.substring(1);
    }
    if (cookie.indexOf(cookieName) === 0) {
      return cookie.substring(cookieName.length, cookie.length);
    }
  }
  return "";
}
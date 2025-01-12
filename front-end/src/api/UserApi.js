import axios from 'axios';

const BASE_URL = 'http://localhost:8001';

export const getUserById = async (userId, token) => {
  try {
    const response = await axios.get(`${BASE_URL}/users/${userId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    console.error('Error getting user by ID:', error);
    throw new Error('No se pudo obtener la información del usuario');
  }
};

export const authenticateUser = async (username, password) => {
  try {
    const response = await axios.get(BASE_URL, {
      auth: { username, password },
    });
    return response;
  } catch (error) {
    console.error('Error authenticating user:', error);
    throw new Error('Usuario o contraseña incorrectos');
  }
};

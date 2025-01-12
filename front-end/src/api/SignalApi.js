import axios from 'axios';

const BASE_URL = 'http://localhost:8001/signals';

export const getSignals = async (params, token) => {
    const response = await axios.get(BASE_URL, {
        params,
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
    return response;
};

export const getSignalsByTime = async (params, token) => {
    const response = await axios.get(`${BASE_URL}/time`, {
        params,
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
    return response;
};

import axios from 'axios';

const BASE_URL = 'http://localhost:8001/detectors';

export const getDetectorById = async (detectorId, token) => {
    const response = await axios.get(`${BASE_URL}/${detectorId}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    return response;
};

export const getDetectors = async (params, token) => {
    const response = await axios.get(BASE_URL, {
        params: params,
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });
    return response;
};

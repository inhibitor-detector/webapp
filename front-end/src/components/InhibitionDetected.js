import React, { useState, useEffect } from 'react';
import AlertContainer from './AlertContainer';
import { useAuth } from './AuthContext';
import axios from 'axios';

const InhibitionDetected = () => {
  const [open, setOpen] = useState(false);
  const { userRole, userId, token } = useAuth();
  const [lastId, setLastId] = useState(
    () => Number(localStorage.getItem('lastId')) || -1
  );
  const [detector, setDetector] = useState([]);
  const [isInitialLoad, setIsInitialLoad] = useState(true);

  const axiosGetSignals = async () => {
    try {
      let params = { isHeartbeat: false };
      if (!userRole.includes('ADMIN')) {
        params.ownerId = userId;
      }
      const response = await axios.get('http://localhost:80/signals', {
        params: params,
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching signals:', error);
      return null;
    }
  };

  const fetchSignals = async () => {
    const data = await axiosGetSignals();
    if (data && data.length > 0) {
      const lastId = data[0].id;
      setLastId(lastId);
      localStorage.setItem('lastId', lastId);
    }
  };

  const checkForNewSignals = async () => {
    const data = await axiosGetSignals();
    if (data && data.length > 0) {
      const actualId = data[0].id;
      if (actualId !== lastId) {
        if (!isInitialLoad) {
          setOpen(true);
        }
        setLastId(actualId);
        localStorage.setItem('lastId', actualId);

        try {
          const detectorResponse = await axios.get(`http://localhost:80/detectors/${data[0].detectorId}`, {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          });
          if (detectorResponse.status === 200) {
            setDetector(detectorResponse.data);
          }
        } catch (error) {
          console.error('Error fetching detector data:', error);
        }
      }
    }
  };

  useEffect(() => {
    if (!token) {
      setLastId(-1);
      localStorage.removeItem('lastId');
      setIsInitialLoad(true);
      return;
    }

    const fetchAndCheckSignals = async () => {
      await fetchSignals();
      await checkForNewSignals();
      setIsInitialLoad(false);
    };

    fetchAndCheckSignals();

    const interval = setInterval(() => {
      checkForNewSignals();
    }, 15000);

    return () => clearInterval(interval);
  }, [token, userRole, userId, lastId]);

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div>
      <AlertContainer open={open} onClose={handleClose} detector={detector} />
    </div>
  );
};

export default InhibitionDetected;

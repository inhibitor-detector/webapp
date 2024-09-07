import React, { useState, useEffect } from 'react';
import AlertContainer from './AlertContainer';
import { useAuth } from './AuthContext';
import axios from 'axios';

const InhibitionDetected = () => {
  const [open, setOpen] = useState(false);
  const { userRole, userId } = useAuth();
  const { token } = useAuth();
  const [ lastId, setLastId ] = useState(-1);
  const [detectorId, setDetectorId] = useState([]);

  useEffect(() => {
    const fetchSignals = async () => {
      if (token) {
        try {
          let params = { isHeartbeat: false };
          if (!userRole.includes('ADMIN')) {
            params.ownerId = userId;
          }
          const response = await axios.get('http://localhost:8000/signals', {
            params: params,
            headers: {
              'Authorization': `Bearer ${token}`
            }
          });
          if (response.status === 200) {
            const data = response.data;
            if(data.length > 0) {
              const actualId = data[0].id;
              if(lastId === -1) {
                setLastId(actualId);
              } else {
                if(actualId !== lastId) {
                  setOpen(true);
                  setLastId(actualId);
                  setDetectorId(data[0].detectorId);
                }
              }
            }
          }
        } catch (error) {
          console.error('Error:', error);
        }
      }

    };

    const interval = setInterval(fetchSignals, 15000);

    fetchSignals();

    return () => clearInterval(interval);
  }, [token, userRole, userId, lastId, detectorId]);

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div>
      <AlertContainer open={open} onClose={handleClose} detectorId={detectorId} />
    </div>
  );
};

export default InhibitionDetected;

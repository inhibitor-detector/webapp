import React, { useState, useEffect } from 'react';
import { TableContainer, Table, TableHead, TableBody, TableRow, TableCell, Paper, Button } from '@mui/material';
import { CheckCircle, Cancel } from '@mui/icons-material';
import { useLocation } from 'react-router-dom';
import { useAuth } from './AuthContext';
import axios from 'axios';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';

const HeartbeatTable = () => {
    const { token } = useAuth();
    const [heartbeats, setHeartbeats] = useState([]);
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const selectedDetector = searchParams.get('selectedDetector');

    useEffect(() => {
        const fetchData = async () => {
          try {
            console.log(selectedDetector)
            const response = await axios.get(`http://localhost:8000/signals?detectorId=${selectedDetector}`, {
                headers: {
                  Authorization: `Bearer ${token}`
                }
              });
            if (response.status === 200) {
              setHeartbeats(response.data);
            }
          } catch (error) {
            console.error('Error:', error);
          }
        };
    
        fetchData();
      }, [selectedDetector, token]);

      const handleGoBack = () => {
        window.history.back();
    };
    

  return (
    <div>
      <Button variant="contained" onClick={handleGoBack} sx={{
        backgroundColor: '#8bc34a',
        color: 'white',
        '&:hover': {
            backgroundColor: '#8bc34a',
        },
        '& .MuiButton-startIcon': {
            marginRight: '4px',
        },
    }}
    startIcon={<ArrowBackIcon />}>Volver</Button>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell sx={{ textAlign: 'center', verticalAlign: 'middle' }}>Heartbeat</TableCell>
              <TableCell sx={{ textAlign: 'center', verticalAlign: 'middle' }}>Tiempo</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {heartbeats.map((data, index) => (
              <TableRow key={index}>
                <TableCell  sx={{ textAlign: 'center', verticalAlign: 'middle' }}>
                  {data.isHeartbeat ? (
                    <CheckCircle sx={{ color: 'green' }} />
                  ) : (
                    <Cancel sx={{ color: 'red' }} />
                  )}
                </TableCell>
                <TableCell sx={{ textAlign: 'center', verticalAlign: 'middle' }}>{data.timestamp}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};

export default HeartbeatTable;

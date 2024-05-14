import React, { useState, useEffect } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import ResponsiveAppBar from './Nav';
import axios from 'axios';
import { useAuth } from './AuthContext';

const SignalTable = () => {
  const { token } = useAuth();
  const [signals, setSignals] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get('http://localhost:8000/signals', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.status === 200) {
          setSignals(response.data);
        }
      } catch (error) {
        console.error('Error:', error);
      }
    };

    fetchData();
  }, [token]);

  return (
    <div>
      <ResponsiveAppBar/>
      <div style={{ paddingTop: '20px', maxWidth: '95%', margin: '0 auto' }}>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sx={{ color: '#8bc34a',fontSize: '1.1rem', textAlign: 'center' }}>Detector</TableCell>
                <TableCell sx={{ color: '#8bc34a',fontSize: '1.1rem', textAlign: 'center' }}>Horario</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {signals.map(signal => (
                <TableRow key={signal.id} sx={{ '&:hover': { backgroundColor: '#f5f5f5' } }}>
                  <TableCell sx={{textAlign: 'center' }}>{signal.detectorId}</TableCell>
                  <TableCell sx={{textAlign: 'center' }}>{signal.timestamp}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </div>
    </div>
  );
};

export default SignalTable;

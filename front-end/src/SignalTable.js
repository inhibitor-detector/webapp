import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';

axios.defaults.baseURL = 'http://localhost:8000';

const SignalTable = () => {
  const [signals, setSignals] = useState([]);

  const fetchSignals = async () => {
    try {
      const response = await axios.get('/signal_ids');
      const signalIds = response.data.signals_ids;
      console.log(signalIds)
      const signalDataPromises = signalIds.map(id => axios.get(`/signals/${id}`));
      const signalDataResponses = await Promise.all(signalDataPromises);
      const signalsData = signalDataResponses.map(response => response.data.Signal);
      setSignals(signalsData);
    } catch (error) {
      console.error('Error fetching signals:', error);
      setSignals([]);
    }
  };

  useEffect(() => {
    fetchSignals();
  }, []);

  return (
    <div>
      <h2>Señales detectadas:</h2>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell sx={{ color: '#8bc34a',fontSize: '1.1rem', textAlign: 'center' }}>INHIBIDOR</TableCell>
              <TableCell sx={{ color: '#8bc34a',fontSize: '1.1rem', textAlign: 'center' }}>INHIBICION</TableCell>
              <TableCell sx={{ color: '#8bc34a',fontSize: '1.1rem', textAlign: 'center' }}>COMIENZA</TableCell>
              <TableCell sx={{ color: '#8bc34a',fontSize: '1.1rem', textAlign: 'center' }}>TERMINA</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {signals.map(signal => (
              <TableRow key={signal.id}>
                <TableCell sx={{textAlign: 'center' }}>{signal.id_inhibitor}</TableCell>
                <TableCell sx={{textAlign: 'center' }}>{signal.inhibition ? '✓' : '❌'}</TableCell>
                <TableCell sx={{textAlign: 'center' }}>{signal.start}</TableCell>
                <TableCell sx={{textAlign: 'center' }}>{signal.end}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};

export default SignalTable;

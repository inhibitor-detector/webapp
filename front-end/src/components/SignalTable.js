import React, { useState, useEffect, useCallback } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, CircularProgress, Box } from '@mui/material';
import ResponsiveAppBar from './Nav';
import axios from 'axios';
import { useAuth } from './AuthContext';
import { refreshToken } from './AuthService';

const SignalTable = () => {
  const { token, userRole, userId, exp, setExp, saveToken } = useAuth();
  const [signals, setSignals] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchAllSignals = useCallback(async () => {
    setLoading(true);
    let allSignals = [];
    let page = 1;
    let hasMore = true;

    try {
      refreshToken(exp, setExp, saveToken);
      while (hasMore) {
        let params = { page, isHeartbeat: false };
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
          if (data.length === 0) {
            hasMore = false;
          } else {
            allSignals = [...allSignals, ...data];
            page++;
          }
        } else {
          hasMore = false;
        }
      }
      setSignals(allSignals);
    } catch (error) {
      console.error('Error:', error);
    }
    setLoading(false);
  }, [token, userRole, userId, exp, setExp, saveToken]);

  useEffect(() => {
    fetchAllSignals();
  }, [fetchAllSignals]);

  return (
    <div>
      <ResponsiveAppBar/>
      <div style={{ paddingTop: '20px', maxWidth: '95%', margin: '0 auto' }}>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Detector</TableCell>
                <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Horario</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {signals.map(signal => (
                <TableRow key={signal.id} sx={{ '&:hover': { backgroundColor: '#f5f5f5' } }}>
                  <TableCell sx={{ textAlign: 'center' }}>{signal.detectorId}</TableCell>
                  <TableCell sx={{ textAlign: 'center' }}>{signal.timestamp}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        {(loading && 
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexGrow: 1 }}>
          <CircularProgress sx={{ color: '#8bc34a' }} />
        </Box>)}
      </div>
    </div>
  );
};

export default SignalTable;

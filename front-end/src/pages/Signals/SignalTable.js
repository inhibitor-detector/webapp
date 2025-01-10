import React, { useState, useEffect, useCallback } from 'react';
import { format } from 'date-fns';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Paper, CircularProgress, Box } from '@mui/material';
import ResponsiveAppBar from '../../layouts/Nav';
import axios from 'axios';
import { useAuth } from '../../components/AuthContext';
import DashboardCard from '../../layouts/DashboardCard';
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive';
import Popup from '../../components/Popup';
import { CheckCircleOutline, HighlightOff } from '@mui/icons-material';

const SignalTable = () => {
  const { token, userRole, userId } = useAuth();
  const [signals, setSignals] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedDetector, setSelectedDetector] = useState(null);
  const [open, setOpen] = useState(null);

  const fetchAllSignals = useCallback(async () => {
    setLoading(true);
    let allSignals = [];
    let page = 1;
    let hasMore = true;

    try {
      while (hasMore) {
        let params = { page, isHeartbeat: false };
        if (!userRole.includes('ADMIN')) {
          params.ownerId = userId;
        }
        const response = await axios.get('http://localhost:8001/signals', {
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
  }, [token, userRole, userId]);

  useEffect(() => {
    fetchAllSignals();
  }, [fetchAllSignals]);

  const fetchDetectorDetails = async (detectorId) => {
    try {
      const response = await axios.get(`http://localhost:8001/detectors/${detectorId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (response.status === 200) {
        setSelectedDetector(response.data);
      } else {
        console.error('Error al obtener los detalles del detector');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const handleClick = (event, detectorId) => {
    setOpen(event.currentTarget);
    fetchDetectorDetails(detectorId);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div>
      <ResponsiveAppBar />
      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexGrow: 1 }}>
          <CircularProgress sx={{ color: '#8bc34a' }} />
        </Box>
      ) : (
        <div style={{ paddingTop: '20px', maxWidth: '95%', margin: '0 auto' }}>
          <div style={{ textAlign: 'center', marginTop: '20px' }}>
            <Typography
              variant="h4"
              style={{
                fontWeight: 'bold',
              }}
            >
              Alertas
            </Typography>
          </div>
          <DashboardCard stats={[{
            label: "Total Alertas",
            value: signals.length,
            icon: <NotificationsActiveIcon />,
            backgroundColor: "#EF5350",
          }]} />
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Detector</TableCell>
                  <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Horario</TableCell>
                  <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Estado</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {signals.map(signal => (
                  <TableRow key={signal.id} sx={{ '&:hover': { backgroundColor: '#f5f5f5' } }}>
                    <TableCell
                      sx={{ textAlign: 'center', cursor: 'pointer' }}
                      onClick={(event) => handleClick(event, signal.detectorId)}
                    >
                      {signal.detectorId}
                    </TableCell>
                    <TableCell sx={{ textAlign: 'center' }}>
                      {format(new Date(signal.timestamp), 'dd/MM/yyyy HH:mm:ss')}
                    </TableCell>
                    <TableCell sx={{ textAlign: 'center' }}>{signal.status ? (
                      <CheckCircleOutline sx={{ color: 'green', fontSize: 18 }} />
                    ) : (
                      <HighlightOff sx={{ color: 'red', fontSize: 18 }} />
                    )}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </div>
      )}
      <Popup
        popup={open}
        selectedDetector={selectedDetector}
        onClose={handleClose}
      />
    </div>
  );
};

export default SignalTable;

import React, { useState, useEffect } from 'react';
import { format } from 'date-fns';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Paper, Box } from '@mui/material';
import ResponsiveAppBar from '../../layouts/Nav';
import { useAuth } from '../../components/AuthContext';
import DashboardCard from '../../layouts/DashboardCard';
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive';
import Popup from '../../components/Popup';
import { CheckCircleOutline, HighlightOff } from '@mui/icons-material';
import { updateSignal } from '../../api/SignalApi';
import { getDetectorById } from '../../api/DetectorApi';
import Button from '@mui/material/Button';
import { useSignal } from '../../components/SignalContext';
import './SignalTable.css'
import Title from '../../components/Title';

const SignalTable = () => {
  const { token } = useAuth();
  const { signals, setSignals } = useSignal();
  const [selectedDetector, setSelectedDetector] = useState(null);
  const [open, setOpen] = useState(null);

  useEffect(() => {
  }, [signals]);

  const fetchDetectorDetails = async (detectorId) => {
    try {
      const response = await getDetectorById(detectorId, token);
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

  const handleVerify = async (signalId) => {
    try {
      const updatedSignal = signals.find(signal => signal.id === signalId);
      if (updatedSignal) {
        updatedSignal.acknowledged = true;
        await updateSignal(signalId, updatedSignal, token);
        setSignals(prevSignals =>
          prevSignals.map(signal =>
            signal.id === signalId ? { ...signal, acknowledged: true } : signal
          )
        );
      }
    } catch (error) {
      console.error('Error al verificar la señal:', error);
    }
  };

  const filteredSignals = signals.filter(signal => !signal.isHeartbeat);

  return (
    <div>
      <ResponsiveAppBar />
      <div style={{ paddingTop: '20px', maxWidth: '95%', margin: '0 auto' }}>
        <Title title={'Alertas'} />
        <DashboardCard stats={[{
          label: "Total Alertas",
          value: filteredSignals.length,
          icon: <NotificationsActiveIcon />,
          backgroundColor: "#EF5350",
        }]} />
        {filteredSignals.length === 0 ? (
          <Typography
            variant="h6"
            sx={{
              textAlign: 'center',
              padding: '20px',
            }}
          >
            No se detectaron alertas
          </Typography>
        ) : (
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell className='table-title'>Detector</TableCell>
                  <TableCell className='table-title'>Horario</TableCell>
                  <TableCell className='table-title'>Estado</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {filteredSignals.map(signal => (
                  <TableRow key={signal.id}>
                    <TableCell
                      sx={{ textAlign: 'center', cursor: 'pointer' }}
                      onClick={(event) => handleClick(event, signal.detectorId)}
                    >
                      {signal.detectorId}
                    </TableCell>
                    <TableCell sx={{ textAlign: 'center' }}>
                      {format(new Date(signal.timestamp), 'dd/MM/yyyy HH:mm:ss')}
                    </TableCell>
                    <TableCell sx={{ textAlign: 'center' }}>
                      {signal.acknowledged ? (
                        <Box display="inline-flex" alignItems="center">
                          <CheckCircleOutline sx={{ color: 'green', fontSize: 16 }} />
                          <span style={{ marginLeft: '8px', color: 'green' }}>Verificado</span>
                        </Box>
                      ) : (
                        <Box display="inline-flex" alignItems="center">
                          <HighlightOff sx={{ color: 'red', fontSize: 16 }} />
                          <Button
                            className='verify-button-table'
                            size="small"
                            onClick={() => handleVerify(signal.id)}
                          >
                            Verificar
                          </Button>
                        </Box>
                      )}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </div>
      <Popup
        popup={open}
        selectedDetector={selectedDetector}
        onClose={handleClose}
      />
    </div>
  );
};

export default SignalTable;

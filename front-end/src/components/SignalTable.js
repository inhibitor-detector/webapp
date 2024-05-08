import React, { useState, useEffect } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import signalsData from './mock_data/signals.json';
import { IconButton, Popover } from '@mui/material';
import ResponsiveAppBar from './Nav';

const SignalTable = () => {
  const [signals, setSignals] = useState([]);
  const [popup, setPopup] = useState(null);

  const handlePopoverOpen = (event, detectorId) => {
    setPopup(event.currentTarget);
  };

  const handlePopoverClose = () => {
    setPopup(null);
  };

  const open = Boolean(popup);

  useEffect(() => {
    setSignals(signalsData.signals);
  }, []);

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
                  <TableCell sx={{textAlign: 'center' }}>
                    <IconButton onClick={(event) => handlePopoverOpen(event, signal.detector_id)}>
                      {signal.detector_id}
                    </IconButton>
                  </TableCell>
                  <TableCell sx={{textAlign: 'center' }}>{signal.timestamp}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <Popover
          open={open}
          popup={popup}
          onClose={handlePopoverClose}
          anchorOrigin={{
            vertical: 'bottom',
            horizontal: 'center',
          }}
          transformOrigin={{
            vertical: 'top',
            horizontal: 'center',
          }}
        >
          <div style={{ padding: '10px' }}>Detector!</div>
        </Popover>
      </div>
    </div>
  );
};

export default SignalTable;

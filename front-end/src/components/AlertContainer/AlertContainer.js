import React, { useState } from 'react';
import { Button, Modal, Alert, Stack, Typography, Box } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import CheckIcon from '@mui/icons-material/Check';
import Popup from '../Popup';
import { updateSignal } from '../../api/SignalApi';
import { useSignal } from '../SignalContext';
import './AlertContainer.css'

const Notification = ({ open, onClose, detector, signal, token }) => {
  const [popup, setPopup] = useState(null);
  const { setSignals } = useSignal();

  const handleClose = () => {
    setPopup(null);
  };

  const handleClick = (event) => {
    setPopup(event.currentTarget);
  };

  const handleVerify = async (signalId) => {
    try {
      const updatedSignal = { ...signal, acknowledged: true };
      await updateSignal(signalId, updatedSignal, token);
      setSignals(prevSignals =>
        prevSignals.map(signal =>
          signal.id === signalId ? { ...signal, acknowledged: true } : signal
        )
      );
      onClose();
    } catch (error) {
      console.error('Error al verificar la señal:', error);
    }
  };

  return (
    <div>
      {open && (
        <Stack sx={{ width: '100%' }} spacing={2}>
          <Alert severity="error">Inhibidor detectado.</Alert>
        </Stack>
      )}
      <Modal
        open={open}
        onClose={onClose}
        className='modal'
        disableAutoFocus
      >
        <div className='modal-container'>
          <Button
            variant="contained"
            onClick={onClose}
            className='close-button'
          >
            <CloseIcon className='close-icon' />
          </Button>
          <Typography className='title'>
            ¡ALERTA!
          </Typography>
          <Typography className='message'>
            Inhibidor detectado
          </Typography>
          <Box textAlign='center'>
            <Button onClick={handleClick} className='link-button'>
              Detector
            </Button>
            <Box>
              <Button
                variant="contained"
                color="success"
                onClick={() => handleVerify(signal.id)}
                className='verify-button'
              >
                <CheckIcon className='check-icon' />
                Verificado
              </Button>
            </Box>
          </Box>
        </div>
      </Modal>
      <Popup popup={popup} selectedDetector={detector} onClose={handleClose} />
    </div>
  );
};

export default Notification;

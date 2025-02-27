import CloseIcon from '@mui/icons-material/Close';
import { Alert, Box, Button, Modal, Stack, Typography } from '@mui/material';
import React, { useState } from 'react';
import Popup from '../Popup';
import './ErrorContainer.css';

const ErrorContainer = ({ open, onClose, detector, signal, token }) => {
  const [popup, setPopup] = useState(null);

  const handleClose = () => {
    setPopup(null);
  };

  const handleClick = (event) => {
    setPopup(event.currentTarget);
  };

  return (
    <div>
      {open && (
        <Stack sx={{ width: '100%' }} spacing={2}>
          <Alert severity="warning">Falla en detector.</Alert>
        </Stack>
      )}
      <Modal
        open={open}
        onClose={onClose}
        className='modal'
        disableAutoFocus
      >
        <div className='modal-container-error'>
          <Button
            variant="contained"
            onClick={onClose}
            className='close-button'
          >
            <CloseIcon className='close-icon' />
          </Button>
          <Typography className='title'>
            ¡Error!
          </Typography>
          <Typography className='message'>
            Se detecto una falla en: 
          </Typography>
          <Box textAlign='center'>
            <Button onClick={handleClick} className='link-button'>
              Detector
            </Button>
          </Box>
        </div>
      </Modal>
      <Popup popup={popup} selectedDetector={detector} onClose={handleClose} />
    </div>
  );
};

export default ErrorContainer;

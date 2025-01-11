import React, { useState } from 'react';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import Alert from '@mui/material/Alert';
import Stack from '@mui/material/Stack';
import CloseIcon from '@mui/icons-material/Close';
import { Typography } from '@mui/material';
import Popup from './Popup';
import Box from '@mui/material/Box';
import CheckIcon from '@mui/icons-material/Check';

const Notification = ({ open, onClose, detector }) => {
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
          <Alert severity="error">Inhibidor detectado.</Alert>
        </Stack>
      )}
      <Modal
        open={open}
        onClose={onClose}
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center'
        }}
        disableAutoFocus={true}
      >
        <div style={{ position: 'relative', backgroundColor: 'red', padding: '20px', borderRadius: '5px' }}>
          <Button
            variant="contained"
            onClick={onClose}
            style={{
              position: 'absolute',
              top: '8px',
              right: '8px',
              borderRadius: '50%',
              minWidth: 'auto',
              padding: 0,
              backgroundColor: 'grey',
            }}
          >
            <CloseIcon style={{ color: 'white' }} />
          </Button>
          <Typography
            style={{
              textAlign: 'center',
              padding: '20px',
              color: 'white',
              fontSize: 30
            }}
          >
            ¡ALERTA!
          </Typography>
          <Typography
            style={{
              textAlign: 'center',
              color: 'white',
              fontSize: 24
            }}
          >
            Inhibidor detectado
          </Typography>
          <Box textAlign='center'>
            <Button onClick={handleClick}
              style={{
                color: 'white',
                textDecoration: 'underline',
              }}
            >
              Detector
            </Button>
            <Box textAlign="center">
              <Button
                variant="contained"
                color="success"
                onClick={() => console.log("Chequeado")}
                style={{
                  margin: '5px',
                }}
              >
                <CheckIcon style={{ marginRight: '5px' }} />
                Chequeado
              </Button>

              <Button
                variant="contained"
                color="error"
                onClick={() => console.log("No Chequeado")}
                style={{
                  margin: '5px',
                  backgroundColor: 'grey',
                  color: 'white',
                }}
              >
                <CloseIcon style={{ marginRight: '5px' }} />
                No Chequeado
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

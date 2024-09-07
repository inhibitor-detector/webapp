import React from 'react';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import Alert from '@mui/material/Alert';
import Stack from '@mui/material/Stack';
import CloseIcon from '@mui/icons-material/Close';
import { Typography } from '@mui/material';


const Notification = ({ open, onClose, detectorId }) => {
  return (
    <div>
      {open && (
        <Stack sx={{ width: '100%' }} spacing={2}>
          <Alert severity="error">Alerta, inhibidor detectado.</Alert>
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
            Inhibidor detectado por detector: {detectorId}
          </Typography>
        </div>
      </Modal>
    </div>
  );
};

export default Notification;

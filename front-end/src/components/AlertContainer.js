import React from 'react';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import Alert from '@mui/material/Alert';
import Stack from '@mui/material/Stack';
import CloseIcon from '@mui/icons-material/Close';


const Notification = ({ open, onClose, selectedDetector }) => {
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
        >
          <div style={{  position: 'relative', backgroundColor: 'red', padding: '20px', borderRadius: '5px' }}>
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
            <h2 style={{ color: 'white', textAlign: 'center' }}>¡ALERTA!</h2>
            <h3 style={{ color: 'white', textAlign: 'center' }}>Inhibidor detectado</h3>
            <h3 style={{ color: 'white', textAlign: 'center' }}>Detector: {selectedDetector.id}</h3>
            <h3 style={{ color: 'white', textAlign: 'center' }}>Descripción: {selectedDetector.name}</h3>
          </div>
        </Modal>
    </div>
  );
};

export default Notification;

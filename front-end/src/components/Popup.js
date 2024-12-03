import React from 'react';
import { Popover } from '@mui/material';


const Popup = ({ popup, selectedDetector, onClose }) => {

    return (
        <Popover
            open={Boolean(popup)}
            anchorEl={popup}
            onClose={onClose}
            anchorOrigin={{
            vertical: 'bottom',
            horizontal: 'center',
            }}
            transformOrigin={{
            vertical: 'top',
            horizontal: 'center',
            }}
        >
        {selectedDetector && (
          <div style={{ padding: '20px' }}>
            <h3>Detalle</h3>
            <p>Id: {selectedDetector.id}</p>
            <p>Nombre: {selectedDetector.name}</p>
            <p>Estado: {selectedDetector.isOnline ? 'Activo' : 'Inactivo'}</p>
            <p>Ubicación: {selectedDetector.description}</p>
          </div>
        )}
      </Popover>
    );
  };
  
  export default Popup;
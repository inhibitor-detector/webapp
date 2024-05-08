import React, { useState, useEffect } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import detectorsData from './mock_data/detectors.json';
import Popup from './Popup';
import ResponsiveAppBar from './Nav';

const DetectorTable = () => {
  const [detectors, setDetectors] = useState([]);
  const [selectedDetector, setSelectedDetector] = useState(null);
  const [popup, setPopup] = useState(null);

  useEffect(() => {
    setDetectors(detectorsData.detectors);
  }, []);

  const handleClick = (event, detector) => {
    setSelectedDetector(detector);
    setPopup(event.currentTarget);
  };

  const handleClose = () => {
    setSelectedDetector(null);
    setPopup(null);
  };

  return (
    <div>
      <ResponsiveAppBar/>
      <div style={{ paddingTop: '20px', maxWidth: '95%', margin: '0 auto' }}>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Id</TableCell>
              <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Ubicación</TableCell>
              <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Activo</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {detectors.map((detector) => (
              <DetectorRow key={detector.id} detector={detector} onClick={handleClick} />
            ))}
          </TableBody>
        </Table>
      <Popup popup={popup} selectedDetector={selectedDetector} onClose={handleClose}/>
      </TableContainer>
      </div>
      
    </div>
  );
};

const DetectorRow = ({ detector, onClick }) => {
  const [isHovered, setIsHovered] = useState(false);

  const handleMouseEnter = () => {
    setIsHovered(true);
  };

  const handleMouseLeave = () => {
    setIsHovered(false);
  };

  return (
    <TableRow
      onClick={(event) => onClick(event, detector)}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
      sx={{ cursor: 'pointer', backgroundColor: isHovered ? '#f0f0f0' : 'inherit' }}
    >
      <TableCell sx={{ textAlign: 'center' }}>{detector.id}</TableCell>
      <TableCell sx={{ textAlign: 'center' }}>{detector.name}</TableCell>
      <TableCell sx={{ textAlign: 'center' }}>{detector.isOnline ? '✓' : '❌'}</TableCell>
    </TableRow>
  );
};

export default DetectorTable;

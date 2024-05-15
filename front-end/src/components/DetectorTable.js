import React, { useState, useEffect } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import axios from 'axios';
import Popup from './Popup';
import ResponsiveAppBar from './Nav';
import { useAuth } from './AuthContext';
import { useNavigate } from 'react-router-dom';

const DetectorTable = () => {
  const { token, userRole, userId } = useAuth();
  const [detectors, setDetectors] = useState([]);
  const [selectedDetector, setSelectedDetector] = useState(null);
  const [popup, setPopup] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        let params = {};
        if (!userRole.includes('ADMIN')) {
          params = {
            ownerId: userId
          };
        }
        const response = await axios.get('http://localhost:8000/detectors', {
          params: params,
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.status === 200) {
          setDetectors(response.data);
        }
      } catch (error) {
        console.error('Error:', error);
      }
    };


    fetchData();

    const intervalId = setInterval(() => {
      fetchData();
    }, 15000);

    return () => {
      clearInterval(intervalId);
    };
  }, [token, userRole, userId]);

  const handleClick = (event, detector, columnIndex) => {
    if (columnIndex === 3) {
      setSelectedDetector(detector);
    } else {
      setSelectedDetector(detector);
      setPopup(event.currentTarget);
    }
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
                <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Nombre</TableCell>
                <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Ubicación</TableCell>
                <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Activo</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {detectors.map((detector) => (
                <DetectorRow key={detector.id} detector={detector} onClick={(event, columnIndex) => handleClick(event, detector, columnIndex)} />
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
  const navigate = useNavigate();

  const handleMouseEnter = () => {
    setIsHovered(true);
  };

  const handleMouseLeave = () => {
    setIsHovered(false);
  };

  const handleClick = (event, columnIndex) => {
    if (columnIndex === 3) { 
      navigate(`/Heartbeats?selectedDetector=${detector.id}`);
    }
  };

  return (
    <TableRow
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
      sx={{ cursor: 'pointer', backgroundColor: isHovered ? '#f0f0f0' : 'inherit' }}
    >
      <TableCell
        onClick={(event) => onClick(event, 0)}
        sx={{ textAlign: 'center', color: 'black' }}
      >
        {detector.id}
      </TableCell>
      <TableCell onClick={(event) => onClick(event, 1)} sx={{ textAlign: 'center' }}>{detector.name}</TableCell>
      <TableCell onClick={(event) => onClick(event, 2)} sx={{ textAlign: 'center' }}>{detector.description}</TableCell>
      <TableCell onClick={(event) => handleClick(event, 3)} sx={{ textAlign: 'center', color: detector.isOnline ? 'green' : 'red' }}>{detector.isOnline ? '✓' : '❌'}</TableCell>
    </TableRow>
  );
};

export default DetectorTable;

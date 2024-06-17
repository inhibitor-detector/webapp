import React, { useState, useEffect, useCallback, useMemo } from 'react';
import axios from 'axios';
import Popup from './Popup';
import SelectOrder from './Select';
import ResponsiveAppBar from './Nav';
import { useAuth } from './AuthContext';
import { useNavigate } from 'react-router-dom';
import { CheckCircleOutline, HighlightOff } from '@mui/icons-material';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Typography, TextField } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import './DetectorTable.css';
import { refreshToken } from './AuthService';

const DetectorTable = () => {
  const { token, userRole, userId, exp, saveToken, setExp } = useAuth();
  const [detectors, setDetectors] = useState([]);
  const [selectedDetector, setSelectedDetector] = useState(null);
  const [popup, setPopup] = useState(null);
  const [orderType, setOrderType] = useState('');
  const [activeCount, setActiveCount] = useState(0);
  const [inactiveCount, setInactiveCount] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResultsMessage, setSearchResultsMessage] = useState('');

  const fetchAllData = useCallback(async () => {
    let allDetectors = [];
    let page = 1;
    let hasMore = true;

    try {
      refreshToken(exp, setExp, saveToken);
      while (hasMore) {
        let params = { page };
        if (!userRole.includes('ADMIN')) {
          params.ownerId = userId;
        }
        const response = await axios.get('http://localhost:8000/detectors', {
          params: params,
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.status === 200) {
          const data = response.data;
          if (data.length === 0) {
            hasMore = false;
          } else {
            allDetectors = [...allDetectors, ...data];
            page++;
          }
        } else {
          hasMore = false;
        }
      }
      allDetectors.sort((a, b) => a.id - b.id);
      setDetectors(allDetectors);
      setActiveCount(allDetectors.filter(detector => detector.isOnline).length);
      setInactiveCount(allDetectors.filter(detector => !detector.isOnline).length);
    } catch (error) {
      console.error('Error fetching detectors:', error);
    }
  }, [token, userRole, userId, saveToken, exp, setExp]);

  useEffect(() => {
    fetchAllData();
    const intervalId = setInterval(() => {
      fetchAllData();
    }, 60000);

    return () => {
      clearInterval(intervalId);
    };
  }, [fetchAllData]);

  const sortedDetectors = useMemo(() => {
    let sorted = [...detectors];
    if (orderType === 'Id') {
      sorted.sort((a, b) => a.id - b.id);
    } else if (orderType === 'Activo') {
      sorted.sort((a, b) => (a.isOnline === b.isOnline) ? 0 : a.isOnline ? -1 : 1);
    } else if (orderType === 'Desactivado') {
      sorted.sort((a, b) => (a.isOnline === b.isOnline) ? 0 : a.isOnline ? 1 : -1);
    }
    return sorted;
  }, [detectors, orderType]);

  const filteredDetectors = sortedDetectors.filter(detector =>
    detector.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    detector.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

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

  useEffect(() => {
    if (searchTerm.trim() !== '') {
      if (filteredDetectors.length === 0) {
        setSearchResultsMessage('No se encontraron resultados');
      } else {
        setSearchResultsMessage('');
      }
    } else {
      setSearchResultsMessage('');
    }
  }, [filteredDetectors, searchTerm]);

  return (
    <div>
      <ResponsiveAppBar />
      <div style={{ paddingTop: '20px', maxWidth: '95%', margin: '0 auto', display: 'flex', flexDirection: 'column' }}>
        <TextField
          className="search-field"
          label="Buscar por nombre"
          variant="outlined"
          size="small"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          InputProps={{
            endAdornment: <SearchIcon />,
          }}
        />
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
          <Typography variant="body1">
            <CheckCircleOutline sx={{ fontSize: 18, verticalAlign: 'middle', color: '#4CAF50', marginRight: '5px' }} />
            Total Activos: {activeCount}
          </Typography>
          <Typography variant="body1">
            <HighlightOff sx={{ fontSize: 18, verticalAlign: 'middle', color: '#F44336', marginRight: '5px' }} />
            Total Inactivos: {inactiveCount}
          </Typography>
          <SelectOrder setOrderType={setOrderType} />
        </div>

        {!searchResultsMessage && (
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
                {filteredDetectors.map((detector) => (
                  <DetectorRow key={detector.id} detector={detector} onClick={(event, columnIndex) => handleClick(event, detector, columnIndex)} />
                ))}
              </TableBody>
            </Table>
          </TableContainer>)}
        {searchResultsMessage && (
          <Typography
            variant="body1"
            style={{
              textAlign: 'center',
              padding: '10px',
              color: 'grey'
            }}
          >
            {searchResultsMessage}
          </Typography>
        )}

      </div>
      <Popup popup={popup} selectedDetector={selectedDetector} onClose={handleClose} />
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
      <TableCell onClick={(event) => handleClick(event, 3)} sx={{ textAlign: 'center' }}>
        {detector.isOnline ? (
          <CheckCircleOutline sx={{ color: 'green', fontSize: 18 }} />
        ) : (
          <HighlightOff sx={{ color: 'red', fontSize: 18 }} />
        )}
      </TableCell>
    </TableRow>
  );
};

export default DetectorTable;

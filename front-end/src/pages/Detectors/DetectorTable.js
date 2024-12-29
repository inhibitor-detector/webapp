import React, { useState, useEffect, useCallback, useMemo } from 'react';
import axios from 'axios';
import SelectOrder from '../../components/Select/Select';
import ResponsiveAppBar from '../../layouts/Nav';
import DashboardStats from '../../layouts/Dashboard';
import { useAuth } from '../../components/AuthContext';
import { useNavigate } from 'react-router-dom';
import { CheckCircleOutline, HighlightOff } from '@mui/icons-material';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Typography, TextField, Box, CircularProgress } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import './DetectorTable.css';

const DetectorTable = () => {
  const { token, userRole, userId } = useAuth();
  const [detectors, setDetectors] = useState([]);
  const [orderType, setOrderType] = useState('');
  const [activeCount, setActiveCount] = useState(0);
  const [inactiveCount, setInactiveCount] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResultsMessage, setSearchResultsMessage] = useState('');
  const [loading, setLoading] = useState(true);
  const [users, setUsers] = useState({});

  const fetchAllData = useCallback(async () => {
    setLoading(true);
    let allDetectors = [];
    let page = 1;
    let hasMore = true;

    try {
      while (hasMore) {
        let params = { page };
        if (userRole && !userRole.includes('ADMIN')) {
          params.ownerId = userId;
        }
        const response = await axios.get('http://localhost:8001/detectors', {
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
      if (userRole && userRole.includes('ADMIN')) {
        const userIds = [...new Set(allDetectors.map(detector => detector.ownerId))];
        await fetchUsers(userIds);
      }
    } catch (error) {
      console.error('Error fetching detectors:', error);
    }
    setLoading(false);
    // eslint-disable-next-line
  }, [token, userRole, userId]);

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
    if (orderType === 'Id Ascendente') {
      sorted.sort((a, b) => a.id - b.id);
    } else if (orderType === 'Id Descendente') {
      sorted.sort((a, b) => b.id - a.id);
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

  const fetchUsers = async (userIds) => {
    const userRequests = userIds.map(userId =>
      axios.get(`http://localhost:8001/users/${userId}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      })
    );

    try {
      const responses = await Promise.all(userRequests);
      const userMap = {};
      responses.forEach(response => {
        if (response.status === 200) {
          userMap[response.data.id] = response.data.username;
        }

      });
      setUsers(userMap);
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  return (
    <div>
      <ResponsiveAppBar />
      <div style={{ textAlign: 'center', marginTop: '20px' }}>
        <Typography
          variant="h4"
          style={{
            fontWeight: 'bold',
          }}
        >
          Detectores
        </Typography>
      </div>
      <DashboardStats activeCount={activeCount} inactiveCount={inactiveCount} totalCount={activeCount + inactiveCount} />
      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexGrow: 1 }}>
          <CircularProgress sx={{ color: '#8bc34a' }} />
        </Box>
      ) : (
        <div style={{ maxWidth: '95%', margin: '0 auto', display: 'flex', flexDirection: 'column' }}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "10px", gap: "16px" }}>
            <TextField
              className="search-field"
              label="Buscar por nombre"
              size="small"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              InputProps={{
                endAdornment: <SearchIcon />,
              }}
            />
            <SelectOrder
              setOrderType={setOrderType}
            />
          </div>

          {!searchResultsMessage && (
            <TableContainer component={Paper}>
              <Table>
                <TableHead>
                  <TableRow >
                    <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Id</TableCell>
                    <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Nombre</TableCell>
                    <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Ubicación</TableCell>
                    {userRole.includes('ADMIN') && (
                      <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Usuario</TableCell>
                    )}
                    <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Activo</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {filteredDetectors.map((detector) => (
                    <DetectorRow key={detector.id} detector={detector} users={users} userRole={userRole} />
                  ))}
                </TableBody>
              </Table>
            </TableContainer>)}
          {searchResultsMessage && (
            <Typography
              style={{
                textAlign: 'center',
                padding: '10px',
                color: 'grey'
              }}
            >
              {searchResultsMessage}
            </Typography>
          )}
        </div>)}
    </div>
  );
};

const DetectorRow = ({ detector, users, userRole }) => {
  const navigate = useNavigate();

  const handleClick = (event) => {
    navigate(`/Heartbeats?selectedDetector=${detector.id}`);
  };

  return (
    <TableRow sx={{
      backgroundColor: detector.isOnline ? '' : 'rgba(255, 0, 0, 0.1)',
    }}
    >
      <TableCell
        onClick={(event) => handleClick(event, 0)}
        sx={{ textAlign: 'center', color: 'black' }}
      >
        {detector.id}
      </TableCell>
      <TableCell sx={{ textAlign: 'center' }}>{detector.name}</TableCell>
      <TableCell sx={{ textAlign: 'center' }}>{detector.description}</TableCell>
      {userRole.includes('ADMIN') && (
        <TableCell sx={{ textAlign: 'center' }}>{users[detector.ownerId] || 'Cargando...'}</TableCell> // Aquí puedes reemplazarlo con el username si lo tienes disponible
      )}
      <TableCell onClick={(event) => handleClick(event)} sx={{ textAlign: 'center', cursor: 'pointer' }}>
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

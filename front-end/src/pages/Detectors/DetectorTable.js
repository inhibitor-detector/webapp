import { CheckCircleOutline, HighlightOff, RemoveCircleOutline } from '@mui/icons-material';
import BlockIcon from "@mui/icons-material/Block";
import DevicesIcon from "@mui/icons-material/Devices";
import DoneIcon from "@mui/icons-material/Done";
import ReportProblemIcon from "@mui/icons-material/ReportProblem";
import SearchIcon from '@mui/icons-material/Search';
import { Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Typography } from '@mui/material';
import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getDetectors } from '../../api/DetectorApi';
import { getUserById } from '../../api/UserApi';
import { useAuth } from '../../components/AuthContext';
import LoadingBox from '../../components/LoadingBox';
import SelectOrder from '../../components/Select/Select';
import Title from '../../components/Title';
import { decodeStatus } from '../../components/utils/decodeStatus.js';
import DashboardCard from '../../layouts/DashboardCard';
import ResponsiveAppBar from '../../layouts/Nav';
import './DetectorTable.css';

const DetectorTable = () => {
  const { token, userRole, userId } = useAuth();
  const [detectors, setDetectors] = useState([]);
  const [orderType, setOrderType] = useState('');
  const [activeCount, setActiveCount] = useState(0);
  const [inactiveCount, setInactiveCount] = useState(0);
  const [failedCount, setFailedCount] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResultsMessage, setSearchResultsMessage] = useState('');
  const [loading, setLoading] = useState(true);
  const [users, setUsers] = useState({});

  const detectorsRef = useRef([]);

  const fetchAllData = useCallback(async () => {
    let allDetectors = [];
    let page = 1;
    let hasMore = true;

    const fetchUsers = async (userIds) => {
      const userMap = {};
      try {
        for (const userId of userIds) {
          const user = await getUserById(userId, token);
          userMap[userId] = user.username;
        }
        setUsers(userMap);
      } catch (error) {
        console.error('Error fetching users:', error);
      }
    };

    try {
      while (hasMore) {
        let params = { page };
        if (userRole && !userRole.includes('ADMIN')) {
          params.ownerId = userId;
        }
        const response = await getDetectors(params, token);
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

      if (JSON.stringify(allDetectors) !== JSON.stringify(detectorsRef.current)) {
        detectorsRef.current = allDetectors;
        setDetectors(allDetectors);
        if (userRole && userRole.includes('ADMIN')) {
          const userIds = [...new Set(allDetectors.map(detector => detector.ownerId))];
          await fetchUsers(userIds);
        }
      }
    } catch (error) {
      console.error('Error fetching detectors:', error);
    }
    setLoading(false);
  }, [token, userRole, userId]);

  useEffect(() => {
    setFailedCount(detectors.filter(detector => detector.isOnline && detector.status !== 1).length);
    setActiveCount(detectors.filter(detector => detector.isOnline && detector.status === 1).length);
    setInactiveCount(detectors.filter(detector => !detector.isOnline).length);
  }, [detectors]);

  useEffect(() => {
    setLoading(true);
    fetchAllData();
    const intervalId = setInterval(fetchAllData, 2000);
    return () => clearInterval(intervalId);
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

  return (
    <div>
      <ResponsiveAppBar />
      <Title title={'Detectores'} />
      <DashboardCard stats={[
        {
          label: "Total Activos",
          value: activeCount,
          icon: <DoneIcon />,
          backgroundColor: "#66BB6A",
        },
        {
          label: "Total Activos con Fallas",
          value: failedCount,
          icon: <ReportProblemIcon />,
          backgroundColor: "#FFD54F",
        },
        {
          label: "Total Inactivos",
          value: inactiveCount,
          icon: <BlockIcon />,
          backgroundColor: "#EF5350",
        },
        {
          label: "Total Detectores",
          value: activeCount + inactiveCount + failedCount,
          icon: <DevicesIcon />,
          backgroundColor: "#42A5F5",
        },
      ]} />
      {loading ? (
        <LoadingBox />
      ) : (detectors.length === 0 ? (
        <Typography
          variant="h6"
          sx={{
            textAlign: 'center',
            padding: '20px',
          }}
        >
          No hay detectores
        </Typography>
      ) : (
        <div style={{ maxWidth: '95%', margin: '0 auto', display: 'flex', flexDirection: 'column' }}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "16px" }}>
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
          </div>
          <SelectOrder setOrderType={setOrderType} style={{ marginBottom: "10px" }} />

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
                    <TableCell sx={{ color: '#8bc34a', fontSize: '1.1rem', textAlign: 'center' }}>Estado</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {filteredDetectors.map((detector) => (
                    <DetectorRow key={detector.id} detector={detector} users={users} userRole={userRole} decodeStatus={decodeStatus} />
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
        </div>
      ))}
    </div>
  );
};

const DetectorRow = ({ detector, users, userRole, decodeStatus }) => {
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
        <TableCell sx={{ textAlign: 'center' }}>{users[detector.ownerId] || 'Cargando...'}</TableCell>
      )}
      <TableCell
        onClick={(event) => handleClick(event)}
        sx={{ textAlign: "center", cursor: "pointer" }}
      >
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            width: 24,
            height: 24,
            borderRadius: "50%",
            backgroundColor: "rgba(200, 200, 200, 0.3)",
            margin: "auto",
            "&:hover": {
              backgroundColor: "rgba(150, 150, 150, 0.5)",
            },
          }}
        >
          {detector.isOnline && (detector.status === 1 || detector.status === 65 )? (
            <CheckCircleOutline sx={{ color: "green", fontSize: 18 }} />
          ) : !detector.isOnline ? (
            <HighlightOff sx={{ color: "red", fontSize: 18 }} />
          ) : (
            <RemoveCircleOutline sx={{ color: "#FFD54F", fontSize: 18 }} />
          )}
        </Box>
      </TableCell>
      <TableCell sx={{ textAlign: 'center' }}>{decodeStatus(detector.status)}</TableCell>
    </TableRow>
  );
};

export default DetectorTable;

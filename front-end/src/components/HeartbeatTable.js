import React, { useState, useEffect, useCallback } from 'react';
import { TableContainer, Table, TableHead, TableBody, TableRow, TableCell, Paper, Button, Box, Typography, CircularProgress } from '@mui/material';
import { useLocation } from 'react-router-dom';
import { useAuth } from './AuthContext';
import axios from 'axios';
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import { CheckCircleOutline, HighlightOff } from '@mui/icons-material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import './HeartbeatTable.css'
import { refreshToken } from './AuthService';
import { formatDistanceToNow } from 'date-fns';
import { es } from 'date-fns/locale';

const HeartbeatTable = () => {
  const { token, userRole, userId, exp, saveToken, setExp, saveUserId } = useAuth();
  const [heartbeats, setHeartbeats] = useState([]);
  const [loading, setLoading] = useState(true);
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const selectedDetector = searchParams.get('selectedDetector');
  const [currentPage, setCurrentPage] = useState(1);
  const [hasMorePages, setHasMorePages] = useState(true);

  const styles = {
    container: {
      flexGrow: 1,
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      position: 'relative',
    },
    background: {
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundImage: `url('/logo.png')`,
      backgroundRepeat: 'no-repeat',
      backgroundPosition: 'center',
      opacity: 0.2,
      zIndex: 0,
    },
  };

  const handleGoBack = () => {
    window.history.back();
  };

  const fetchData = useCallback(async (page) => {
    setLoading(true);
    try {
      refreshToken(exp, setExp, saveToken, saveUserId);
      let params = {
        detectorId: selectedDetector,
        page: page,
        isHeartbeat: true
      };
      if (!userRole.includes('ADMIN')) {
        params.ownerId = userId;
      }
      const response = await axios.get('http://localhost:80/signals', {
        params,
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.status === 200) {
        if (response.data.length > 0) {
          setHeartbeats(response.data);
          setCurrentPage(page);
          setHasMorePages(response.data.length >= 10);
        } else {
          setHeartbeats([]);
          setCurrentPage(1);
          setHasMorePages(false);
        }
      }
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  }, [selectedDetector, userId, userRole, token, exp, saveToken, setExp]);

  useEffect(() => {
    fetchData(1);
  }, [selectedDetector, fetchData]);

  const handleNextPage = () => {
    if (hasMorePages) {
      const nextPage = currentPage + 1;
      fetchData(nextPage);
    }
  };

  const handlePrevPage = () => {
    if (currentPage > 1) {
      const prevPage = currentPage - 1;
      fetchData(prevPage);
    }
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <div>
        <Button
          variant="contained"
          onClick={handleGoBack}
          sx={{
            backgroundColor: '#8bc34a',
            color: 'white',
            '&:hover': {
              backgroundColor: '#8bc34a',
            },
            '& .MuiButton-startIcon': {
              marginRight: '4px',
            },
          }}
          startIcon={<ArrowBackIcon />}
        >
          Volver
        </Button>
      </div>
      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexGrow: 1 }}>
          <CircularProgress sx={{ color: '#8bc34a' }} />
        </Box>
      ) : heartbeats.length > 0 ? (
        <div>
          <TableContainer component={Paper} sx={{ flexGrow: 1 }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell sx={{ textAlign: 'center', verticalAlign: 'middle' }}>Heartbeat</TableCell>
                  <TableCell sx={{ textAlign: 'center', verticalAlign: 'middle' }}>Tiempo</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {heartbeats.map((data, index) => (
                  <TableRow key={index}>
                    <TableCell sx={{ textAlign: 'center', verticalAlign: 'middle' }}>
                      {data.isHeartbeat ? (
                        <CheckCircleOutline sx={{ color: 'green', fontSize: 18 }} />
                      ) : (
                        <HighlightOff sx={{ color: 'red', fontSize: 18 }} />
                      )}
                    </TableCell>
                    <TableCell sx={{ textAlign: 'center', verticalAlign: 'middle' }}>
                      {formatDistanceToNow(new Date(data.timestamp), { addSuffix: true, locale: es })}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              position: 'fixed',
              bottom: 1,
              width: '100%',
              backgroundColor: 'white',
              padding: '10px 0',
            }}
          >
            <Button
              onClick={handlePrevPage}
              disabled={currentPage === 1}
              className="custom-button"
            >
              <ArrowBackIosIcon
                sx={{
                  fontSize: '20px',
                  color: currentPage === 1 ? 'rgba(139, 195, 74, 0.5)' : '#8bc34a',
                }}
              />
            </Button>
            <Button
              onClick={handleNextPage}
              disabled={!hasMorePages}
              className="custom-button"
            >
              <ArrowForwardIosIcon
                sx={{
                  fontSize: '20px',
                  color: !hasMorePages ? 'rgba(139, 195, 74, 0.5)' : '#8bc34a',
                }}
              />
            </Button>
          </Box>
        </div>
      ) : (
        <Box sx={styles.container}>
          <div style={styles.background} />
          <Typography variant="body1" sx={{
            fontSize: '22px',
          }}>
            No se detectaron señales
          </Typography>
        </Box>
      )}
    </div>
  );
};

export default HeartbeatTable;

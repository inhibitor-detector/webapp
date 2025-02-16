import { CheckCircleOutline } from '@mui/icons-material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import { Box, Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@mui/material';
import { formatDistanceToNow } from 'date-fns';
import { es } from 'date-fns/locale';
import React, { useCallback, useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { getSignals } from '../../api/SignalApi';
import { useAuth } from '../../components/AuthContext';
import './HeartbeatTable.css';
import LoadingBox from '../../components/LoadingBox';

const HeartbeatTable = () => {
  const { token, userRole, userId } = useAuth();
  const [heartbeats, setHeartbeats] = useState([]);
  const [loading, setLoading] = useState(true);
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const selectedDetector = searchParams.get('selectedDetector');
  const [pagination, setPagination] = useState({ currentPage: 1, hasMorePages: true });

  const styles = {
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
    },
  };

  const fetchData = useCallback(async (page) => {
    setLoading(true);
    try {
      let params = {
        detectorId: selectedDetector,
        page,
        isHeartbeat: true
      };
      if (!userRole.includes('ADMIN')) {
        params.ownerId = userId;
      }
      const response = await getSignals(params, token);
      if (response.status === 200) {
        setHeartbeats(response.data);
        setPagination({ currentPage: page, hasMorePages: response.data.length >= 10 });
      }
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  }, [selectedDetector, userId, userRole, token]);

  useEffect(() => {
    fetchData(1);
  }, [fetchData]);

  const handlePageChange = (next) => {
    fetchData(pagination.currentPage + (next ? 1 : -1));
  };

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <div>
        <Button
          variant="contained"
          onClick={() => window.history.back()}
          className='back-button'
          startIcon={<ArrowBackIcon />}
        >
          Volver
        </Button>
      </div>
      {loading ? (
        <LoadingBox />
      ) : heartbeats.length > 0 ? (
        <div>
          <TableContainer component={Paper} sx={{ flexGrow: 1 }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell sx={{ textAlign: 'center' }}>Heartbeat</TableCell>
                  <TableCell sx={{ textAlign: 'center' }}>Tiempo</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {heartbeats.map((data, index) => (
                  <TableRow key={index}>
                    <TableCell sx={{ textAlign: 'center' }}>
                      <CheckCircleOutline sx={{ color: 'green', fontSize: 18 }} />
                    </TableCell>
                    <TableCell sx={{ textAlign: 'center', verticalAlign: 'middle' }}>
                      {formatDistanceToNow(new Date(data.timestamp), { addSuffix: true, locale: es })}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
          <Box className='box-arrows'>
            <Button onClick={() => handlePageChange(false)} disabled={pagination.currentPage === 1} className="custom-button">
              <ArrowBackIosIcon
                sx={{
                  fontSize: '20px',
                  color: pagination.currentPage === 1 ? 'rgba(139, 195, 74, 0.5)' : '#8bc34a',
                }}
              />
            </Button>
            <Button onClick={() => handlePageChange(true)} disabled={!pagination.hasMorePages} className="custom-button">
              <ArrowForwardIosIcon
                sx={{
                  fontSize: '20px',
                  color: !pagination.hasMorePages ? 'rgba(139, 195, 74, 0.5)' : '#8bc34a',
                }}
              />
            </Button>
          </Box>
        </div>
      ) : (
        <Box className="container-box">
          <div style={styles.background} />
          <Typography variant="body1" sx={{ fontSize: '22px' }}>
            No se detectaron señales
          </Typography>
        </Box>
      )}
    </div>
  );
};

export default HeartbeatTable;

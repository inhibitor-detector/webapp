import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';

axios.defaults.baseURL = 'http://localhost:8000';

const DetectorTable = () => {
  const [detectors, setDetectors] = useState([]);

  const fetchDetectors = async () => {
    try {
      const response = await axios.get('/detector_ids');
      const detectorIds = response.data.detector_ids;
      const detectorDataPromises = detectorIds.map(id => axios.get(`/detector/${id}`));
      const detectorDataResponses = await Promise.all(detectorDataPromises);
      const detectorsData = detectorDataResponses.map(response => response.data.Detector);
      setDetectors(detectorsData);
    } catch (error) {
      console.error('Error fetching detectors:', error);
      setDetectors([]);
    }
  };

  useEffect(() => {
    fetchDetectors();
  }, []);

  return (
    <div>
      <h2>Detectores habilitados:</h2>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell sx={{ color: '#8bc34a',fontSize: '1.1rem', textAlign: 'center' }}>CLIENTE</TableCell>
              <TableCell sx={{ color: '#8bc34a',fontSize: '1.1rem', textAlign: 'center' }}>UBICACION</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {detectors.map(detector => (
              <TableRow key={detector.id}>
                <TableCell sx={{textAlign: 'center' }}>{detector.client}</TableCell>
                <TableCell sx={{textAlign: 'center' }}>{detector.location}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};

export default DetectorTable;

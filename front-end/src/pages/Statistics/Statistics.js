import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, ResponsiveContainer } from 'recharts';
import { useAuth } from '../../components/AuthContext';
import ResponsiveAppBar from '../../layouts/Nav';
import { CircularProgress, Box, Select, MenuItem } from '@mui/material';

const SignalsChart = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [range, setRange] = useState(24);
  const { token, userRole, userId } = useAuth();

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);

      let allSignals = [];
      let hasMore = true;
      let page = 1;

      try {
        while (hasMore) {
          let params = { page, isHeartbeat: false };
          if (!userRole.includes('ADMIN')) {
            params.ownerId = userId;
          }
          const response = await axios.get('http://localhost:8001/signals', {
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
              allSignals = [...allSignals, ...data];
              page++;
            }
          } else {
            hasMore = false;
          }
        }

        const processedData = processSignals(allSignals, range);
        setData(processedData);
      } catch (error) {
        console.error('Error:', error);
      }

      setLoading(false);
    };

    fetchData();
  }, [token, userRole, userId, range]);

  return (
    <div>
      <ResponsiveAppBar />
      <Box sx={{ p: 2, display: 'flex', justifyContent: 'center', borderColor: 'red' }}>
        <Select value={range} onChange={(event) => setRange(event.target.value)}
          sx={{
            '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
              borderColor: '#8bc34a',
            },
          }}
        >
          <MenuItem value={24}>Últimas 24 horas</MenuItem>
          <MenuItem value={12}>Últimas 12 horas</MenuItem>
          <MenuItem value={6}>Últimas 6 horas</MenuItem>
          <MenuItem value={1}>Última hora</MenuItem>
        </Select>
      </Box>
      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexGrow: 1 }}>
          <CircularProgress sx={{ color: '#8bc34a' }} />
        </Box>
      ) : (
        <ResponsiveContainer width="100%" height={400}>
          <BarChart data={data} margin={{ top: 20, right: 30, left: 20, bottom: 60 }}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis
              dataKey="time"
              label={{
                value: 'Hora',
                position: 'insideBottomRight',
                offset: -5
              }}
            />
            <YAxis
              label={{
                value: 'Alertas',
                angle: -90,
                position: 'insideLeft'
              }}
            />
            <Bar dataKey="count" fill="#8bc34a" barSize="20" />
          </BarChart>
        </ResponsiveContainer>

      )}
    </div>
  );
};

const processSignals = (signals, range) => {
  const signalsByTime = {};

  const now = new Date();
  const rangeStartTime = new Date(now - range * 60 * 60 * 1000);

  signals.forEach((signal) => {
    const signalDate = new Date(signal.timestamp);
    if (signalDate >= rangeStartTime) {
      let timeLabel;

      if (range === 1) {
        const interval = Math.floor(signalDate.getMinutes() / 15) * 15;
        timeLabel = `${signalDate.getHours().toString().padStart(2, '0')}:${interval.toString().padStart(2, '0')}`;
      } else {
        const hour = signalDate.getHours();
        timeLabel = `${hour.toString().padStart(2, '0')}:00`;
      }

      signalsByTime[timeLabel] = (signalsByTime[timeLabel] || 0) + 1;
    }
  });

  const processedData = [];
  for (let i = 0; i < range; i++) {
    const timeSlot = new Date(now - (range - i - 1) * 60 * 60 * 1000);
    const baseHour = timeSlot.getHours();

    if (range === 1) {
      for (let m = 0; m <= 45; m += 15) {
        const intervalTime = new Date(timeSlot.setMinutes(m));
        const timeLabel = `${intervalTime.getHours().toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;
        processedData.push({
          time: timeLabel,
          count: signalsByTime[timeLabel] || 0
        });
      }
    } else {
      const timeLabel = `${baseHour.toString().padStart(2, '0')}:00`;
      processedData.push({
        time: timeLabel,
        count: signalsByTime[timeLabel] || 0
      });
    }
  }

  return processedData;
};

export default SignalsChart;

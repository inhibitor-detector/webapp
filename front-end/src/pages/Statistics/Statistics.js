import { Box, MenuItem, Select } from '@mui/material';
import React, { useEffect, useState } from 'react';
import { Bar, BarChart, CartesianGrid, ResponsiveContainer, XAxis, YAxis } from 'recharts';
import { getSignalsByTime } from '../../api/SignalApi';
import { useAuth } from '../../components/AuthContext';
import LoadingBox from '../../components/LoadingBox';
import Title from '../../components/Title';
import ResponsiveAppBar from '../../layouts/Nav';

const SignalsChart = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [range, setRange] = useState(24);
  const { token, userRole, userId } = useAuth();

  const formatDate = (date) => {
    return new Date(date).toLocaleString('sv-SE', { timeZone: 'America/Argentina/Buenos_Aires' }).replace(' ', 'T');
  };

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);

      const now = new Date();
      const endTime = formatDate(now);
      const startTime = formatDate(new Date(now - range * 60 * 60 * 1000));

      try {
        const params = {
          startTime,
          endTime,
        };

        if (!userRole.includes('ADMIN')) {
          params.ownerId = userId;
        }

        const response = await getSignalsByTime(params, token);

        if (response.status === 200) {
          const processedData = processSignals(response.data, range);
          setData(processedData);
        }
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
      <Title title={'Estadisticas'}/>
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
        <LoadingBox/>
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
              allowDecimals={false}
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

  signals.forEach((signal) => {
    const signalDate = new Date(signal.timestamp);
    let timeLabel;

    if (range === 1) {
      const interval = Math.ceil(signalDate.getMinutes() / 5) * 5;
      timeLabel = `${signalDate.getHours().toString().padStart(2, '0')}:${interval.toString().padStart(2, '0')}`;
    } else {
      const hour = signalDate.getHours();
      timeLabel = `${hour.toString().padStart(2, '0')}:00`;
    }

    signalsByTime[timeLabel] = (signalsByTime[timeLabel] || 0) + 1;
  });

  const processedData = [];
  const now = new Date();

  if (range === 1) {
    let roundedMinutes = Math.ceil(now.getMinutes() / 5) * 5 + 5;
    const roundedTime = new Date(now.setMinutes(roundedMinutes, 0, 0));
    roundedTime.setHours(roundedTime.getHours() - 1);

    for (let m = 0; m <= 11; m += 1) {
      const intervalTime = new Date(roundedTime.getTime());
      intervalTime.setMinutes(roundedMinutes);
      const timeLabel = `${intervalTime.getHours().toString().padStart(2, '0')}:${intervalTime.getMinutes().toString().padStart(2, '0')}`;
      processedData.push({
        time: timeLabel,
        count: signalsByTime[timeLabel] || 0
      });
      roundedMinutes += 5;
    }
  } else {
    for (let i = 0; i < range; i++) {
      const timeSlot = new Date(now - (range - i - 1) * 60 * 60 * 1000);
      const baseHour = timeSlot.getHours();
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

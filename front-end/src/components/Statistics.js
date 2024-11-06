import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { useAuth } from './AuthContext';
import ResponsiveAppBar from './Nav';
import { CircularProgress, Box } from '@mui/material';

const SignalsChart = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const { token, userRole, userId, exp, setExp, saveToken, saveUserId } = useAuth();

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
          const response = await axios.get('http://localhost:80/signals', {
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

        const processedData = processSignals(allSignals);
        setData(processedData);
      } catch (error) {
        console.error('Error:', error);
      }

      setLoading(false);
    };

    fetchData();
  }, [token, userRole, userId, exp, setExp, saveToken]);

  const processSignals = (signals) => {
    const signalsByHour = {};

    signals.forEach(signal => {
      const date = new Date(signal.timestamp);

      const twentyFourHoursAgo = new Date(new Date() - 24 * 60 * 60 * 1000);

      if (date >= twentyFourHoursAgo) {
        const hour = date.getHours();

        if (!signalsByHour[hour]) {
          signalsByHour[hour] = 0;
        }

        signalsByHour[hour] += 1;
      }
    });

    const now = new Date();
    const processedData = [];

    for (let i = 23; i >= 0; i--) {
      const hour = new Date(now - i * 60 * 60 * 1000).getHours();
      const timeLabel = `${hour.toString().padStart(2, '0')}:00`;
      processedData.push({
        time: timeLabel,
        count: signalsByHour[hour] || 0
      });
    }

    return processedData;
  };

  const filterLastHour = (data) => {
    return data.slice(-2);
  };

  return (
    <div>
      <ResponsiveAppBar />
      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexGrow: 1 }}>
          <CircularProgress sx={{ color: '#8bc34a' }} />
        </Box>
      ) : (
        <ResponsiveContainer width="100%" height={400}>
          <LineChart data={data} margin={{ top: 20, right: 30, left: 20}}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="time" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="count" name="Cantidad de inhibiciones en las últimas 24 horas" stroke="#8bc34a" />
          </LineChart>

          <LineChart data={filterLastHour(data)} margin={{ top: 20, right: 30, left: 20 }}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="time" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="count" name="Cantidad de inhibiciones en la última hora" stroke="#8bc34a" />
          </LineChart>
        </ResponsiveContainer>
      )}
    </div>
  );
};

export default SignalsChart;

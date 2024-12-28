import React from 'react';
import DetectorTable from './components/DetectorTable';
import SignalTable from './components/SignalTable';
import SignIn from './components/auth/Signin';
import { Route, Routes } from 'react-router-dom';
import PrivateRoutes from './components/PrivateRoutes'
import HeartbeatTable from './components/HeartbeatTable';
import InhibitionDetected from './components/InhibitionDetected';
import SignalsChart from './components/Statistics';

const App = () => {

  return (
    <div>
      <InhibitionDetected />
      <Routes>
        <Route path='/' element={<SignIn />} />
        <Route element={<PrivateRoutes />}>
          <Route path='Heartbeats' element={<HeartbeatTable />} />
          <Route path='Detectores' element={<DetectorTable />} />
          <Route path='Alertas' element={<SignalTable />} />
          <Route path='Estadisticas' element={<SignalsChart />} />
        </Route>
      </Routes>
    </div>
  );
};

export default App;

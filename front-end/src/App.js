import React from 'react';
import DetectorTable from './pages/Detectors/DetectorTable';
import SignalTable from './pages/Inhibitions/SignalTable';
import SignIn from './pages/SignIn/Signin';
import { Route, Routes } from 'react-router-dom';
import PrivateRoutes from './components/PrivateRoutes'
import HeartbeatTable from './components/HeartbeatTable/HeartbeatTable';
import InhibitionDetected from './components/InhibitionDetected';
import SignalsChart from './pages/Statistics/Statistics';

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

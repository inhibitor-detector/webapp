import React from 'react';
import { Route, Routes } from 'react-router-dom';
import InhibitionDetected from './components/InhibitionDetected';
import PrivateRoutes from './components/PrivateRoutes';
import DetectorTable from './pages/Detectors/DetectorTable';
import HeartbeatTable from './pages/HeartbeatTable/HeartbeatTable';
import SignIn from './pages/SignIn/Signin';
import SignalTable from './pages/Signals/SignalTable';
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

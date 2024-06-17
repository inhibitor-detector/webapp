import React from 'react';
import DetectorTable from './components/DetectorTable';
import SignalTable from './components/SignalTable';
import SignIn from './components/Signin';
import { Route, Routes } from 'react-router-dom';
import PrivateRoutes from './components/PrivateRoutes'
import HeartbeatTable from './components/HeartbeatTable';
import InhibitionDetected from './components/InhibitionDetected';

const App = () => {

  return (
    <div>
      <InhibitionDetected />
      <Routes>
      <Route path='/' element={<SignIn/>}/>
        <Route element={<PrivateRoutes />}>
          <Route path='Heartbeats' element={<HeartbeatTable/>}/>
          <Route path='Detectores' element={<DetectorTable/>}/>
          <Route path='Inhibiciones' element={<SignalTable/>}/>
        </Route>
      </Routes>
    </div>
  );
};

export default App;

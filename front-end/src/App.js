import React, { useState }  from 'react';
import DetectorTable from './components/DetectorTable';
import SignalTable from './components/SignalTable';
import SignIn from './components/Signin';
import { Route, Routes } from 'react-router-dom';
import AlertContainer from './components/AlertContainer';
import PrivateRoutes from './components/PrivateRoutes'
import HeartbeatTable from './components/HeartbeatTable';

const App = () => {
  const [open, setOpen] = useState(true);

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div>
      <AlertContainer open={open} onClose={handleClose}/>
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

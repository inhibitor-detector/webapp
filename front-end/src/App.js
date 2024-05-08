import React, { useState }  from 'react';
import DetectorTable from './components/DetectorTable';
import SignalTable from './components/SignalTable';
import SignIn from './components/Signin';
import { Route, Routes } from 'react-router-dom';
import AlertContainer from './components/AlertContainer';
import detectorsData from './components/mock_data/detectors.json';

const App = () => {
  const [open, setOpen] = useState(true);

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div>
      <AlertContainer
        open={open}
        onClose={handleClose}
        selectedDetector={detectorsData.detectors[1]}
      />
      <Routes>
        <Route path='/' element={<SignIn/>}/>
        <Route path='Detectores' element={<DetectorTable/>}/>
        <Route path='Señales' element={<SignalTable/>}/>
      </Routes>
    </div>
  );
};

export default App;

import React, { useState } from 'react';
import Navbar from './Navbar';
import DetectorTable from './DetectorTable';
import SignalTable from './SignalTable';
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8000';

const App = () => {
  const [activeTab, setActiveTab] = useState('detectors');

  return (
    <div>
      <Navbar activeTab={activeTab} setActiveTab={setActiveTab} />
      {activeTab === 'detectors' && <DetectorTable/>}
      {activeTab === 'signals' && <SignalTable/>}
    </div>
  );
};

export default App;

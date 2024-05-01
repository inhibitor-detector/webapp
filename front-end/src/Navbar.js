import React from 'react';
import { Box, Tab, Tabs } from '@mui/material';

const Navbar = ({ activeTab, setActiveTab }) => {
  const handleChange = (event, newValue) => {
    setActiveTab(newValue);
  };

  return (
    <Box sx={{ bgcolor: '#f8f9fa', borderBottom: 1, borderColor: 'divider' }}>
      <Tabs 
      value={activeTab}
      onChange={handleChange}
      centered
      indicatorColor="primary"
      sx={{
            "& .MuiTabs-indicator": { backgroundColor: "#72cb10" },
            "& .Mui-selected": { color: '#72cb10 !important' }
          }}>
          <Tab label="Detectores" value="detectors" sx={{ color: '#8bc34a' }} />
          <Tab label="Señales" value="signals" sx={{ color: '#8bc34a' }} />
      </Tabs>
    </Box>
  );
};

export default Navbar;

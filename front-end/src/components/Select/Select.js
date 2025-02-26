import React from 'react';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import './Select.css';

export default function SelectOrder(props) {
  const [order, setOrder] = React.useState('');

  const handleChange = (event) => {
    const selectedOrder = event.target.value;
    setOrder(selectedOrder);
    props.setOrderType(selectedOrder);
  };

  const orderOptions = [
    { value: '', label: '-' },
    { value: 'Id Ascendente', label: 'Id Ascendente' },
    { value: 'Id Descendente', label: 'Id Descendente' },
    { value: 'Activo', label: 'Activado' },
    { value: 'Desactivado', label: 'Desactivado' }
  ];

  return (
    <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
      <FormControl variant="standard" sx={{ m: 1, minWidth: 120 }}>
        <InputLabel id="demo-simple-select-standard-label" className='font-size'>Ordenar Por</InputLabel>
        <Select
          labelId="demo-simple-select-standard-label"
          value={order}
          onChange={handleChange}
          label="Ordenar Por"
          className='font-size'
        >
          {orderOptions.map((option) => (
            <MenuItem key={option.value} value={option.value} className='font-size'>
              {option.label}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </Box>
  );
}

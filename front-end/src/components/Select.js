import React from 'react';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import './SelectOrder.css';

export default function SelectOrder(props) {
  const [order, setOrder] = React.useState('');

  const handleChange = (event) => {
    setOrder(event.target.value);
    props.setOrderType(event.target.value);
  };

  return (
    <Box sx={{ minWidth: 120 }}>
      <FormControl variant="standard" sx={{ m: 1, minWidth: 120 }}>
        <InputLabel id="demo-simple-select-standard-label">Ordenar Por</InputLabel>
        <Select
          labelId="demo-simple-select-standard-label"
          id="demo-simple-select-standard"
          value={order}
          onChange={handleChange}
          label="Ordenar Por"
        >
          <MenuItem value="">
            <em>-</em>
          </MenuItem>
          <MenuItem value={'Id Ascendente'}>Id Ascendente</MenuItem>
          <MenuItem value={'Id Descendente'}>Id Descendente</MenuItem>
          <MenuItem value={'Activo'}>Activado</MenuItem>
          <MenuItem value={'Desactivado'}>Desactivado</MenuItem>
        </Select>
      </FormControl>
    </Box>
  );
}

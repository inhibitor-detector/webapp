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
    setOrder(event.target.value);
    props.setOrderType(event.target.value);
  };

  return (
    <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
      <FormControl variant="standard" sx={{ m: 1, minWidth: 120 }}>
        <InputLabel id="demo-simple-select-standard-label" className='font-size'>Ordenar Por</InputLabel>
        <Select
          labelId="demo-simple-select-standard-label"
          id="demo-simple-select-standard"
          value={order}
          onChange={handleChange}
          label="Ordenar Por"
          className='font-size'
        >
          <MenuItem value="">
            <em>-</em>
          </MenuItem>
          <MenuItem value={'Id Ascendente'} className='font-size'>Id Ascendente</MenuItem>
          <MenuItem value={'Id Descendente'} className='font-size'>Id Descendente</MenuItem>
          <MenuItem value={'Activo'} className='font-size'>Activado</MenuItem>
          <MenuItem value={'Desactivado'} className='font-size'>Desactivado</MenuItem>
        </Select>
      </FormControl>
    </Box>
  );
}

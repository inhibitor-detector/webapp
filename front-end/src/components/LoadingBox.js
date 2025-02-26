import { Box, CircularProgress } from '@mui/material';

const LoadingBox = () => (
  <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexGrow: 1 }}>
    <CircularProgress sx={{ color: '#8bc34a' }} />
  </Box>
);

export default LoadingBox;

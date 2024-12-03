import React from 'react';
import { Box, Typography, Grid, Paper } from "@mui/material";
import DoneIcon from "@mui/icons-material/Done";
import BlockIcon from "@mui/icons-material/Block";
import DevicesIcon from "@mui/icons-material/Devices";

const DashboardStats = ({ activeCount, inactiveCount, totalCount }) => {
  const stats = [
    {
      label: "Total Activos",
      value: activeCount,
      icon: <DoneIcon />,
      backgroundColor: "#66BB6A",
    },
    {
      label: "Total Inactivos",
      value: inactiveCount,
      icon: <BlockIcon />,
      backgroundColor: "#EF5350",
    },
    {
      label: "Total Detectores",
      value: totalCount,
      icon: <DevicesIcon />,
      backgroundColor: "#42A5F5",
    },
  ];

  return (
    <Box margin={1}>
      <Grid container spacing={0}>
        {stats.map((stat, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <Paper
              elevation={0}
              sx={{
                display: "flex",
                alignItems: "center",
                padding: 2,
                border: "1px solid #e0e0e0",
              }}
            >
              <Box
                sx={{
                  marginRight: 2,
                  width: 40,
                  height: 40,
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  borderRadius: "50%",
                  backgroundColor: stat.backgroundColor,
                  color: "#FFFFFF",
                }}
              >
                {stat.icon}
              </Box>
              <Box>
                <Typography variant="h6" sx={{ margin: 0 }}>
                  {stat.value}
                </Typography>
                <Typography color="textSecondary">
                  {stat.label}
                </Typography>
              </Box>
            </Paper>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default DashboardStats;

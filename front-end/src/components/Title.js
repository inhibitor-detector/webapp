import React from "react";
import { Typography } from "@mui/material";

const Title = ({ title }) => {
  return (
    <div style={{ textAlign: "center", marginTop: "30px", marginBottom: "30px" }}>
      <Typography variant="h4" style={{ fontWeight: "bold" }}>
        { title }
      </Typography>
    </div>
  );
};

export default Title;
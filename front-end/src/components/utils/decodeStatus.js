import React from "react";
import {Typography} from "@mui/material";

export const decodeStatus = (status) => {
  const ERROR_FLAGS = {
    FIRST_HEARTBEAT: 64,
    MEMORY_FAILED: 32,
    YARD_FAILED: 16,
    ANALYZER_FAILED: 8,
    RFCAT_FAILED: 4,
    FAILED: 2,
    ACTIVE: 1,
  };

  const errors = [];

  if (status & ERROR_FLAGS.FIRST_HEARTBEAT) errors.push("Primer Heartbeat");
  if (status & ERROR_FLAGS.MEMORY_FAILED) errors.push("Fallo de memoria");
  if (status & ERROR_FLAGS.YARD_FAILED) errors.push("Fallo de Yard");
  if (status & ERROR_FLAGS.ANALYZER_FAILED) errors.push("Fallo del analyzer");
  if (status & ERROR_FLAGS.RFCAT_FAILED) errors.push("Fallo de RFCAT");
  if (status & ERROR_FLAGS.FAILED) errors.push("Falla general");
  if (errors.length === 0) {
    if (status & ERROR_FLAGS.ACTIVE) errors.push("OK");
  }

  if (errors.length > 0) {
    return errors.map((error, index) => (
      <Typography key={index} variant="body2">
        {error}
      </Typography>
    ));
  }

  return "-";
};

export default decodeStatus;

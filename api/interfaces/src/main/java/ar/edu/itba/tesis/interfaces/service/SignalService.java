package ar.edu.itba.tesis.interfaces.service;

import ar.edu.itba.tesis.interfaces.CrudOperations;
import ar.edu.itba.tesis.models.Signal;

import java.time.LocalDateTime;
import java.util.List;

public interface SignalService extends CrudOperations<Signal, Long> {
    List<Signal> findAllPaginated(Integer page, Integer pageSize, Long ownerId, Long detectorId, Boolean isHeartBeat, Boolean status);

    List<Signal> findByTime(LocalDateTime startTime, LocalDateTime endTime, Long ownerI);
}

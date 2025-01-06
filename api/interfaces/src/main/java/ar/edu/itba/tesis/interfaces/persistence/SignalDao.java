package ar.edu.itba.tesis.interfaces.persistence;

import ar.edu.itba.tesis.interfaces.CrudOperations;
import ar.edu.itba.tesis.models.Signal;

import java.time.LocalDateTime;
import java.util.List;

public interface SignalDao extends CrudOperations<Signal, Long> {
    List<Signal> findAllPaginated(Integer page, Integer pageSize, Long ownerId, Long detectorId, Boolean isHeartbeat);

    List<Signal> findByTime(LocalDateTime startTime, LocalDateTime endTime, Long ownerId);
}

package ar.edu.itba.tesis.interfaces.service;

import java.time.LocalDateTime;
import java.util.List;

import ar.edu.itba.tesis.interfaces.CrudOperations;
import ar.edu.itba.tesis.models.Detector;

public interface DetectorService extends CrudOperations<Detector, Long> {
    List<Detector> findAllPaginated(Integer page, Integer pageSize, Long ownerId);
    void updateLastHeartbeat(Long id, LocalDateTime lastHeartbeat);
}

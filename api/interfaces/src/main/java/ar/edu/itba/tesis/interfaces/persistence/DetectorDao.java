package ar.edu.itba.tesis.interfaces.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.tesis.interfaces.CrudOperations;
import ar.edu.itba.tesis.models.Detector;

public interface DetectorDao extends CrudOperations<Detector, Long> {
    List<Detector> findByOwnerIdPaginated(Integer page, Integer pageSize, Long ownerId);
    void updateLastHeartbeat(Long id, LocalDateTime lastHeartbeat);
    void updateStatus(Long id, Integer status);
    Optional<Detector> findByUserId(Long userId);
}

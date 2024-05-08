package ar.edu.itba.tesis.interfaces.persistence;

import ar.edu.itba.tesis.interfaces.CrudOperations;
import ar.edu.itba.tesis.models.Detector;

import java.util.List;

public interface DetectorDao extends CrudOperations<Detector, Long> {
    List<Detector> findByOwnerIdPaginated(Integer page, Integer pageSize, Long ownerId);
}

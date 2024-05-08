package ar.edu.itba.tesis.interfaces.service;

import ar.edu.itba.tesis.interfaces.CrudOperations;
import ar.edu.itba.tesis.models.Detector;

import java.util.List;

public interface DetectorService extends CrudOperations<Detector, Long> {
    List<Detector> findAllPaginated(Integer page, Integer pageSize, Long ownerId);
}

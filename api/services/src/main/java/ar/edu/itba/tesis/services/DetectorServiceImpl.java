package ar.edu.itba.tesis.services;

import ar.edu.itba.tesis.interfaces.persistence.DetectorDao;
import ar.edu.itba.tesis.interfaces.service.DetectorService;
import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.models.Detector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DetectorServiceImpl implements DetectorService {

    private final DetectorDao detectorDao;

    @Autowired
    public DetectorServiceImpl(DetectorDao detectorDao) {
        this.detectorDao = detectorDao;
    }

    @Transactional
    @Override
    public Detector create(Detector entity) throws AlreadyExistsException {
        return detectorDao.create(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Detector> findById(Long id) {
        return detectorDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Detector> findAll() {
        return detectorDao.findAll();
    }

    @Transactional
    @Override
    public Detector update(Long id, Detector entity) throws NotFoundException, AlreadyExistsException {
        return detectorDao.update(id, entity);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        detectorDao.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(Detector entity) {
        detectorDao.delete(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return detectorDao.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return detectorDao.count();
    }
}

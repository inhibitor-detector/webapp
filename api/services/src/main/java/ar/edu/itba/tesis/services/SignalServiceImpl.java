package ar.edu.itba.tesis.services;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.persistence.SignalDao;
import ar.edu.itba.tesis.interfaces.service.SignalService;
import ar.edu.itba.tesis.models.Signal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SignalServiceImpl implements SignalService {

    private final SignalDao signalDao;

    @Autowired
    public SignalServiceImpl(SignalDao heartbeatDao) {
        this.signalDao = heartbeatDao;
    }

    @Transactional
    @Override
    public Signal create(Signal entity) throws AlreadyExistsException {
        return signalDao.create(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Signal> findById(Long id) {
        return signalDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Signal> findAll() {
        return signalDao.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Signal> findAllPaginated(Integer page, Integer pageSize) {
        return signalDao.findAllPaginated(page, pageSize);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Signal> findAllPaginated(Integer page, Integer pageSize, Long ownerId, Long detectorId, Boolean isHeartbeat) {
        // TODO: Validate that ownerId belongs to a owner with ROLE USER/ADMIN
        // TODO: Validate that detector with detectorId exists
        return signalDao.findAllPaginated(page, pageSize, ownerId, detectorId, isHeartbeat);
    }

    @Transactional
    @Override
    public Signal update(Long id, Signal entity) throws NotFoundException, AlreadyExistsException {
        return signalDao.update(id, entity);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        signalDao.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(Signal entity) {
        signalDao.delete(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return signalDao.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return signalDao.count();
    }

    @Transactional(readOnly = true)
    public List<Signal> findByTime(LocalDateTime startTime, LocalDateTime endTime, Long ownerId) {
        return signalDao.findByTime(startTime, endTime, ownerId);
    }
}

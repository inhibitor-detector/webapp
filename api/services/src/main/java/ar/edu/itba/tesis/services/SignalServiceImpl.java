package ar.edu.itba.tesis.services;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.persistence.SignalDao;
import ar.edu.itba.tesis.interfaces.service.SignalService;
import ar.edu.itba.tesis.models.Signal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SignalServiceImpl implements SignalService {

    private final SignalDao heartbeatDao;

    @Autowired
    public SignalServiceImpl(SignalDao heartbeatDao) {
        this.heartbeatDao = heartbeatDao;
    }

    @Transactional
    @Override
    public Signal create(Signal entity) throws AlreadyExistsException {
        return heartbeatDao.create(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Signal> findById(Long id) {
        return heartbeatDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Signal> findAll() {
        return heartbeatDao.findAll();
    }

    @Transactional
    @Override
    public Signal update(Long id, Signal entity) throws NotFoundException, AlreadyExistsException {
        return heartbeatDao.update(id, entity);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        heartbeatDao.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(Signal entity) {
        heartbeatDao.delete(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return heartbeatDao.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return heartbeatDao.count();
    }
}

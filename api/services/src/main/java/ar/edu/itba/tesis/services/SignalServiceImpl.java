package ar.edu.itba.tesis.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.persistence.SignalDao;
import ar.edu.itba.tesis.interfaces.service.DetectorService;
import ar.edu.itba.tesis.interfaces.service.EmailService;
import ar.edu.itba.tesis.interfaces.service.SignalService;
import ar.edu.itba.tesis.models.Signal;

@Service
public class SignalServiceImpl implements SignalService {

    private final SignalDao signalDao;
    private final EmailService emailService;
    private final DetectorService detectorService;

    @Autowired
    public SignalServiceImpl(SignalDao heartbeatDao, EmailService emailService, DetectorService detectorService) {
        this.signalDao = heartbeatDao;
        this.emailService = emailService;
        this.detectorService = detectorService;
    }

    @Transactional
    @Override
    public Signal create(Signal entity) throws AlreadyExistsException {
        if (entity.getAcknowledged() == null) {
            entity.setAcknowledged(false);
        }
        Long detector_id = entity.getDetector().getId();
        if (entity.getIsHeartbeat()) {
            detectorService.updateLastHeartbeat(detector_id, LocalDateTime.now());
            System.out.println("Signal status: " + entity.getStatus());
            detectorService.updateStatus(detector_id, entity.getStatus());
        } else {
            String ownerEmail = entity.getDetector().getOwner().getEmail();
            String detectorName = entity.getDetector().getName();
            String timestamp = entity.getTimestamp().toString();
            emailService.sendInhibitionDetectedEmail(ownerEmail, detectorName, timestamp);
        }
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
    public List<Signal> findAllPaginated(Integer page, Integer pageSize, Long ownerId, Long detectorId, Boolean isHeartbeat, Boolean acknowledged) {
        return signalDao.findAllPaginated(page, pageSize, ownerId, detectorId, isHeartbeat, acknowledged);
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
    @Override
    public List<Signal> findByTime(LocalDateTime startTime, LocalDateTime endTime, Long ownerId) {
        return signalDao.findByTime(startTime, endTime, ownerId);
    }
}

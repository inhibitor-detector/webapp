package ar.edu.itba.tesis.services;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.service.DetectorService;
import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.Signal;
import ar.edu.itba.tesis.persistence.SignalHibernateDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignalServiceImplTest {

    private final Long ID = 1L;
    private final Signal signal = getSignal();

    @InjectMocks
    private SignalServiceImpl signalService;

    @Mock
    private SignalHibernateDao signalDaoMock;
    @Mock private DetectorService detectorServiceMock;

    @Test
    public void testCreateSignal() throws AlreadyExistsException {
        when(signalDaoMock.create(any(Signal.class))).thenReturn(signal);

        Signal createdSignal = signalService.create(signal);

        assertNotNull(createdSignal);
        assertEquals(ID, createdSignal.getId());
        verify(signalDaoMock).create(signal);
    }

    @Test
    public void testFindSignalById() {
        when(signalDaoMock.findById(1L)).thenReturn(Optional.of(signal));

        Optional<Signal> foundSignal = signalService.findById(1L);

        assertTrue(foundSignal.isPresent());
        assertEquals(ID, foundSignal.get().getId());
        verify(signalDaoMock).findById(ID);
    }

    @Test
    void testFindSignalByIdNotFound() {
        when(signalDaoMock.findById(2L)).thenReturn(Optional.empty());

        Optional<Signal> foundSignal = signalDaoMock.findById(2L);

        assertTrue(foundSignal.isEmpty());
        verify(signalDaoMock).findById(2L);
    }

    @Test
    public void testFindAllSignals() {
        when(signalDaoMock.findAll()).thenReturn(List.of(signal));

        List<Signal> signals = signalService.findAll();

        assertFalse(signals.isEmpty());
        assertEquals(1, signals.size());
        verify(signalDaoMock).findAll();
    }

    @Test
    public void testFindAllSignalsPaginated() {
        when(signalDaoMock.findAllPaginated(1, 10)).thenReturn(List.of(signal));

        List<Signal> signals = signalService.findAllPaginated(1, 10);

        assertFalse(signals.isEmpty());
        assertEquals(1, signals.size());
        verify(signalDaoMock).findAllPaginated(1, 10);
    }

    @Test
    public void testFindAllSignalsPaginatedWithParams() {
        when(signalDaoMock.findAllPaginated(1, 10, ID, ID, true, true)).thenReturn(List.of(signal));

        List<Signal> signals = signalService.findAllPaginated(1, 10, ID, ID, true, true);

        assertFalse(signals.isEmpty());
        assertEquals(1, signals.size());
        verify(signalDaoMock).findAllPaginated(1, 10, ID, ID, true, true);
    }

    @Test
    public void testUpdateSignal() throws NotFoundException, AlreadyExistsException {
        when(signalDaoMock.update(eq(ID), any(Signal.class))).thenReturn(signal);

        Signal updatedSignal = signalService.update(ID, signal);

        assertNotNull(updatedSignal);
        assertEquals(ID, updatedSignal.getId());
        verify(signalDaoMock).update(ID, signal);
    }

    @Test
    public void testDeleteSignalById() {
        signalService.deleteById(ID);

        verify(signalDaoMock).deleteById(ID);
    }

    @Test
    public void testDeleteSignal() {
        signalService.delete(signal);

        verify(signalDaoMock).delete(signal);
    }

    @Test
    public void testSignalExists() {
        when(signalDaoMock.existsById(ID)).thenReturn(true);

        assertTrue(signalService.existsById(ID));
        verify(signalDaoMock).existsById(ID);
    }

    @Test
    public void testSignalCount() {
        when(signalDaoMock.count()).thenReturn(10L);

        Long count = signalService.count();

        assertEquals(10L, count);
        verify(signalDaoMock).count();
    }

    @Test
    public void testFindByTime() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        when(signalDaoMock.findByTime(startTime, endTime, ID)).thenReturn(List.of(signal));

        List<Signal> signals = signalService.findByTime(startTime, endTime, ID);

        assertFalse(signals.isEmpty());
        verify(signalDaoMock).findByTime(startTime, endTime, ID);
    }

    private Signal getSignal() {
        Detector detector = new Detector();
        detector.setId(ID);

        Signal signal = new Signal();
        signal.setId(ID);
        signal.setAcknowledged(false);
        signal.setIsHeartbeat(false);
        signal.setDetector(detector);
        return signal;
    }
}
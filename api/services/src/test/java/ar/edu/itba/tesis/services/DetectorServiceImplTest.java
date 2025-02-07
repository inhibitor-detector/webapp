package ar.edu.itba.tesis.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.persistence.DetectorDao;
import ar.edu.itba.tesis.models.Detector;

@ExtendWith(MockitoExtension.class)
class DetectorServiceImplTest {

    private final Long ID = 1L;
    private final Detector detector = getDetector();

    @InjectMocks
    private DetectorServiceImpl detectorService;

    @Mock
    private DetectorDao detectorDaoMock;

    @Test
    public void testCreateDetector() throws AlreadyExistsException {
        when(detectorDaoMock.create(detector)).thenReturn(detector);

        Detector result = detectorService.create(detector);

        assertNotNull(result);
        assertEquals(ID, result.getId());
        verify(detectorDaoMock).create(detector);
    }

    @Test
    public void testFindDetectorById() {
        when(detectorDaoMock.findById(ID)).thenReturn(Optional.of(detector));

        Optional<Detector> foundDetector = detectorService.findById(ID);

        assertTrue(foundDetector.isPresent());
        assertEquals(detector, foundDetector.get());
        verify(detectorDaoMock).findById(ID);
    }

    @Test
    void testFindDetectorByIdNotFound() {
        when(detectorDaoMock.findById(2L)).thenReturn(Optional.empty());

        Optional<Detector> foundDetector = detectorDaoMock.findById(2L);

        assertTrue(foundDetector.isEmpty());
        verify(detectorDaoMock).findById(2L);
    }

    @Test
    public void testFindAllDetectors() {
        when(detectorDaoMock.findAll()).thenReturn(List.of(detector));

        List<Detector> detectors = detectorService.findAll();

        assertFalse(detectors.isEmpty());
        assertEquals(1, detectors.size());
        verify(detectorDaoMock).findAll();
    }

    @Test
    public void testFindAllDetectorsPaginated() {
        when(detectorDaoMock.findAllPaginated(1, 10)).thenReturn(List.of(detector));

        List<Detector> detectors = detectorService.findAllPaginated(1, 10);

        assertFalse(detectors.isEmpty());
        assertEquals(1, detectors.size());
        verify(detectorDaoMock).findAllPaginated(1, 10);
    }

    @Test
    public void testFindAllDetectorsPaginatedByDefaultOwnerId() {
        when(detectorDaoMock.findAllPaginated(1, 10)).thenReturn(List.of(detector));

        List<Detector> detectors = detectorService.findAllPaginated(1, 10, 0L);

        assertFalse(detectors.isEmpty());
        assertEquals(1, detectors.size());
        verify(detectorDaoMock).findAllPaginated(1, 10);
    }

    @Test
    public void testFindAllDetectorsPaginatedByOwnerId() {
        when(detectorDaoMock.findByOwnerIdPaginated(1, 10, 1L)).thenReturn(List.of(detector));

        List<Detector> detectors = detectorService.findAllPaginated(1, 10, 1L);

        assertFalse(detectors.isEmpty());
        assertEquals(1, detectors.size());
        verify(detectorDaoMock).findByOwnerIdPaginated(1, 10, 1L);
    }

    @Test
    public void testUpdateDetector() throws NotFoundException, AlreadyExistsException {
        when(detectorDaoMock.update(ID, detector)).thenReturn(detector);

        Detector updatedDetector = detectorService.update(ID, detector);

        assertNotNull(updatedDetector);
        assertEquals(ID, updatedDetector.getId());
        verify(detectorDaoMock).update(ID, detector);
    }

    @Test
    public void testUpdateLastHeartbeat() {
        LocalDateTime lastHeartbeat = LocalDateTime.now();

        detectorService.updateLastHeartbeat(ID, lastHeartbeat);

        verify(detectorDaoMock).updateLastHeartbeat(ID, lastHeartbeat);
    }

    @Test
    public void testUpdateStatus() {
        Integer status = 1;

        detectorService.updateStatus(ID, status);

        verify(detectorDaoMock).updateStatus(ID, status);
    }

    @Test
    public void testDeleteDetectorById() {
        detectorService.deleteById(ID);

        verify(detectorDaoMock).deleteById(ID);
    }

    @Test
    public void testDeleteDetector() {
        detectorService.delete(detector);

        verify(detectorDaoMock).delete(detector);
    }

    @Test
    public void testDetectorExists() {
        when(detectorDaoMock.existsById(ID)).thenReturn(true);

        boolean exists = detectorService.existsById(ID);

        assertTrue(exists);
        verify(detectorDaoMock).existsById(ID);
    }

    @Test
    public void testDetectorCount() {
        when(detectorDaoMock.count()).thenReturn(10L);

        Long count = detectorService.count();

        assertEquals(10L, count);
        verify(detectorDaoMock).count();
    }

    private Detector getDetector() {
        Detector detector = new Detector();
        detector.setId(ID);
        return detector;
    }
}
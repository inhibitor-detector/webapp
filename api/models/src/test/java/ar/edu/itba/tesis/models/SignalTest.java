package ar.edu.itba.tesis.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SignalTest {

    @InjectMocks
    private final Signal signal = new Signal();

    @Mock
    private Detector detectorMock;

    @Test
    public void testSetDetector() {
        signal.setDetector(detectorMock);
        assertEquals(detectorMock, signal.getDetector());
    }

    @Test
    public void testSetTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        signal.setTimestamp(timestamp);
        assertEquals(timestamp, signal.getTimestamp());
    }

    @Test
    public void testSetIsHeartbeat() {
        signal.setIsHeartbeat(true);
        assertTrue(signal.getIsHeartbeat());

        signal.setIsHeartbeat(false);
        assertFalse(signal.getIsHeartbeat());
    }

    @Test
    public void testSetAcknowledged() {
        signal.setAcknowledged(true);
        assertTrue(signal.getAcknowledged());

        signal.setAcknowledged(false);
        assertFalse(signal.getAcknowledged());
    }
}

package ar.edu.itba.tesis.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class DetectorTest {

    @InjectMocks
    private final Detector detector = new Detector();

    @Mock
    private User userMock;

    @Test
    public void testSetOwner() {
        detector.setOwner(userMock);
        assertEquals(userMock, detector.getOwner());
    }

    @Test
    public void testSetUser() {
        detector.setUser(userMock);
        assertEquals(userMock, detector.getUser());
    }

    // @Test
    // public void testSetIsOnline() {
    //     detector.setIsOnline(true);
    //     assertTrue(detector.getIsOnline());

    //     detector.setIsOnline(false);
    //     assertFalse(detector.getIsOnline());
    // }
    @Test
    public void testSetLastHeartbeat() {
        LocalDateTime lastHeartbeat = LocalDateTime.now();
        detector.setLastHeartbeat(lastHeartbeat);
        assertEquals(lastHeartbeat, detector.getLastHeartbeat());
    }

    @Test
    public void testSetVersion() {
        String version = "Detector version";
        detector.setVersion(version);
        assertEquals(version, detector.getVersion());
    }

    @Test
    public void testSetName() {
        String name = "Detector Test";
        detector.setName(name);
        assertEquals(name, detector.getName());
    }

    @Test
    public void testSetDescription() {
        String description = "Detector Desc";
        detector.setDescription(description);
        assertEquals(description, detector.getDescription());
    }
}

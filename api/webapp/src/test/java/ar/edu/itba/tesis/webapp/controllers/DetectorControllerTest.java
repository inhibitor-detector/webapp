package ar.edu.itba.tesis.webapp.controllers;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.DetectorNotFoundException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.service.DetectorService;
import ar.edu.itba.tesis.interfaces.service.UserService;
import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.User;
import ar.edu.itba.tesis.webapp.dtos.DetectorDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DetectorControllerTest {

    private final Long ID = 1L;
    private final Detector detector = getDetector();
    private final DetectorDto detectorDto = getDetectorDto();

    @InjectMocks
    private DetectorController detectorController;

    @Mock
    private DetectorService detectorServiceMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private UriInfo uriInfoMock;
    @Mock
    private UriBuilder uriBuilderMock;

    @Test
    public void testGetDetectors() {
        when(detectorServiceMock.findAllPaginated(1, 10, 0L)).thenReturn(List.of(detector));

        Response response = detectorController.getDetectors(1, 10, 0L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(List.of(detectorDto), response.getEntity());
    }

    @Test
    public void testGetDetectorsEmptyResult() {
        when(detectorServiceMock.findAllPaginated(1, 10, 0L)).thenReturn(Collections.emptyList());

        Response response = detectorController.getDetectors(1, 10, 0L);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testSaveDetector() throws NotFoundException, AlreadyExistsException {
        setUpUriMocks();
        when(userServiceMock.findById(detector.getOwner().getId())).thenReturn(Optional.of(getUser()), Optional.of(getUser()));
        when(detectorServiceMock.create(any(Detector.class))).thenReturn(detector);

        Response response = detectorController.saveDetector(detectorDto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(detectorDto, response.getEntity());
    }

    @Test
    public void testSaveDetectorNotFound() {
        assertThrows(DetectorNotFoundException.class, () -> detectorController.saveDetector(detectorDto));
    }

    @Test
    public void testGetDetector() throws NotFoundException {
        when(detectorServiceMock.findById(ID)).thenReturn(Optional.of(detector));

        Response response = detectorController.getDetector(ID);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(detectorDto, response.getEntity());
    }

    @Test
    void testGetDetectorNotFound() {
        when(detectorServiceMock.findById(ID)).thenReturn(Optional.empty());

        assertThrows(DetectorNotFoundException.class, () -> detectorController.getDetector(ID));
    }

    @Test
    void testDeleteDetector() {
        Response response = detectorController.deleteDetector(ID);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(detectorServiceMock).deleteById(ID);
    }

    private Detector getDetector() {
        Detector detector = new Detector();
        detector.setId(ID);
        detector.setOwner(getUser());
        detector.setUser(getUser());
        return detector;
    }

    private DetectorDto getDetectorDto() {
        return DetectorDto.fromDetector(detector);
    }

    private User getUser() {
        User user = new User();
        user.setId(0L);
        return user;
    }

    private void setUpUriMocks() {
        ReflectionTestUtils.setField(detectorController, "uriInfo", uriInfoMock);
        when(uriInfoMock.getAbsolutePathBuilder()).thenReturn(uriBuilderMock);
        when(uriBuilderMock.path(anyString())).thenReturn(uriBuilderMock);
    }
}
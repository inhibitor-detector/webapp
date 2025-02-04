package ar.edu.itba.tesis.webapp.controllers;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.service.DetectorService;
import ar.edu.itba.tesis.interfaces.service.SignalService;
import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.Signal;
import ar.edu.itba.tesis.webapp.auth.AccessControl;
import ar.edu.itba.tesis.webapp.dtos.SignalDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignalControllerTest {

    private final Long ID = 1L;
    private final Signal signal = getSignal();
    private final SignalDto signalDto = getSignalDto();
    private final Detector detector = getDetector();

    @InjectMocks
    private SignalController signalController;

    @Mock
    private DetectorService detectorServiceMock;
    @Mock
    private SignalService signalServiceMock;
    @Mock
    private SecurityContext securityContextMock;
    @Mock
    private AccessControl accessControlMock;
    @Mock
    private Authentication authenticationMock;
    @Mock
    private UriInfo uriInfoMock;
    @Mock
    private UriBuilder uriBuilderMock;

    @Test
    public void testGetSignals() {
        when(signalServiceMock.findAllPaginated(1, 10, 0L, ID, false, false)).thenReturn(List.of(signal));

        Response response = signalController.getSignals(1, 10, 0L, ID, false, false);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(List.of(signalDto), response.getEntity());
    }

    @Test
    public void testGetSignalsEmptyResult() {
        when(signalServiceMock.findAllPaginated(1, 10, 0L, ID, false, false)).thenReturn(Collections.emptyList());

        Response response = signalController.getSignals(1, 10, 0L, ID, false, false);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testSaveSignal() throws NotFoundException, AlreadyExistsException {
        setUpUriMocks();
        setUpSecurityMocksToSave(true);
        when(detectorServiceMock.findById(ID)).thenReturn(Optional.of(detector));
        when(signalServiceMock.create(any(Signal.class))).thenReturn(signal);

        Response response = signalController.saveSignal(signalDto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(signalDto, response.getEntity());
    }

    @Test
    public void testSaveSignalNotFound() {
        assertThrows(NotFoundException.class, () -> signalController.saveSignal(signalDto));
    }

    @Test
    void testSaveSignalAccessDenied() {
        setUpSecurityMocksToSave(false);
        when(detectorServiceMock.findById(ID)).thenReturn(Optional.of(detector));

        assertThrows(AccessDeniedException.class, () -> signalController.saveSignal(signalDto));
    }

    @Test
    void testUpdateSignal() throws NotFoundException, AlreadyExistsException {
        setUpSecurityMocksToUpdate(true);
        when(detectorServiceMock.findById(ID)).thenReturn(Optional.of(detector));
        when(signalServiceMock.update(eq(ID), any(Signal.class))).thenReturn(signal);

        Response response = signalController.updateSignal(ID, signalDto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(signalServiceMock).update(eq(ID), any(Signal.class));
    }

    @Test
    void testUpdateSignalAccessDenied()  {
        setUpSecurityMocksToUpdate(false);
        when(detectorServiceMock.findById(ID)).thenReturn(Optional.of(detector));

        assertThrows(AccessDeniedException.class, () -> signalController.updateSignal(ID, signalDto));
    }

    @Test
    public void testGetSignal() throws NotFoundException {
        when(signalServiceMock.findById(ID)).thenReturn(Optional.of(signal));

        Response response = signalController.getSignal(ID);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(signalDto, response.getEntity());
    }

    @Test
    void testGetSignalNotFound() {
        when(signalServiceMock.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> signalController.getSignal(ID));
    }

    @Test
    void testDeleteSignal() {
        Response response = signalController.deleteSignal(ID);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(signalServiceMock).deleteById(ID);
    }

    @Test
    void testGetSignalsByTime() {
        String startTime = "2025-01-01T00:00:00";
        String endTime = "2025-01-02T00:00:00";

        List<Signal> signals = List.of(signal);
        when(signalServiceMock.findByTime(any(LocalDateTime.class), any(LocalDateTime.class), eq(ID)))
                .thenReturn(signals);

        Response response = signalController.getSignalsByTime(startTime, endTime, ID);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(signalServiceMock).findByTime(any(LocalDateTime.class), any(LocalDateTime.class), eq(ID));
    }

    private Signal getSignal() {
        Signal signal = new Signal();
        signal.setId(ID);
        signal.setTimestamp(LocalDateTime.now());
        signal.setDetector(getDetector());
        return signal;
    }

    private SignalDto getSignalDto() {
        return SignalDto.fromSignal(signal);
    }

    private Detector getDetector() {
        Detector detector = new Detector();
        detector.setId(ID);
        return detector;
    }

    private void setUpUriMocks() {
        ReflectionTestUtils.setField(signalController, "uriInfo", uriInfoMock);
        when(uriInfoMock.getAbsolutePathBuilder()).thenReturn(uriBuilderMock);
        when(uriBuilderMock.path(anyString())).thenReturn(uriBuilderMock);
    }

    private void setUpSecurityMocksToSave(Boolean canBeAuthenticated) {
        setUpSecurityMocks();
        when(accessControlMock.canPostSignalCheckDetectorId(authenticationMock, detector)).thenReturn(canBeAuthenticated);
    }

    private void setUpSecurityMocksToUpdate(Boolean canBeAuthenticated) {
        setUpSecurityMocks();
        when(accessControlMock.canPutSignal(authenticationMock)).thenReturn(canBeAuthenticated);
    }

    private void setUpSecurityMocks() {
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }
}
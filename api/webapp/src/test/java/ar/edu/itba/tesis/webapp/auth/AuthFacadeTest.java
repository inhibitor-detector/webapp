package ar.edu.itba.tesis.webapp.auth;

import ar.edu.itba.tesis.interfaces.service.DetectorService;
import ar.edu.itba.tesis.interfaces.service.UserService;
import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthFacadeTest {

    private final static String USERNAME = "username";
    private static final Long ID = 1L;

    @InjectMocks
    private AuthFacade authFacade;

    @Mock
    private UserService userServiceMock;
    @Mock
    private DetectorService detectorServiceMock;
    @Mock
    private Authentication authenticationMock;
    @Mock
    private UserDetails userDetailsMock;
    @Mock
    private User user;
    @Mock
    private Detector detector;

    @Test
    public void testGetAuthenticatedUser() {
        setUpAuthMocks();
        when(userServiceMock.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        User authenticatedUser = authFacade.getAuthenticatedUser(authenticationMock);

        assertNotNull(authenticatedUser);
        assertEquals(user, authenticatedUser);
        verify(userServiceMock).findByUsername(USERNAME);
        verify(authenticationMock, times(2)).getPrincipal();
    }

    @Test
    public void testGetAuthenticatedUserNotFound() {
        setUpAuthMocks();
        when(userServiceMock.findByUsername(USERNAME)).thenReturn(Optional.empty());

        User authenticatedUser = authFacade.getAuthenticatedUser(authenticationMock);

        assertNull(authenticatedUser);
        verify(userServiceMock).findByUsername(USERNAME);
        verify(authenticationMock, times(2)).getPrincipal();
    }

    @Test
    public void testGetAuthenticatedUserInvalidPrincipal() {
        when(authenticationMock.getPrincipal()).thenReturn("invalidPrincipal");

        User authenticatedUser = authFacade.getAuthenticatedUser(authenticationMock);

        assertNull(authenticatedUser);
        verifyNoInteractions(userServiceMock);
        verify(authenticationMock).getPrincipal();
    }

    @Test
    public void testGetAuthenticatedDetector() {
        setUpAuthMocks();
        when(userServiceMock.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(user.getId()).thenReturn(ID);
        when(detectorServiceMock.findByUserId(ID)).thenReturn(Optional.of(detector));

        Detector authenticatedDetector = authFacade.getAuthenticatedDetector(authenticationMock);

        assertNotNull(authenticatedDetector);
        assertEquals(detector, authenticatedDetector);
        verify(authenticationMock, times(2)).getPrincipal();
        verify(detectorServiceMock).findByUserId(ID);
    }

    @Test
    public void testGetAuthenticatedDetectorUserNotFound() {
        setUpAuthMocks();
        when(userServiceMock.findByUsername(USERNAME)).thenReturn(Optional.empty());

        Detector authenticatedDetector = authFacade.getAuthenticatedDetector(authenticationMock);

        assertNull(authenticatedDetector);
        verify(userServiceMock).findByUsername(USERNAME);
        verify(authenticationMock, times(2)).getPrincipal();
        verifyNoInteractions(detectorServiceMock);
    }

    @Test
    public void testGetAuthenticatedDetectorDetectorNotFound() {
        setUpAuthMocks();
        when(userServiceMock.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(user.getId()).thenReturn(ID);
        when(detectorServiceMock.findByUserId(ID)).thenReturn(Optional.empty());

        Detector result = authFacade.getAuthenticatedDetector(authenticationMock);

        assertNull(result);
        verify(userServiceMock).findByUsername(USERNAME);
        verify(authenticationMock, times(2)).getPrincipal();
        verify(detectorServiceMock).findByUserId(ID);
    }

    private void setUpAuthMocks() {
        when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn(USERNAME);
    }
}
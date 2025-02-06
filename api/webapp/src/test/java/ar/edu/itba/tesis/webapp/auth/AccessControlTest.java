package ar.edu.itba.tesis.webapp.auth;

import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.Role;
import ar.edu.itba.tesis.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessControlTest {

    private final Long ID = 1L;
    private final User user = getUser();
    private final Detector detector = getDetector();

    @InjectMocks
    private AccessControl accessControl;

    @Mock
    private AuthFacade authFacadeMock;
    @Mock
    private Authentication authentication;

    @Test
    public void testIsAuthenticatedUserById() {
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        assertTrue(accessControl.isAuthenticatedUser(authentication, ID));
    }

    @Test
    void testIsAuthenticatedUserByRole() {
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        assertTrue(accessControl.isAuthenticatedUser(authentication, 2L));
    }

    @Test
    void testNotAuthenticatedUser() {
        user.setRoles(Collections.emptySet());
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        assertFalse(accessControl.isAuthenticatedUser(authentication, 2L));
    }

    @Test
    void testCanPostSignal() {
        user.setRoles(Collections.singleton(Role.DETECTOR));
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        assertTrue(accessControl.canPostSignal(authentication));
    }

    @Test
    void testCantPostSignalWithoutAdminRole() {
        user.setRoles(Collections.emptySet());
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        assertFalse(accessControl.canPostSignal(authentication));
    }

    @Test
    void testCantPostSignalWithNullUser() {
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(null);

        assertFalse(accessControl.canPostSignal(authentication));
    }

    @Test
    void testCanPutSignal() {
        user.setRoles(Collections.singleton(Role.DETECTOR));
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        assertTrue(accessControl.canPutSignal(authentication));
    }

    @Test
    void testCantPutSignal() {
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(null);

        assertFalse(accessControl.canPutSignal(authentication));
    }

    @Test
    void testCanPostSignalCheckDetectorId() {
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        assertTrue(accessControl.canPostSignalCheckDetectorId(authentication, detector));
    }

    @Test
    void testCantPostSignalCheckDetectorIdWithDifferentUserId() {
        user.setId(2L);
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        assertFalse(accessControl.canPostSignalCheckDetectorId(authentication, detector));
    }

    @Test
    void testCantPostSignalCheckDetectorIdWithNullUser() {
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(null);

        assertFalse(accessControl.canPostSignalCheckDetectorId(authentication, detector));
    }

    @Test
    void testIsAdminUser() {
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        assertTrue(accessControl.isAdminUser(authentication));
    }

    @Test
    void testIsNotAdminUserWithoutAdminRole() {
        user.setRoles(Collections.emptySet());
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        assertFalse(accessControl.isAdminUser(authentication));
    }

    @Test
    void testIsNotAdminUserWithNullUser() {
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(null);

        assertFalse(accessControl.isAdminUser(authentication));
    }

    @Test
    void testCanAccessDetectorsByAdmin() {
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        assertTrue(accessControl.canAccessDetectors(authentication, null));
    }

    @Test
    void testCanAccessDetectorsByOwnerId() {
        user.setRoles(Collections.emptySet());
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        String validQuery = "ownerId=1";
        assertTrue(accessControl.canAccessDetectors(authentication, validQuery));
    }
    @Test
    void testCantAccessDetectorsWithInvalidOwnerId() {
        user.setRoles(Collections.emptySet());
        when(authFacadeMock.getAuthenticatedUser(authentication)).thenReturn(user);

        String invalidQuery = "ownerId=notANumber";
        assertThrows(AccessDeniedException.class, () -> accessControl.canAccessDetectors(authentication, invalidQuery));
    }

    private Detector getDetector() {
        User user = getUser();

        Detector detector = new Detector();
        detector.setId(ID);
        detector.setUser(user);
        return detector;
    }

    private User getUser() {
        User user = new User();
        user.setId(ID);
        user.setRoles(Collections.singleton(Role.ADMIN));
        return user;
    }
}
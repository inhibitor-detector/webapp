package ar.edu.itba.tesis.webapp.auth;

import ar.edu.itba.tesis.interfaces.service.UserService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthFacadeTest {

    @InjectMocks
    private AuthFacade authFacade;

    @Mock
    private UserService userServiceMock;
    @Mock
    private Authentication authenticationMock;
    @Mock
    private UserDetails userDetailsMock;
    @Mock
    private User user;

    @Test
    void testGetAuthenticatedUser() {
        when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("testUser");
        when(userServiceMock.findByUsername("testUser")).thenReturn(Optional.of(user));

        User result = authFacade.getAuthenticatedUser(authenticationMock);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testGetAuthenticatedUserNotFound() {
        when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("testUser");
        when(userServiceMock.findByUsername("testUser")).thenReturn(Optional.empty());

        User result = authFacade.getAuthenticatedUser(authenticationMock);

        assertNull(result);
    }

    @Test
    void testGetAuthenticatedUserInvalidPrincipal() {
        when(authenticationMock.getPrincipal()).thenReturn("invalidPrincipal");

        User result = authFacade.getAuthenticatedUser(authenticationMock);

        assertNull(result);
    }
}
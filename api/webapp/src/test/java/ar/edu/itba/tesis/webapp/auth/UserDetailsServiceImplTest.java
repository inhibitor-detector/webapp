package ar.edu.itba.tesis.webapp.auth;

import ar.edu.itba.tesis.interfaces.service.UserService;
import ar.edu.itba.tesis.models.Role;
import ar.edu.itba.tesis.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    private final String USERNAME = "username";
    private final String PASSWORD = "testPassword";
    private final User user = getUser();

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserService userServiceMock;

    @Test
    public void testLoadUserByUsername() {
        when(userServiceMock.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);

        assertNotNull(userDetails);
        assertEquals(USERNAME, userDetails.getUsername());
        assertEquals(PASSWORD, userDetails.getPassword());
        assertEquals(Role.USER.getRole(), userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userServiceMock.findByUsername("userNotExists")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> userDetailsService.loadUserByUsername("userNotExists"));
    }

    private User getUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(Role.USER));
        return user;
    }
}
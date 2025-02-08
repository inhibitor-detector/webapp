package ar.edu.itba.tesis.webapp.auth;

import ar.edu.itba.tesis.webapp.auth.exceptions.InvalidAuthenticationTokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtToUserDetailsConverterTest {

    private final String USERNAME = "username";

    @InjectMocks
    private JwtToUserDetailsConverter converter;

    @Mock
    private UserDetailsService userDetailsServiceMock;
    @Mock
    private UserDetailsChecker userDetailsCheckerMock;
    @Mock
    private UserDetails userDetailsMock;
    @Mock
    private Jwt jwtMock;

    @Test
    public void testConvert() throws NoSuchFieldException, IllegalAccessException {
        when(jwtMock.getExpiresAt()).thenReturn(Instant.now().plusSeconds(10));
        when(jwtMock.getIssuedAt()).thenReturn(Instant.now());
        when(jwtMock.getSubject()).thenReturn(USERNAME);
        when(userDetailsServiceMock.loadUserByUsername(USERNAME)).thenReturn(userDetailsMock);
        when(userDetailsMock.getAuthorities()).thenReturn(Collections.emptyList());

        Field field = JwtToUserDetailsConverter.class.getDeclaredField("detailsChecker");
        field.setAccessible(true);
        field.set(converter, userDetailsCheckerMock);

        UsernamePasswordAuthenticationToken authenticationToken = converter.convert(jwtMock);

        assertNotNull(authenticationToken);
        assertEquals(userDetailsMock, authenticationToken.getPrincipal());
        assertTrue(authenticationToken.getAuthorities().isEmpty());
        assertEquals("", authenticationToken.getCredentials());
    }

    @Test
    public void testConvertThrowsExceptionNoExpirationDate() {
        when(jwtMock.getExpiresAt()).thenReturn(null);

        assertThrows(InvalidAuthenticationTokenException.class, () -> converter.convert(jwtMock));
    }

    @Test
    public void convert_ShouldThrowException_WhenJwtHasNoIssueDate() {
        when(jwtMock.getExpiresAt()).thenReturn(Instant.now());
        when(jwtMock.getIssuedAt()).thenReturn(null);

        assertThrows(InvalidAuthenticationTokenException.class, () -> converter.convert(jwtMock));
    }
}

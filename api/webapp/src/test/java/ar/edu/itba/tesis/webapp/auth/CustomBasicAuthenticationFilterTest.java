package ar.edu.itba.tesis.webapp.auth;


import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomBasicAuthenticationFilterTest {

    private final Long ID = 1L;
    private final User user = getUser();
    private final Detector detector = getDetector();

    @InjectMocks
    private CustomBasicAuthenticationFilter customBasicAuthenticationFilter;

    @Mock
    private AuthenticationManager authenticationManagerMock;
    @Mock
    private AuthenticationEntryPoint authenticationEntryPointMock;
    @Mock
    private JwtEncoder jwtEncoderMock;
    @Mock
    private AuthFacade authFacadeMock;
    @Mock
    private Authentication authenticationMock;
    @Mock
    private Jwt jwtMock;
    @Mock
    private HttpServletRequest requestMock;
    @Mock
    private HttpServletResponse responseMock;

    @Test
    public void testOnSuccessfulAuthentication() throws IOException {
        when(authFacadeMock.getAuthenticatedUser(authenticationMock)).thenReturn(user);
        when(authFacadeMock.getAuthenticatedDetector(authenticationMock)).thenReturn(detector);
        when(authenticationMock.getName()).thenReturn("username");
        when(jwtEncoderMock.encode(any(JwtEncoderParameters.class))).thenReturn(jwtMock);
        when(jwtMock.getTokenValue()).thenReturn("token");

        customBasicAuthenticationFilter.onSuccessfulAuthentication(requestMock, responseMock, authenticationMock);

        verify(jwtEncoderMock).encode(any(JwtEncoderParameters.class));
        verify(responseMock).addHeader("Authorization","Bearer token");
    }

    private User getUser() {
        User user = new User();
        user.setId(ID);
        return user;
    }

    private Detector getDetector() {
        Detector detector = new Detector();
        detector.setId(ID);
        return detector;
    }
}
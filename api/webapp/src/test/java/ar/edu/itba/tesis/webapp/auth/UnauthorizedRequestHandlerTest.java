package ar.edu.itba.tesis.webapp.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnauthorizedRequestHandlerTest {

    private final static String EXCEPTION_MSG = "Unauthorized access";

    @InjectMocks
    private UnauthorizedRequestHandler handler;

    @Mock
    private HttpServletRequest requestMock;
    @Mock
    private HttpServletResponse responseMock;
    @Mock
    private PrintWriter writerMock;
    @Mock
    private AuthenticationException authException;

    @Test
    public void testCommence() throws IOException {
        when(authException.getMessage()).thenReturn(EXCEPTION_MSG);
        when(responseMock.getWriter()).thenReturn(writerMock);

        handler.commence(requestMock, responseMock, authException);

        verify(responseMock).addHeader("WWW-Authenticate", "Basic realm=realmName must be specified, Bearer realm=JWT Required");
        verify(responseMock).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(responseMock).setContentType(MediaType.APPLICATION_JSON);
        verify(writerMock).write("{\n \"message\": \""+ EXCEPTION_MSG + "\"\n}");
    }
}
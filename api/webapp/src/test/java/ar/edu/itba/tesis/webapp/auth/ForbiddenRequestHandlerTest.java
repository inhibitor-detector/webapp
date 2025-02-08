package ar.edu.itba.tesis.webapp.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForbiddenRequestHandlerTest {

    private final static String EXCEPTION_MSG = "Access denied";

    @InjectMocks
    private ForbiddenRequestHandler handler;

    @Mock
    private HttpServletRequest requestMock;
    @Mock
    private HttpServletResponse responseMock;
    @Mock
    private PrintWriter writerMock;

    @Test
    public void testHandle() throws IOException {
        AccessDeniedException exception = new AccessDeniedException(EXCEPTION_MSG);
        when(responseMock.getWriter()).thenReturn(writerMock);

        handler.handle(requestMock, responseMock, exception);

        verify(responseMock).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(responseMock).setContentType(MediaType.APPLICATION_JSON);
        verify(writerMock).write("{\n \"message\": \"" + EXCEPTION_MSG + "\"\n}");
    }
}
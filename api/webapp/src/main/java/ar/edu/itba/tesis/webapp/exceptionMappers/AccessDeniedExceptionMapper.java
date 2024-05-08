package ar.edu.itba.tesis.webapp.exceptionMappers;

import ar.edu.itba.tesis.webapp.dtos.ErrorDto;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.springframework.security.access.AccessDeniedException;

@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    @Override
    public Response toResponse(AccessDeniedException exception) {
        return Response
                .status(Response.Status.FORBIDDEN)
                .entity(ErrorDto.fromErrorMsg(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

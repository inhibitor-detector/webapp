package ar.edu.itba.tesis.webapp.exceptionMappers;

import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.webapp.dtos.ErrorDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.server.ParamException;

@Provider
public class QueryParamExceptionMapper implements ExceptionMapper<ParamException.QueryParamException> {
    @Override
    public Response toResponse(ParamException.QueryParamException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ErrorDto.fromErrorMsg(String.format("Query param '%s' has incorrect format. Example value: '%s'", exception.getParameterName(), exception.getDefaultStringValue())))
                .build();
    }
}

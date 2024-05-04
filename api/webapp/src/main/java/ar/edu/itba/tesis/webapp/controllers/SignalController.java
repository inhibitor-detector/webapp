package ar.edu.itba.tesis.webapp.controllers;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.DetectorNotFoundException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.service.DetectorService;
import ar.edu.itba.tesis.interfaces.service.SignalService;
import ar.edu.itba.tesis.interfaces.service.SignalService;
import ar.edu.itba.tesis.interfaces.service.UserService;
import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.Signal;
import ar.edu.itba.tesis.models.User;
import ar.edu.itba.tesis.webapp.dtos.DetectorDto;
import ar.edu.itba.tesis.webapp.dtos.SignalDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("/signals")
public class SignalController {
    private final DetectorService detectorService;
    private final UserService userService;
    private final SignalService signalService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public SignalController(DetectorService detectorService, UserService userService, SignalService signalService) {
        this.detectorService = detectorService;
        this.userService = userService;
        this.signalService = signalService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSignals() {
        final List<Signal> signals = signalService.findAll();

        if (signals.isEmpty()) {
            return Response
                    .noContent()
                    .build();
        }

        return Response
                .ok(SignalDto.fromSignals(signals))
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveSignal(@Valid SignalDto signalDto) throws NotFoundException, AlreadyExistsException {
        final Detector detector = detectorService.findById(signalDto.detectorId()).orElseThrow(() -> new DetectorNotFoundException(signalDto.detectorId()));

        final Signal signal = signalService.create(Signal.builder().detector(detector).timestamp(parseAndValidate(signalDto.timestamp())).isHeartbeat(signalDto.isHeartbeat()).build());

        return Response
                .created(uriInfo
                        .getAbsolutePathBuilder()
                        .path(signal.getId().toString())
                        .build())
                .entity(SignalDto.fromSignal(signal))
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSignal(@PathParam("id") Long id) throws NotFoundException {
        final Signal signal = signalService.findById(id).orElseThrow(() -> new NotFoundException(""));
        return Response
                .ok(SignalDto.fromSignal(signal))
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSignal(@PathParam("id") Long id) {
        signalService.deleteById(id);
        return Response
                .noContent()
                .build();
    }


    public static LocalDateTime parseAndValidate(String dateString) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        return LocalDateTime.parse(dateString, formatter);
    }

}

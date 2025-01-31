package ar.edu.itba.tesis.webapp.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.DetectorNotFoundException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.service.DetectorService;
import ar.edu.itba.tesis.interfaces.service.SignalService;
import ar.edu.itba.tesis.interfaces.service.UserService;
import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.Signal;
import ar.edu.itba.tesis.webapp.auth.AccessControl;
import ar.edu.itba.tesis.webapp.dtos.SignalDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/signals")
public class SignalController {
    private final DetectorService detectorService;
    private final UserService userService;
    private final SignalService signalService;
    private final AccessControl accessControl;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public SignalController(DetectorService detectorService, UserService userService, SignalService signalService, AccessControl accessControl) {
        this.detectorService = detectorService;
        this.userService = userService;
        this.signalService = signalService;
        this.accessControl = accessControl;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSignals(@Min(1) @DefaultValue("1") @QueryParam("page") final Integer page,
                               @Min(1) @Max(100) @DefaultValue("10") @QueryParam("pageSize") final Integer pageSize,
                               @Min(0) @DefaultValue("0") @QueryParam("ownerId") final Long ownerId,
                               @Min(0) @DefaultValue("0") @QueryParam("detectorId") final Long detectorId,
                               @QueryParam("isHeartbeat") final Boolean isHeartBeat, @QueryParam("acknowledged") final Boolean acknowledged
    ) {
        final List<Signal> signals = signalService.findAllPaginated(page, pageSize, ownerId, detectorId, isHeartBeat, acknowledged);

        if (signals.isEmpty()) {
            return Response
                    .noContent()
                    .build();
        }
        // TODO: Set links to proper pages
        return Response
                .ok(SignalDto.fromSignals(signals))
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveSignal(@Valid SignalDto signalDto) throws NotFoundException, AlreadyExistsException {
        final Detector detector = detectorService.findById(signalDto.detectorId()).orElseThrow(() -> new DetectorNotFoundException(signalDto.detectorId()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!accessControl.canPostSignalCheckDetectorId(authentication, detector)) throw new AccessDeniedException("Access denied");

        final Signal signal = signalService.create(buildNewSignal(signalDto, detector));

        // We also update the detector last_heartbeat
        if (signalDto.isHeartbeat()) {
            detector.setLastHeartbeat(LocalDateTime.now());
            // detectorService.save(detector);  // Make sure this method exists in your service
            detectorService.update(detector.getId(), detector);
        }

        return Response
                .created(uriInfo
                        .getAbsolutePathBuilder()
                        .path(signal.getId().toString())
                        .build())
                .entity(SignalDto.fromSignal(signal))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSignal(@PathParam("id") Long id, @Valid SignalDto signalDto) throws AlreadyExistsException, NotFoundException {
        final Detector detector = detectorService.findById(signalDto.detectorId()).orElseThrow(() -> new DetectorNotFoundException(signalDto.detectorId()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!accessControl.canPutSignal(authentication)) throw new AccessDeniedException("Access denied");

        final Signal signal = signalService.update(id, buildNewSignal(signalDto, detector));
        return Response
                .ok(SignalDto.fromSignal(signal))
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

    @GET
    @Path("/time")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSignalsByTime(
            @QueryParam("startTime") String startTime,
            @QueryParam("endTime") String endTime,
            @QueryParam("ownerId") final Long ownerId) {
        LocalDateTime start = parseAndValidate(startTime);
        LocalDateTime end = parseAndValidate(endTime);

        List<Signal> signals = signalService.findByTime(start, end, ownerId);

        List<SignalDto> signalDtos = signals.stream()
                .map(SignalDto::fromSignal)
                .toList();
        return Response.ok(signalDtos).build();
    }

    private Signal buildNewSignal(SignalDto signalDto, Detector detector) {
        return Signal.builder()
                .detector(detector)
                .timestamp(parseAndValidate(signalDto.timestamp()))
                .isHeartbeat(signalDto.isHeartbeat())
                .acknowledged(signalDto.acknowledged())
                .build();
    }
}

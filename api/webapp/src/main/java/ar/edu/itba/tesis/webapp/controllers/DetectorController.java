package ar.edu.itba.tesis.webapp.controllers;

import ar.edu.itba.tesis.interfaces.service.DetectorService;
import ar.edu.itba.tesis.interfaces.service.SignalService;
import ar.edu.itba.tesis.interfaces.service.UserService;
import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.DetectorNotFoundException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.User;
import ar.edu.itba.tesis.webapp.dtos.DetectorDto;
import ar.edu.itba.tesis.webapp.dtos.UserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Path("/detectors")
public class DetectorController {
    private final DetectorService detectorService;
    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public DetectorController(DetectorService detectorService, UserService userService) {
        this.detectorService = detectorService;
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDetectors(@Min(1) @DefaultValue("1") @QueryParam("page") final Integer page,
                                 @Min(1) @Max(100) @DefaultValue("10") @QueryParam("pageSize") final Integer pageSize,
                                 @Min(0) @DefaultValue("0") @QueryParam("ownerId") final Long ownerId) {
        final List<Detector> detectors = detectorService.findAllPaginated(page, pageSize, ownerId);

        if (detectors.isEmpty()) {
            return Response
                    .noContent()
                    .build();
        }

        return Response
                .ok(DetectorDto.fromDetectors(detectors))
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveDetector(@Valid DetectorDto detectorDto) throws NotFoundException, AlreadyExistsException {
        final User ownerUser = userService.findById(detectorDto.ownerId()).orElseThrow(() -> new DetectorNotFoundException(detectorDto.ownerId()));
        final User user = userService.findById(detectorDto.userId()).orElseThrow(() -> new DetectorNotFoundException(detectorDto.userId()));

        final Detector detector = detectorService.
                create(Detector.builder()
                        .owner(ownerUser)
                        .user(user)
                        .isOnline(detectorDto.isOnline())
                        .version(detectorDto.version())
                        .name(detectorDto.name())
                        .description(detectorDto.description())
                        .build());

        return Response
                .created(uriInfo
                        .getAbsolutePathBuilder()
                        .path(detector.getId().toString())
                        .build())
                .entity(DetectorDto.fromDetector(detector))
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDetector(@PathParam("id") Long id) throws NotFoundException {
        final Detector detector = detectorService.findById(id).orElseThrow(() -> new DetectorNotFoundException(id));
        return Response
                .ok(DetectorDto.fromDetector(detector))
                .build();
    }

//    @PUT
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response updateDetector(@PathParam("id") Long id, @Valid DetectorDto detectorDto) throws AlreadyExistsException, NotFoundException {
//        final Detector detector = detectorService.update(id, buildNewDetector(detectorDto));
//        return Response
//                .ok(DetectorDto.fromDetector(detector))
//                .build();
//    }

    @DELETE
    @Path("/{id}")
    public Response deleteDetector(@PathParam("id") Long id) {
        detectorService.deleteById(id);
        return Response
                .noContent()
                .build();
    }

}

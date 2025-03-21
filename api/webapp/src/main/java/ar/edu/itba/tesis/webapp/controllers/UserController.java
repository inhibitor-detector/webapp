package ar.edu.itba.tesis.webapp.controllers;

import ar.edu.itba.tesis.interfaces.service.UserService;
import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.exceptions.UserNotFoundException;
import ar.edu.itba.tesis.models.User;
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


@Path("/users")
public class UserController {
    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@Min (1) @DefaultValue("1") @QueryParam("page") final Integer page,
                             @Min(1) @Max(100) @DefaultValue("10") @QueryParam("pageSize") final Integer pageSize) {
        final List<User> users = userService.findAllPaginated(page, pageSize);

        if (users.isEmpty()) {
            return Response
                    .noContent()
                    .build();
        }

        return Response
                .ok(UserDto.fromUsers(users))
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveUser(@Valid UserDto userDto) throws AlreadyExistsException {
        final User user = userService.create(buildNewUser(userDto));

        return Response
                .created(uriInfo
                        .getAbsolutePathBuilder()
                        .path(user.getId().toString())
                        .build())
                .entity(UserDto.fromUser(user))
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Long id) throws NotFoundException {
        final User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return Response
                .ok(UserDto.fromUser(user))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, @Valid UserDto userDto) throws AlreadyExistsException, NotFoundException {
        final User user = userService.update(id, buildNewUser(userDto));
        return Response
                .ok(UserDto.fromUser(user))
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        userService.deleteById(id);
        return Response
                .noContent()
                .build();
    }

    /*
    Auxiliary methods
     */
    private User buildNewUser(UserDto userDto) {
        return User.builder()
                .email(userDto.email())
                .username(userDto.username())
                .password(userDto.password())
                .build();
    }
}

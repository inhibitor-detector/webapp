package ar.edu.itba.tesis.webapp.controllers;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.service.UserService;
import ar.edu.itba.tesis.models.User;
import ar.edu.itba.tesis.webapp.dtos.UserDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private final Long ID = 1L;
    private final User user = getUser();
    private final UserDto userDto = getUserDto();

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userServiceMock;
    @Mock
    private UriInfo uriInfoMock;
    @Mock
    private UriBuilder uriBuilderMock;

    @Test
    void testGetUsers() {
        when(userServiceMock.findAllPaginated(1, 10)).thenReturn(List.of(user));

        Response response = userController.getUsers(1, 10);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(List.of(UserDto.fromUser(user)), response.getEntity());
    }

    @Test
    void testGetUsersEmptyResult() {
        when(userServiceMock.findAllPaginated(1, 10)).thenReturn(Collections.emptyList());

        Response response = userController.getUsers(1, 10);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    void testSaveUser() throws AlreadyExistsException {
        setUpUriMocks();
        when(userServiceMock.create(any(User.class))).thenReturn(user);

        Response response = userController.saveUser(userDto);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(UserDto.fromUser(user), response.getEntity());
    }

    @Test
    void testGetUser() throws NotFoundException {
        when(userServiceMock.findById(ID)).thenReturn(Optional.of(user));

        Response response = userController.getUser(ID);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(UserDto.fromUser(user), response.getEntity());
    }

    @Test
    void testGetUserNotFound() {
        when(userServiceMock.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userController.getUser(ID));
    }

    @Test
    void testUpdateUser() throws AlreadyExistsException, NotFoundException {
        when(userServiceMock.update(eq(ID), any())).thenReturn(user);

        Response response = userController.updateUser(ID, userDto);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void testDeleteUser() {
        Response response = userController.deleteUser(ID);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(userServiceMock).deleteById(ID);
    }

    private User getUser() {
        User user = new User();
        user.setId(ID);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("12345678");
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    private UserDto getUserDto() {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getCreatedAt().toString(),
                user.getRoles()
        );
    }

    private void setUpUriMocks() {
        ReflectionTestUtils.setField(userController, "uriInfo", uriInfoMock);
        when(uriInfoMock.getAbsolutePathBuilder()).thenReturn(uriBuilderMock);
        when(uriBuilderMock.path(anyString())).thenReturn(uriBuilderMock);
    }
}
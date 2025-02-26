package ar.edu.itba.tesis.services;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.models.User;
import ar.edu.itba.tesis.persistence.UserHibernateDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final Long ID = 1L;
    private final String EMAIL = "user@email.com";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";

    private final User user = getUser();

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserHibernateDao userDaoMock;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCreateUser() throws AlreadyExistsException {
        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
        when(userDaoMock.create(any(User.class))).thenReturn(user);

        User createdUser = userService.create(user);

        assertNotNull(createdUser);
        assertEquals(PASSWORD, createdUser.getPassword());
        verify(userDaoMock).create(user);
    }

    @Test
    public void testFindUserById() {
        when(userDaoMock.findById(ID)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(ID);

        assertTrue(foundUser.isPresent());
        assertEquals(ID, foundUser.get().getId());
        verify(userDaoMock).findById(ID);
    }

    @Test
    public void testFindUserByIdNotFound() {
        when(userDaoMock.findById(2L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findById(2L);

        assertTrue(foundUser.isEmpty());
        verify(userDaoMock).findById(2L);
    }

    @Test
    public void testFindAllUsers() {
        when(userDaoMock.findAll()).thenReturn(List.of(user));

        List<User> users = userService.findAll();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(userDaoMock).findAll();
    }

    @Test
    public void testFindAllUsersPaginated() {
        when(userDaoMock.findAllPaginated(1, 10)).thenReturn(List.of(user));

        List<User> users = userService.findAllPaginated(1, 10);

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(userDaoMock).findAllPaginated(1, 10);
    }

    @Test
    public void testUpdateUser() throws NotFoundException, AlreadyExistsException {
        when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
        when(userDaoMock.update(eq(ID), any(User.class))).thenReturn(user);

        User updatedUser = userService.update(ID, user);

        assertNotNull(updatedUser);
        assertEquals(PASSWORD, updatedUser.getPassword());
        verify(userDaoMock).update(eq(ID), any(User.class));
    }

    @Test
    public void testDeleteUserById() {
        userService.deleteById(ID);

        verify(userDaoMock).deleteById(ID);
    }

    @Test
    public void testDeleteUser() {
        userService.delete(user);

        verify(userDaoMock).delete(user);
    }

    @Test
    public void testUserExists() {
        when(userDaoMock.existsById(ID)).thenReturn(true);

        boolean exists = userDaoMock.existsById(ID);

        assertTrue(exists);
        verify(userDaoMock).existsById(ID);
    }

    @Test
    public void testUserCount() {
        when(userDaoMock.count()).thenReturn(10L);

        Long count = userService.count();

        assertEquals(10L, count);
        verify(userDaoMock).count();
    }

    @Test
    public void testFindUserByEmail() {
        when(userDaoMock.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByEmail(EMAIL);

        assertTrue(foundUser.isPresent());
        verify(userDaoMock).findByEmail(EMAIL);
    }

    @Test
    public void testFindUserByUsername() {
        when(userDaoMock.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername(USERNAME);

        assertTrue(foundUser.isPresent());
        verify(userDaoMock).findByUsername(USERNAME);
    }

    private User getUser() {
        User user = new User();
        user.setId(ID);
        user.setEmail(EMAIL);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        return user;
    }
}
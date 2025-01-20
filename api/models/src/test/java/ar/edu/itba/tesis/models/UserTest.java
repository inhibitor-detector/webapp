package ar.edu.itba.tesis.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserTest {

    private final String EMAIL = "test@example.com";
    private final String USERNAME = "testUser";
    private final String PASSWORD = "testPassword";
    private final Long ID = 1L;

    @InjectMocks
    private final User user = new User();

    @Mock
    private Role roleMock;

    @Test
    public void testSetEmail() {
        user.setEmail(EMAIL);
        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    public void testSetUsername() {
        user.setUsername(USERNAME);
        assertEquals(USERNAME, user.getUsername());
    }

    @Test
    public void testSetPassword() {
        user.setPassword(PASSWORD);
        assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    public void testSetEnabled() {
        user.setEnabled(true);
        assertTrue(user.isEnabled());

        user.setEnabled(false);
        assertFalse(user.isEnabled());
    }

    @Test
    public void testSetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        user.setCreatedAt(createdAt);
        assertEquals(createdAt, user.getCreatedAt());
    }

    @Test
    public void testAddRole() {
        user.addRole(roleMock);
        Set<Role> roles = user.getRoles();
        assertTrue(roles.contains(roleMock));
    }

    @Test
    public void testEquals() {
        User user1 = new User();
        user1.setId(ID);

        User user2 = new User();
        user2.setId(ID);

        assertEquals(user1, user2);

        user2.setId(2L);
        assertNotEquals(user1, user2);
    }

    @Test
    public void testHashCode() {
        User user1 = new User();
        user1.setId(ID);

        User user2 = new User();
        user2.setId(ID);

        assertEquals(user1.hashCode(), user2.hashCode());

        user2.setId(2L);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testToString() {
        user.setId(ID);
        user.setEmail(EMAIL);
        user.setUsername(USERNAME);

        String expectedToString = "User " + user.getId() + ": [email = " + user.getEmail() + ", name = " + user.getUsername() + "]";
        assertEquals(expectedToString, user.toString());
    }
}

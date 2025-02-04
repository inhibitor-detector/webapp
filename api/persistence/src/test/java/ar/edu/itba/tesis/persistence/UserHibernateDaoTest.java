package ar.edu.itba.tesis.persistence;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.UsernameAlreadyExistsException;
import ar.edu.itba.tesis.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserHibernateDaoTest {

    private final Long ID = 1L;
    private final String EMAIL = "user@email.com";
    private final String USERNAME = "username";

    private final User user = getUser();

    @InjectMocks
    private UserHibernateDao userHibernateDao;

    @Mock
    private EntityManager entityManagerMock;
    @Mock
    private TypedQuery<User> typedQueryMock;
    @Mock
    private CriteriaQuery<User> criteriaQueryMock;
    @Mock
    private CriteriaBuilder criteriaBuilderMock;
    @Mock
    private Query queryMock;
    @Mock
    private TypedQuery<Long> queryLongMock;
    @Mock
    private Root<User> rootMock;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userHibernateDao, "entityManager", entityManagerMock);
    }

    @Test
    public void testCreateUser() throws AlreadyExistsException {
        setUpMocksForCreate();

        User result = userHibernateDao.create(user);

        assertNotNull(user.getId());
        assertEquals(ID, result.getId());
        verify(entityManagerMock).persist(user);
    }

    @Test
    public void testCreateUserWithExistingEmail() {
        final User newUser = new User();
        newUser.setId(ID + 1);
        newUser.setEmail(EMAIL);

        setUpMocksForCreate();

        assertThrows(UsernameAlreadyExistsException.class,
                () -> userHibernateDao.create(newUser));
    }

    @Test
    public void testFindUserById() {
        when(entityManagerMock.find(User.class, ID)).thenReturn(user);

        final Optional<User> result = userHibernateDao.findById(ID);

        assertTrue(result.isPresent());
        assertEquals(EMAIL, result.get().getEmail());
    }

    @Test
    public void testFindUserByIdNotFound() {
        final Optional<User> result = userHibernateDao.findById(ID);

        assertTrue(result.isEmpty());
        verify(entityManagerMock).find(User.class, ID);
    }

    @Test
    public void testFindAllUsers() {
        when(entityManagerMock.getCriteriaBuilder()).thenReturn(criteriaBuilderMock);
        when(criteriaBuilderMock.createQuery(User.class)).thenReturn(criteriaQueryMock);
        when(entityManagerMock.createQuery(criteriaQueryMock)).thenReturn(typedQueryMock);
        when(typedQueryMock.getResultList()).thenReturn(List.of(user));

        final List<User> result = userHibernateDao.findAll();

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(criteriaBuilderMock).createQuery(User.class);
        verify(entityManagerMock).createQuery(criteriaQueryMock);
    }

    @Test
    public void testDeleteUserById() {
        String query = "DELETE FROM users WHERE id = :id";
        setUpMocksForDelete(query);

        userHibernateDao.deleteById(ID);

        verify(entityManagerMock).createNativeQuery(query);
        verify(queryMock).setParameter("id", ID);
        verify(queryMock).executeUpdate();
    }

    @Test
    public void testDeleteUser() {
        String query = "DELETE FROM users WHERE id = :id";
        setUpMocksForDelete(query);

        userHibernateDao.delete(user);

        verify(entityManagerMock).createNativeQuery(query);
        verify(queryMock).setParameter("id", ID);
        verify(queryMock).executeUpdate();
    }

    @Test
    public void testUserExists() {
        when(entityManagerMock.find(User.class, ID)).thenReturn(user);

        final boolean exists = userHibernateDao.existsById(ID);

        assertTrue(exists);
    }

    @Test
    public void testUserNotExists() {
        final boolean exists = userHibernateDao.existsById(100L);

        assertFalse(exists);
    }

    @Test
    public void testCountUsers() {
        String query = "SELECT COUNT(*) FROM User";
        when(entityManagerMock.createQuery(query, Long.class)).thenReturn(queryLongMock);
        when(queryLongMock.getSingleResult()).thenReturn(10L);

        final long count = userHibernateDao.count();

        assertEquals(10, count);
        verify(entityManagerMock).createQuery(query, Long.class);
        verify(queryLongMock).getSingleResult();
    }

    @Test
    public void testFindUserByEmail() {
        String query = "FROM User WHERE email = :email";
        setUpEntityManagerMocks(query, "email", EMAIL);
        when(typedQueryMock.getResultList()).thenReturn(List.of(user));

        final Optional<User> result = userHibernateDao.findByEmail(EMAIL);

        assertTrue(result.isPresent());
        assertEquals(ID, result.get().getId());
        assertEquals(EMAIL, result.get().getEmail());
    }

    @Test
    public void testFindUserByEmailNotFound() {
        String query = "FROM User WHERE email = :email";
        setUpEntityManagerMocks(query, "email", EMAIL);

        final Optional<User> result = userHibernateDao.findByEmail(EMAIL);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindUserByUsername() {
        String query = "FROM User WHERE username = :username";
        setUpEntityManagerMocks(query, "username", USERNAME);;
        when(typedQueryMock.getResultList()).thenReturn(List.of(user));

        final Optional<User> result = userHibernateDao.findByUsername(USERNAME);

        assertTrue(result.isPresent());
        assertEquals(ID, result.get().getId());
        assertEquals(EMAIL, result.get().getEmail());
        assertEquals(USERNAME, result.get().getUsername());
    }

    @Test
    public void testFindUserByUsernameNotFound() {
        String query = "FROM User WHERE username = :username";
        setUpEntityManagerMocks(query, "username", USERNAME);;

        final Optional<User> user = userHibernateDao.findByUsername(USERNAME);

        assertTrue(user.isEmpty());
    }

    private User getUser() {
        User user = new User();
        user.setId(ID);
        user.setEmail(EMAIL);
        user.setUsername(USERNAME);
        return user;
    }

    private void setUpMocksForCreate() {
        when(entityManagerMock.createQuery(anyString(), eq(User.class))).thenReturn(typedQueryMock);
        when(typedQueryMock.setParameter(anyString(), any())).thenReturn(typedQueryMock);
        when(typedQueryMock.getResultList()).thenReturn(List.of(user));
    }

    private void setUpMocksForDelete(String query) {
        when(entityManagerMock.createNativeQuery(query)).thenReturn(queryMock);
        when(queryMock.setParameter("id", ID)).thenReturn(queryMock);
    }

    private void setUpEntityManagerMocks(String query, String parameter, String value) {
        when(entityManagerMock.createQuery(query, User.class)).thenReturn(typedQueryMock);
        when(typedQueryMock.setParameter(parameter, value)).thenReturn(typedQueryMock);
    }

}
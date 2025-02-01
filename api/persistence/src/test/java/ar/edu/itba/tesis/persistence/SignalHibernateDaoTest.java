package ar.edu.itba.tesis.persistence;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.models.Signal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignalHibernateDaoTest {

    private final Long ID = 1L;
    private final Signal signal = getSignal();

    @InjectMocks
    private SignalHibernateDao signalDao;

    @Mock
    private EntityManager entityManagerMock;
    @Mock
    private TypedQuery<Signal> typedQueryMock;
    @Mock
    private CriteriaBuilder criteriaBuilderMock;
    @Mock
    private CriteriaQuery<Signal> criteriaQueryMock;
    @Mock
    private Root<Signal> rootMock;
    @Mock
    private Query queryMock;
    @Mock
    private Path<Object> pathMock;

    @Test
    public void testCreateSignal() {
        Signal result = signalDao.create(signal);

        assertNotNull(result);
        assertEquals(ID, result.getId());
        verify(entityManagerMock).persist(signal);
    }

    @Test
    public void testFindSignalById() {
        when(entityManagerMock.find(Signal.class, ID)).thenReturn(signal);

        Optional<Signal> result = signalDao.findById(ID);

        assertTrue(result.isPresent());
        assertEquals(ID, result.get().getId());
    }

    @Test
    public void testFindSignalByIdNotFound() {
        Optional<Signal> result = signalDao.findById(ID);

        assertTrue(result.isEmpty());
        verify(entityManagerMock).find(Signal.class, ID);
    }

    @Test
    public void testFindAllSignals() {
        setUpEntityManagerMocks();
        when(rootMock.get("timestamp")).thenReturn(pathMock);

        List<Signal> result = signalDao.findAll();

        assertResult(result);
        verifyCreateQueryAndExecution();
    }

    @Test
    public void testFindAllSignalsPaginated() {
        setUpEntityManagerMocks();
        when(rootMock.get("timestamp")).thenReturn(pathMock);

        List<Signal> result = signalDao.findAllPaginated(1, 10);

        assertResult(result);
        verifyCreateQueryAndExecution();
        verifySetResults();
    }

    @Test
    public void testFindAllSignalsPaginatedWithParams() {
        setUpEntityManagerMocks();
        setupRootMocks();
        when(rootMock.get("acknowledged")).thenReturn(pathMock);

        List<Signal> result = signalDao.findAllPaginated(1, 10, 1L, 1L, true, true);

        assertResult(result);
        verifyCreateQueryAndExecution();
        verifySetResults();
        verify(entityManagerMock).getCriteriaBuilder();
        verify(criteriaQueryMock).from(Signal.class);
        verify(criteriaQueryMock).orderBy(criteriaBuilderMock.desc(pathMock));
    }

    @Test
    public void testUpdateSignal() throws AlreadyExistsException, NotFoundException {
        when(entityManagerMock.find(Signal.class, ID)).thenReturn(signal);
        signal.setAcknowledged(true);

        signalDao.update(ID, signal);

        verify(entityManagerMock).persist(signal);
        assertTrue(signal.getAcknowledged());
    }

    @Test
    public void testUpdateSignalNotFound() {
        assertThrows(NotFoundException.class, () -> signalDao.update(ID, signal));
        verify(entityManagerMock, never()).persist(any());
    }

    @Test
    public void testDeleteSignalById() {
        String query = "DELETE FROM signals WHERE id = :id";
        when(entityManagerMock.createNativeQuery(query)).thenReturn(queryMock);
        when(queryMock.setParameter("id", ID)).thenReturn(queryMock);

        signalDao.deleteById(ID);

        verify(entityManagerMock).createNativeQuery(query);
        verify(queryMock).setParameter("id", ID);
        verify(queryMock).executeUpdate();
    }

    @Test
    public void testSignalExists() {
        when(entityManagerMock.find(Signal.class, ID)).thenReturn(signal);

        boolean exists = signalDao.existsById(ID);

        assertTrue(exists);
    }

    @Test
    public void testSignalNotExists() {
        boolean exists = signalDao.existsById(ID);

        assertFalse(exists);
    }

    @Test
    public void testFindByTime() {
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 2, 0, 0);

        setUpEntityManagerMocks();
        setupRootMocks();
        when(criteriaQueryMock.select(rootMock)).thenReturn(criteriaQueryMock);

        List<Signal> result = signalDao.findByTime(startTime, endTime, ID);

        assertResult(result);
        verifyCreateQueryAndExecution();
        verify(entityManagerMock).getCriteriaBuilder();
    }

    private Signal getSignal() {
        Signal signal = new Signal();
        signal.setId(ID);
        return signal;
    }

    private void setUpEntityManagerMocks() {
        when(entityManagerMock.getCriteriaBuilder()).thenReturn(criteriaBuilderMock);
        when(criteriaBuilderMock.createQuery(Signal.class)).thenReturn(criteriaQueryMock);
        when(entityManagerMock.createQuery(criteriaQueryMock)).thenReturn(typedQueryMock);
        when(typedQueryMock.getResultList()).thenReturn(List.of(signal));
        when(criteriaQueryMock.from(Signal.class)).thenReturn(rootMock);
    }

    private void setupRootMocks() {
        when(rootMock.get("timestamp")).thenReturn(pathMock);
        when(rootMock.get("isHeartbeat")).thenReturn(pathMock);
        when(rootMock.get("detector")).thenReturn(pathMock);
        when(pathMock.get("owner")).thenReturn(pathMock);
    }

    private void assertResult(List<Signal> result) {
        assertEquals(1, result.size());
        assertEquals(signal, result.get(0));
    }

    private void verifyCreateQueryAndExecution() {
        verify(criteriaBuilderMock).createQuery(Signal.class);
        verify(entityManagerMock).createQuery(criteriaQueryMock);
        verify(typedQueryMock).getResultList();
    }

    private void verifySetResults() {
        verify(typedQueryMock).setMaxResults(10);
        verify(typedQueryMock).setFirstResult(0);
    }
}
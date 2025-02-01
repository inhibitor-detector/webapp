package ar.edu.itba.tesis.persistence;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.DetectorNotFoundException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.models.Detector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DetectorHibernateDaoTest {

    private final Long ID = 1L;
    private final Detector detector = getDetector();

    @InjectMocks
    private DetectorHibernateDao detectorDao;

    @Mock
    private EntityManager entityManagerMock;
    @Mock
    private TypedQuery<Detector> typedQueryMock;
    @Mock
    private CriteriaBuilder criteriaBuilderMock;
    @Mock
    private CriteriaQuery<Detector> criteriaQueryMock;
    @Mock
    private Root<Detector> rootMock;
    @Mock
    private Predicate predicateMock;
    @Mock
    private Query queryMock;
    @Mock
    private Path<Object> pathMock;

    @Test
    public void testCreateDetector() {
        Detector result = detectorDao.create(detector);

        assertNotNull(result);
        assertEquals(ID, result.getId());
        verify(entityManagerMock).persist(detector);
    }

    @Test
    public void testFindDetectorById() {
        when(entityManagerMock.find(Detector.class, ID)).thenReturn(detector);

        Optional<Detector> result = detectorDao.findById(ID);

        assertTrue(result.isPresent());
        assertEquals(ID, result.get().getId());
    }

    @Test
    public void testFindDetectorByIdNotFound() {
        Optional<Detector> result = detectorDao.findById(ID);

        assertTrue(result.isEmpty());
        verify(entityManagerMock).find(Detector.class, ID);
    }

    @Test
    public void testFindAllDetectors() {
        setUpEntityManagerMocks();

        List<Detector> result = detectorDao.findAll();

        assertResult(result);
        verifyCreateQuery();
    }

    @Test
    public void testFindAllDetectorsPaginated() {
        setUpEntityManagerMocks();

        List<Detector> result = detectorDao.findAllPaginated(1, 10);

        assertResult(result);
        verifyCreateQuery();
        verifySetResults();
    }

    @Test
    public void testFindDetectorByOwnerIdPaginated() {
        setUpEntityManagerMocks();
        when(rootMock.get("owner")).thenReturn(pathMock);
        when(pathMock.get("id")).thenReturn(pathMock);
        when(criteriaBuilderMock.equal(pathMock, ID)).thenReturn(predicateMock);
        when(criteriaQueryMock.from(Detector.class)).thenReturn(rootMock);

        List<Detector> result = detectorDao.findByOwnerIdPaginated(1, 10, ID);

        assertResult(result);
        verifyCreateQuery();
        verifySetResults();
    }

    @Test
    public void testUpdateDetector() throws AlreadyExistsException, NotFoundException {
        when(entityManagerMock.find(Detector.class, ID)).thenReturn(detector);

        detectorDao.update(ID, detector);

        verify(entityManagerMock).persist(detector);
    }

    @Test
    public void testUpdateDetectorNotFound() {
        assertThrows(DetectorNotFoundException.class, () -> detectorDao.update(ID, detector));
        verify(entityManagerMock, never()).persist(any());
    }

    @Test
    public void testDeleteDetectorById() {
        String query = "DELETE FROM detectors WHERE id = :id";
        when(entityManagerMock.createNativeQuery(query)).thenReturn(queryMock);
        when(queryMock.setParameter("id", ID)).thenReturn(queryMock);

        detectorDao.deleteById(ID);

        verify(entityManagerMock).createNativeQuery(query);
        verify(queryMock).setParameter("id", ID);
        verify(queryMock).executeUpdate();
    }

    private Detector getDetector() {
        Detector detector = new Detector();
        detector.setId(ID);
        return detector;
    }

    private void setUpEntityManagerMocks() {
        when(entityManagerMock.getCriteriaBuilder()).thenReturn(criteriaBuilderMock);
        when(criteriaBuilderMock.createQuery(Detector.class)).thenReturn(criteriaQueryMock);
        when(entityManagerMock.createQuery(criteriaQueryMock)).thenReturn(typedQueryMock);
        when(typedQueryMock.getResultList()).thenReturn(List.of(detector));
    }

    private void assertResult(List<Detector> result) {
        assertEquals(1, result.size());
        assertEquals(detector, result.get(0));
    }

    private void verifyCreateQuery() {
        verify(criteriaBuilderMock).createQuery(Detector.class);
        verify(entityManagerMock).createQuery(criteriaQueryMock);
    }

    private void verifySetResults() {
        verify(typedQueryMock).setMaxResults(10);
        verify(typedQueryMock).setFirstResult(0);
    }
}
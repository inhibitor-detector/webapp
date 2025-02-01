package ar.edu.itba.tesis.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.DetectorNotFoundException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.persistence.DetectorDao;
import ar.edu.itba.tesis.models.Detector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class DetectorHibernateDao implements DetectorDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Detector create(Detector entity) {
        entityManager.persist(entity); // TODO: Validate user_id unique
        return entity;
    }

    @Override
    public Optional<Detector> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Detector.class, id));

    }

    // Refactor this
    private TypedQuery<Detector> getDetectorsQuery() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Detector> criteriaQuery = criteriaBuilder.createQuery(Detector.class);
        Root<Detector> root = criteriaQuery.from(Detector.class);

        criteriaQuery.select(root);

        return entityManager.createQuery(criteriaQuery);
    }

    @Override
    public List<Detector> findAll() {
        TypedQuery<Detector> query = getDetectorsQuery();
        return query.getResultList();
    }

    @Override
    public List<Detector> findAllPaginated(Integer page, Integer pageSize) {
        TypedQuery<Detector> query = getDetectorsQuery();

        query.setMaxResults(pageSize);
        query.setFirstResult((page - 1) * pageSize);
        return query.getResultList();
    }

    @Override
    public List<Detector> findByOwnerIdPaginated(Integer page, Integer pageSize, Long ownerId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Detector> criteriaQuery = criteriaBuilder.createQuery(Detector.class);
        Root<Detector> root = criteriaQuery.from(Detector.class);

        criteriaQuery.select(root);

        Predicate condition = criteriaBuilder.equal(root.get("owner").get("id"), ownerId); // Reemplaza "propertyName" con el nombre real de la propiedad y "value" con el valor de filtro deseado
        criteriaQuery.where(condition);

        TypedQuery<Detector> query = entityManager.createQuery(criteriaQuery);

        query.setMaxResults(pageSize);
        query.setFirstResult((page - 1) * pageSize);
        return query.getResultList();
    }

    @Override
    public Detector update(Long id, Detector entity) throws NotFoundException, AlreadyExistsException {
        Detector user = findById(id).orElseThrow(() -> new DetectorNotFoundException(id));
        updateDetector(user, entity);
        entityManager.persist(user);
        return user;
    }

    @Override
    public void deleteById(Long id) {
        entityManager.createNativeQuery("DELETE FROM detectors WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void delete(Detector entity) {
        deleteById(entity.getId());
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    // TODO
    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(*) FROM Detector", Long.class)
                .getSingleResult();
    }

    private void updateDetector(Detector user, Detector newValues) {
        // TODO Update detector
    }

    @Override
    public void updateLastHeartbeat(Long id, LocalDateTime lastHeartbeat) {
        System.out.println("in hibernate dao Updating last heartbeat for detector " + id + " to " + lastHeartbeat);
        entityManager.createNativeQuery("UPDATE detectors SET last_heartbeat = :lastHeartbeat WHERE id = :id")
                .setParameter("lastHeartbeat", lastHeartbeat)
                .setParameter("id", id)
                .executeUpdate();
    }
}

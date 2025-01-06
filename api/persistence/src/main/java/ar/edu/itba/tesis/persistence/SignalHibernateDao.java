package ar.edu.itba.tesis.persistence;

import ar.edu.itba.tesis.interfaces.exceptions.AlreadyExistsException;
import ar.edu.itba.tesis.interfaces.exceptions.NotFoundException;
import ar.edu.itba.tesis.interfaces.persistence.SignalDao;
import ar.edu.itba.tesis.models.Signal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class SignalHibernateDao implements SignalDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Signal create(Signal entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Signal> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Signal.class, id));

    }

    // Refactor this
    private TypedQuery<Signal> getSignalsQuery() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Signal> criteriaQuery = criteriaBuilder.createQuery(Signal.class);
        Root<Signal> root = criteriaQuery.from(Signal.class);

        criteriaQuery.select(root);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("timestamp")));

        return entityManager.createQuery(criteriaQuery);
    }

    @Override
    public List<Signal> findAll() {
        TypedQuery<Signal> query = getSignalsQuery();
        return query.getResultList();
    }

    @Override
    public List<Signal> findAllPaginated(Integer page, Integer pageSize) {
        TypedQuery<Signal> query = getSignalsQuery();

        query.setMaxResults(pageSize);
        query.setFirstResult((page - 1) * pageSize);
        return query.getResultList();
    }

    @Override
    public List<Signal> findAllPaginated(Integer page, Integer pageSize, Long ownerId, Long detectorId, Boolean isHeartbeat) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Signal> criteriaQuery = criteriaBuilder.createQuery(Signal.class);
        Root<Signal> root = criteriaQuery.from(Signal.class);

        criteriaQuery.select(root);
        // Sets where SQL clauses if they exist
        setWhereClauses(criteriaBuilder, criteriaQuery, root, ownerId, detectorId, isHeartbeat);

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("timestamp")));

        TypedQuery<Signal> query = entityManager.createQuery(criteriaQuery);

        query.setMaxResults(pageSize);
        query.setFirstResult((page - 1) * pageSize);
        return query.getResultList();
    }



    @Override
    public Signal update(Long id, Signal entity) throws NotFoundException, AlreadyExistsException {
        Signal user = findById(id).orElseThrow(() -> new NotFoundException("Heartbeat not found"));
        updateHeartbeat(user, entity);
        entityManager.persist(user);
        return user;
    }

    @Override
    public void deleteById(Long id) {
        entityManager.createNativeQuery("DELETE FROM users WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void delete(Signal entity) {
        deleteById(entity.getId());
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    // TODO
    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(*) FROM Signal", Long.class)
                .getSingleResult();
    }

    private void updateHeartbeat(Signal user, Signal newValues) {
        // TODO Update heartbeat
    }

    // Aux methods
    private void setWhereClauses(CriteriaBuilder criteriaBuilder, CriteriaQuery<Signal> criteriaQuery, Root<Signal> root, Long ownerId, Long detectorId, Boolean isHeartbeat) {
        Predicate ownerIdCondition = criteriaBuilder.conjunction(); // Always returns true
        if (ownerId != 0) {
            ownerIdCondition = criteriaBuilder.equal(root.get("detector").get("owner").get("id"), ownerId);
        }
        Predicate detectorIdCondition = criteriaBuilder.conjunction(); // Always returns true

        if (detectorId != 0) {
            detectorIdCondition = criteriaBuilder.equal(root.get("detector").get("id"), detectorId);
        }

        Predicate isHeartbeatCondition = criteriaBuilder.conjunction(); // Always returns true
        if (isHeartbeat != null) {
            isHeartbeatCondition = criteriaBuilder.equal(root.get("isHeartbeat"), isHeartbeat);
        }

        criteriaQuery.where(criteriaBuilder.and(ownerIdCondition, detectorIdCondition, isHeartbeatCondition));
    }

    public List<Signal> findByTime(LocalDateTime startTime, LocalDateTime endTime, Long ownerId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Signal> criteriaQuery = criteriaBuilder.createQuery(Signal.class);
        Root<Signal> root = criteriaQuery.from(Signal.class);

        Predicate conditions = criteriaBuilder.and(
                criteriaBuilder.equal(root.get("isHeartbeat"), false),
                criteriaBuilder.between(root.get("timestamp"), startTime, endTime)
        );

        if (ownerId != null) {
            conditions = criteriaBuilder.and(conditions, criteriaBuilder.equal(root.get("detector").get("owner").get("id"), ownerId));
        }

        criteriaQuery.select(root).where(conditions);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}

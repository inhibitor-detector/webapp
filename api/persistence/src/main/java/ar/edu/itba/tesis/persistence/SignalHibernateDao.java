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
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

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
    @Override
    public List<Signal> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Signal> criteriaQuery = criteriaBuilder.createQuery(Signal.class);
        Root<Signal> root = criteriaQuery.from(Signal.class);

        criteriaQuery.select(root);

        TypedQuery<Signal> query = entityManager.createQuery(criteriaQuery);
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
}

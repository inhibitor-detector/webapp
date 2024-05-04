package ar.edu.itba.tesis.persistence;

import ar.edu.itba.tesis.interfaces.DetectorDao;
import ar.edu.itba.tesis.interfaces.exceptions.*;
import ar.edu.itba.tesis.models.Detector;
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
public class DetectorHibernateDao implements DetectorDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Detector create(Detector entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Detector> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Detector.class, id));

    }

    // Refactor this
    @Override
    public List<Detector> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Detector> criteriaQuery = criteriaBuilder.createQuery(Detector.class);
        Root<Detector> root = criteriaQuery.from(Detector.class);

        criteriaQuery.select(root);

        TypedQuery<Detector> query = entityManager.createQuery(criteriaQuery);
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
        entityManager.createNativeQuery("DELETE FROM users WHERE id = :id")
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
}

package ar.edu.itba.tesis.persistence;

import ar.edu.itba.tesis.interfaces.persistence.DetectorDao;
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

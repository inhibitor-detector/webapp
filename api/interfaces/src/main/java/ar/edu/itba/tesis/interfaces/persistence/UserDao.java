package ar.edu.itba.tesis.interfaces.persistence;

import ar.edu.itba.tesis.interfaces.CrudOperations;
import ar.edu.itba.tesis.models.User;

import java.util.List;
import java.util.Optional;


public interface UserDao extends CrudOperations<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findAllPaginated(Integer page, Integer maxResults);

    Optional<User> findByUsername(String username);

}

package ar.edu.itba.tesis.interfaces;

import ar.edu.itba.tesis.models.User;

import java.util.Optional;


public interface UserDao extends CrudOperations<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}

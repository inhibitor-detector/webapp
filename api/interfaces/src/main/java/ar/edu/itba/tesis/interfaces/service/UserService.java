package ar.edu.itba.tesis.interfaces.service;

import ar.edu.itba.tesis.interfaces.CrudOperations;
import ar.edu.itba.tesis.models.User;

import java.util.Optional;

public interface UserService extends CrudOperations<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}

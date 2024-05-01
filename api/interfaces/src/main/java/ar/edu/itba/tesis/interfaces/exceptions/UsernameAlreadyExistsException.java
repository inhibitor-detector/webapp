package ar.edu.itba.tesis.interfaces.exceptions;

public class UsernameAlreadyExistsException extends AlreadyExistsException {

        public UsernameAlreadyExistsException() {
            super("Username already exists");
        }
}

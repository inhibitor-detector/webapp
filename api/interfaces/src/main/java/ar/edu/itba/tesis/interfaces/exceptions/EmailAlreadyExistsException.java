package ar.edu.itba.tesis.interfaces.exceptions;

public class EmailAlreadyExistsException extends AlreadyExistsException {

    public EmailAlreadyExistsException() {
        super("Email already exists");
    }
}

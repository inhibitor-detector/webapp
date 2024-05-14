package ar.edu.itba.tesis.webapp.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationTokenException extends AuthenticationException {
    public InvalidAuthenticationTokenException(String message) {
        super(message);
    }
}

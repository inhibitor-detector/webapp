package ar.edu.itba.tesis.webapp.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

public class ExpiredAuthenticationTokenException extends AuthenticationException {
    public ExpiredAuthenticationTokenException(String message) {
        super(message);
    }
}

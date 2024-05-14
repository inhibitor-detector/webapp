package ar.edu.itba.tesis.webapp.auth;


import ar.edu.itba.tesis.webapp.auth.exceptions.ExpiredAuthenticationTokenException;
import ar.edu.itba.tesis.webapp.auth.exceptions.InvalidAuthenticationTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;

@Component
public class JwtToUserDetailsConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    private final UserDetailsService userDetailsService;
    private final UserDetailsChecker detailsChecker;

    @Autowired
    public JwtToUserDetailsConverter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.detailsChecker = new AccountStatusUserDetailsChecker();
    }

    @Override
    public UsernamePasswordAuthenticationToken convert(@NonNull Jwt source) {
        validateExpirationDate(source);
        UserDetails user = userDetailsService.loadUserByUsername(source.getSubject());
        detailsChecker.check(user);
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }
    private void validateExpirationDate(Jwt source) {
        if (source.getExpiresAt() == null) throw new InvalidAuthenticationTokenException("Jwt Token must have expiration date");
        if (source.getIssuedAt() == null) throw new InvalidAuthenticationTokenException("Jwt Token must have issue date");
    }
}

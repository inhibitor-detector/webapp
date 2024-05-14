package ar.edu.itba.tesis.webapp.auth;

import ar.edu.itba.tesis.interfaces.service.UserService;
import ar.edu.itba.tesis.models.Role;
import ar.edu.itba.tesis.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthFacade {
    private final UserService userService;

    @Autowired
    public AuthFacade(UserService userService) {
        this.userService = userService;
    }

    public UserDetails getAuthenticatedUserDetails(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            return null;
        }
        return (UserDetails) authentication.getPrincipal();
    }

    public User getAuthenticatedUser(Authentication authentication) {
        final UserDetails userDetails = getAuthenticatedUserDetails(authentication);
        if (userDetails == null) {
            return null;
        }
        return userService.findByUsername(userDetails.getUsername()).orElse(null);
    }
}

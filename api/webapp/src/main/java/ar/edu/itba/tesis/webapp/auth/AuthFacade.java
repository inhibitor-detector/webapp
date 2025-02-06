package ar.edu.itba.tesis.webapp.auth;

import ar.edu.itba.tesis.interfaces.service.DetectorService;
import ar.edu.itba.tesis.interfaces.service.UserService;
import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.Role;
import ar.edu.itba.tesis.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthFacade {
    private final UserService userService;
    private final DetectorService detectorService;

    @Autowired
    public AuthFacade(UserService userService, DetectorService detectorService) {
        this.userService = userService;
        this.detectorService = detectorService;
    }

    private UserDetails getAuthenticatedUserDetails(Authentication authentication) {
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

    public Detector getAuthenticatedDetector(Authentication authentication) {
        final UserDetails userDetails = getAuthenticatedUserDetails(authentication);
        if (userDetails == null) {
            return null;
        }
        final User user = userService.findByUsername(userDetails.getUsername()).orElse(null);

        if (user == null) {
            return null;
        }

        return detectorService.findByUserId(user.getId()).orElse(null);
    }
}

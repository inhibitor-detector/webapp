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

import java.util.function.Supplier;

/**
 * <a href="https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#_migrating_expressions">migrating_expressions</a>
 * <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/AntPathMatcher.html">AntPathMatcher</a>
 */
@Component()
public class AccessControl {

    private final UserService userService;

    @Autowired
    public AccessControl(UserService userService) {
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

    public boolean isAuthenticatedUser(Authentication authentication, Long id) {
        final User user = getAuthenticatedUser(authentication);
        return user != null && (user.getRoles().contains(Role.ADMIN) || user.getId().equals(id));
    }

    public boolean canPostSignal(Authentication authentication) {
        final User user = getAuthenticatedUser(authentication);
        return user != null && user.getRoles().contains(Role.DETECTOR);
    }

    // Method for Signal controller, checks that the detector user is the same as the detectorId in the body
    public boolean canPostSignalCheckDetectorId(Authentication authentication, Detector detector) {
        final User user = getAuthenticatedUser(authentication);
        return user != null && user.getId().equals(detector.getUser().getId());
    }

    public boolean isAdminUser(Authentication authentication) {
        final User user = getAuthenticatedUser(authentication);
        return user != null && user.getRoles().contains(Role.ADMIN);
    }
}

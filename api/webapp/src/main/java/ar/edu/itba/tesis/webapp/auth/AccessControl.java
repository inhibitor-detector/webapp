package ar.edu.itba.tesis.webapp.auth;

import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.Role;
import ar.edu.itba.tesis.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * <a href="https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#_migrating_expressions">migrating_expressions</a>
 * <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/AntPathMatcher.html">AntPathMatcher</a>
 */
@Component()
public class AccessControl {

    private final AuthFacade authFacade;

    @Autowired
    public AccessControl(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    public boolean isAuthenticatedUser(Authentication authentication, Long id) {
        final User user = authFacade.getAuthenticatedUser(authentication);
        return user != null && (user.getRoles().contains(Role.ADMIN) || user.getId().equals(id));
    }

    public boolean canPostSignal(Authentication authentication) {
        final User user = authFacade.getAuthenticatedUser(authentication);
        return user != null && user.getRoles().contains(Role.DETECTOR);
    }

    public boolean canPutSignal(Authentication authentication) {
        final User user = authFacade.getAuthenticatedUser(authentication);
        return user != null;
    }

    public boolean canPostSignalCheckDetectorId(Authentication authentication, Detector detector) {
        final User user = authFacade.getAuthenticatedUser(authentication);
        return user != null && user.getId().equals(detector.getUser().getId());
    }

    public boolean isAdminUser(Authentication authentication) {
        final User user = authFacade.getAuthenticatedUser(authentication);
        return user != null && user.getRoles().contains(Role.ADMIN);
    }

    public boolean canAccessDetectors(Authentication authentication, String queryString) {
        final User user = authFacade.getAuthenticatedUser(authentication);
        if (user == null) return false;
        if (user.getRoles().contains(Role.ADMIN)) return true;

        if (queryString == null) return false;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost?" + queryString);
        MultiValueMap<String, String> queryParams = builder.build().getQueryParams();

        if (!queryParams.containsKey("ownerId")) return false;

        try {
            Long ownerId = Long.parseLong(queryParams.get("ownerId").getFirst());
            return user.getId().equals(ownerId);
        } catch (NumberFormatException e) {
            throw new AccessDeniedException("Wrong query param format. ownerId not a number");
        }
    }
}

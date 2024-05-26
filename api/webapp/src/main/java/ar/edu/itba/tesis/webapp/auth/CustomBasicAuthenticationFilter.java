package ar.edu.itba.tesis.webapp.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.time.ZonedDateTime;

public class CustomBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtEncoder jwtEncoder;
    private final AuthFacade authFacade;

    public CustomBasicAuthenticationFilter(AuthenticationManager authenticationManager,
                                           AuthenticationEntryPoint authenticationEntryPoint,
                                           JwtEncoder jwtEncoder,
                                           AuthFacade authFacade) {
        super(authenticationManager, authenticationEntryPoint);
        this.jwtEncoder = jwtEncoder;
        this.authFacade = authFacade;
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        super.onSuccessfulAuthentication(request, response, authResult);
        final Jwt jwt = createJwt(authResult);
        addJwtToResponse(response, jwt);
    }

    private Jwt createJwt(Authentication authentication) {
        final JwsHeader header = JwsHeader.with(SignatureAlgorithm.ES256).build();
        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expiresDate = issuedDate.plusSeconds(900);

        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(issuedDate.toInstant())
                .expiresAt(expiresDate.toInstant())
                .claim("userId", authFacade.getAuthenticatedUser(authentication).getId())
                .build();

        final JwtEncoderParameters parameters = JwtEncoderParameters.from(header, claims);
        return jwtEncoder.encode(parameters);
    }

    private void addJwtToResponse(HttpServletResponse response, Jwt jwt) {
        response.addHeader("Authorization", "Bearer " + jwt.getTokenValue());
        System.out.println("Bearer " + jwt.getTokenValue());
    }
}

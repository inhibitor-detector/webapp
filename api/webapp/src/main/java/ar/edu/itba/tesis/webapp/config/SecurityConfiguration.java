package ar.edu.itba.tesis.webapp.config;

import ar.edu.itba.tesis.webapp.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.web.cors.CorsConfiguration.ALL;

/**
 * <a href="https://www.baeldung.com/spring-security-basic-authentication">spring-security-basic-authentication</a>
 * <p>
 * <a href="https://www.baeldung.com/spring-deprecated-websecurityconfigureradapter">spring-deprecated-websecurityconfigureradapter</a>
 * <p>
 * <a href="https://www.baeldung.com/role-and-privilege-for-spring-security-registration">role-and-privilege-for-spring-security-registration</a>
 */
@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.tesis.webapp.auth")
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;
    private final Converter<Jwt, UsernamePasswordAuthenticationToken> jwtToUserDetailsConverter;
    private final AccessControl accessControl;
    private final AuthFacade authFacade;

    @Autowired
    public SecurityConfiguration(UserDetailsService userDetailsService,
                                 PasswordEncoder passwordEncoder,
                                 JwtDecoder jwtDecoder,
                                 JwtEncoder jwtEncoder,
                                 Converter<Jwt, UsernamePasswordAuthenticationToken> jwtToUserDetailsConverter,
                                 AccessControl accessControl,
                                 AuthFacade authFacade) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtDecoder = jwtDecoder;
        this.jwtEncoder = jwtEncoder;
        this.jwtToUserDetailsConverter = jwtToUserDetailsConverter;
        this.accessControl = accessControl;
        this.authFacade = authFacade;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new UnauthorizedRequestHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ForbiddenRequestHandler();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider() {
        final JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
        jwtAuthenticationProvider.setJwtAuthenticationConverter(jwtToUserDetailsConverter);
        return jwtAuthenticationProvider;
    }

    /**
     * AuthenticationManager with two AuthenticationProviders:
     * - DaoAuthenticationProvider
     * - JwtAuthenticationProvider
     * <p>
     * <a href="https://www.baeldung.com/spring-security-multiple-auth-providers#1-java-configuration">https://www.baeldung.com/spring-security-multiple-auth-providers#1-java-configuration</a>
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        final AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder
                .authenticationProvider(daoAuthenticationProvider())
                .authenticationProvider(jwtAuthenticationProvider())
                .build();
    }

    @Bean
    public BasicAuthenticationFilter basicAuthenticationFilter(HttpSecurity http) throws Exception {
        final AuthenticationManager authenticationManager = authenticationManager(http);
        final AuthenticationEntryPoint authenticationEntryPoint = authenticationEntryPoint();
        return new CustomBasicAuthenticationFilter(authenticationManager, authenticationEntryPoint, jwtEncoder, authFacade);
    }

    @Bean
    public BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter(HttpSecurity http) throws Exception {
        final AuthenticationManager authenticationManager = authenticationManager(http);
        BearerTokenAuthenticationFilter filter = new BearerTokenAuthenticationFilter(authenticationManager);
        filter.setAuthenticationEntryPoint(authenticationEntryPoint());
        return filter;
    }

    /**
     * <a href="https://docs.spring.io/spring-security/reference/reactive/integrations/cors.html">Spring Security CORS</a>
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(ALL));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.addAllowedHeader(ALL);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Link", "Location", "ETag", "Jwt"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(e -> e
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )

                .csrf(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new OrRequestMatcher(
                                new AntPathRequestMatcher("/users", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher("/users", HttpMethod.GET.toString())
                        )).access((authentication, context) -> new AuthorizationDecision(accessControl.isAdminUser(authentication.get())))
                        .requestMatchers(new OrRequestMatcher(
                                new AntPathRequestMatcher("/users/{id:\\d+}/**", HttpMethod.PUT.toString()),
                                new AntPathRequestMatcher("/users/{id:\\d+}/**", HttpMethod.GET.toString()),
                                new AntPathRequestMatcher("/users/{id:\\d+}/**", HttpMethod.DELETE.toString())
                        )).access((authentication, context) -> new AuthorizationDecision(accessControl.isAuthenticatedUser(authentication.get(), Long.parseLong(context.getVariables().get("id")))))
                        .requestMatchers(new OrRequestMatcher(
                                new AntPathRequestMatcher("/signals", HttpMethod.POST.toString())
                        )).access(((authentication, context) -> new AuthorizationDecision(accessControl.canPostSignal(authentication.get()))))

                        .requestMatchers(new OrRequestMatcher(
                                new AntPathRequestMatcher("/signals", HttpMethod.GET.toString())
                        )).access(((authentication, context) -> new AuthorizationDecision(accessControl.canAccessDetectors(authentication.get(), context.getRequest().getQueryString()))))

                        .requestMatchers(new OrRequestMatcher(
                                new AntPathRequestMatcher("/detectors", HttpMethod.GET.toString())
                        )).access(((authentication, context) -> new AuthorizationDecision(accessControl.canAccessDetectors(authentication.get(), context.getRequest().getQueryString()))))

                        .requestMatchers(new OrRequestMatcher(
                                new AntPathRequestMatcher("/detectors", HttpMethod.POST.toString())
                        )).access(((authentication, context) -> new AuthorizationDecision(accessControl.isAdminUser(authentication.get()))))

                        .requestMatchers("/**").permitAll())

                .addFilter(basicAuthenticationFilter(http))
                .addFilter(bearerTokenAuthenticationFilter(http))

                .build();
    }
}

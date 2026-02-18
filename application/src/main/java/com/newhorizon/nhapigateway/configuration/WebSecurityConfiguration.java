package com.newhorizon.nhapigateway.configuration;

import com.newhorizon.nhapigateway.component.RouteHelper;
import com.newhorizon.nhapigateway.enums.RouteRetentionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
@Slf4j
public class WebSecurityConfiguration {
    private final RouteHelper routeHelper;
    private final Environment environment;

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.authorizeExchange(auth -> auth.pathMatchers("/actuator/health", "/actuator/prometheus").permitAll()
                        .pathMatchers("/actuator/**").authenticated()
                        .pathMatchers(getPublicRoutes()).permitAll()
                        .anyExchange().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    // Helper method to retrieve public routes based on the RouteHelper logic
    private String[] getPublicRoutes() {
        return Arrays.stream(HttpMethod.values())
                .flatMap(httpMethod -> {
                    List<String> routes = routeHelper.getRoutesWithRetention(httpMethod, RouteRetentionEnum.PUBLIC);
                    return nonNull(routes) && !routes.isEmpty() ? routes.stream() : null;
                })
                .toArray(String[]::new);
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        String username = environment.getProperty("SPRING_SECURITY_USER_NAME", "microservice");
        String password = environment.getProperty("SPRING_SECURITY_USER_PASSWORD");
        if (isNull(password) || Boolean.FALSE.equals(StringUtils.hasText(password))) {
            password = UUID.randomUUID().toString();
            log.warn("Generated password: {}", password);
        }
        UserDetails user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles("ACTUATOR")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

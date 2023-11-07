package com.fredyhg.destiny2jobs.security.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**"
                                , "/api/user/isEmailAvailable", "/api/user/isDiscordAvailable").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/authenticate", "/api/auth/refresh-token", "/api/user/create").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/package/close-service").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/package/create").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/package/wait-worker").hasAnyRole("ADMIN", "WORKER")
                        .requestMatchers(HttpMethod.POST, "/api/package/accept-service", "api/package/finish-service").hasAnyRole("ADMIN", "WORKER")
                        .requestMatchers(HttpMethod.GET, "/api/user/data").hasAnyRole("ADMIN", "WORKER", "USER")
                        .anyRequest().hasRole("ADMIN")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> {
                    logout.logoutUrl("/api/auth/logout")
                            .addLogoutHandler(logoutHandler)
                            .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()));
                });

        return http.build();
    }

}

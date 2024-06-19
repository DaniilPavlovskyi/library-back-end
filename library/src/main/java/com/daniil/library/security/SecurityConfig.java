package com.daniil.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT username, password, enabled FROM users WHERE username=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.username, a.authority FROM users u " +
                        "INNER JOIN authorities ua ON u.username = ua.username " +
                        "INNER JOIN authority a ON ua.authority = a.id " +
                        "WHERE u.username=?");
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        c -> c
                                .requestMatchers(HttpMethod.GET, "api/books", "api/books/*", "/api/author/*").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/account").hasRole("USER")
                                .requestMatchers(HttpMethod.POST, "api/login", "api/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "api/create-book").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "api/new-loan/*").hasRole("USER")
                                .requestMatchers(HttpMethod.PUT, "api/update-account").hasRole("USER")
                                .requestMatchers(HttpMethod.PUT, "api/update-book").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "api/delete-book/*").hasRole("ADMIN")
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/access-denied")
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

}
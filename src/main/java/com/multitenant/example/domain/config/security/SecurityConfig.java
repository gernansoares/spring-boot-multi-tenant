package com.multitenant.example.domain.config.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Defines security configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationManagerImpl authenticationManagerImpl;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(exceptionHandlingSpec ->
                        exceptionHandlingSpec
                                .authenticationEntryPoint((request, response, accessDeniedException) -> {
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                                })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                                }))

                .csrf(csrf -> csrf.disable())
                .formLogin(formLoginSpec -> formLoginSpec.disable())
                .httpBasic(httpBasicSpec -> httpBasicSpec.disable())

                .authenticationManager(authenticationManagerImpl)
                .securityContext(securityContext -> securityContext.securityContextRepository(securityContextRepository))
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/newuser").permitAll()
                                .requestMatchers("/auth/login").permitAll()
                                .requestMatchers("/testuser/**").authenticated()
                                .requestMatchers("/logout/logout").authenticated()
                                .anyRequest().denyAll());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

}
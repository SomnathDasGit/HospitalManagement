package com.example.HospitalManagement.config;

import com.example.HospitalManagement.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SequrityConfig {

    private final CustomUserDetailsService userDetailsService;

    // Configure URL access, login, and logout
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // public URLs
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
                        // admin URLs
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // patient URLs
                        .requestMatchers("/patient/**").hasRole("PATIENT")
                        // any other request must be authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")  // custom login page
                        .successHandler(customAuthSuccessHandler()) // redirect based on role
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // redirect after logout
                        .permitAll()
                );

        return http.build();
    }

    // AuthenticationManager bean with custom UserDetailsService and BCrypt password encoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return auth.build();
    }

    // BCrypt password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Custom success handler to redirect users based on their role
    @Bean
    public AuthenticationSuccessHandler customAuthSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isPatient = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"));

            if (isAdmin) {
                response.sendRedirect("/admin/dashboard");
            } else if (isPatient) {
                response.sendRedirect("/patient/dashboard");
            } else {
                response.sendRedirect("/"); // fallback
            }
        };
    }
}

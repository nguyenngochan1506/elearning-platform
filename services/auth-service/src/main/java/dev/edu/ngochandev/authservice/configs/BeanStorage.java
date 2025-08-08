package dev.edu.ngochandev.authservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;

@Configuration
public class BeanStorage {
    @Bean
    public String[] publicEndpoints() {
        return new String[] {
            "/api/auth/register",
            "/api/auth/authenticate",
            "/api/auth/reset-password",
            "/api/auth/forgot-password",
            "/api/auth/verify-email",
            "/swagger-ui/**",
            "/v3/api-docs/**",
        };
    }

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}

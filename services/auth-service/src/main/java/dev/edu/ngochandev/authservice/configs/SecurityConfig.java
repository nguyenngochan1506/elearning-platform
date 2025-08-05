package dev.edu.ngochandev.authservice.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.authservice.commons.Translator;
import dev.edu.ngochandev.authservice.dtos.res.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomJwtDecoder jwtDecoder;

    private final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/register",
            "/api/auth/authenticate",
            "/api/auth/change-password",
            "/api/auth/refresh-token",
            "/api/auth/logout",
            "/api/auth/reset-password",
            "/api/auth/forgot-password",
            "/api/auth/verify-email",

            "/api/v1/permissions/list",
            "/api/v1/roles/list",
            "/api/v1/roles",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 ->{
                    oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder))
                            .authenticationEntryPoint(authenticationEntryPoint());
                })
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            String message = authException.getMessage();
            if(message.contains("Jwt expired")){
                message = Translator.translate("error.token.expired");
            } else {
                message = Translator.translate("error.token.invalid");
            }
            ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, message, null );
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        };
    }
}

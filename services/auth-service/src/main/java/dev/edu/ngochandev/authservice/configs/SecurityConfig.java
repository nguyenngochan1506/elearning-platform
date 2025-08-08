package dev.edu.ngochandev.authservice.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.authservice.commons.Translator;
import dev.edu.ngochandev.authservice.dtos.res.ErrorResponseDto;
import dev.edu.ngochandev.authservice.exceptions.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.util.AntPathMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomJwtDecoder jwtDecoder;
	private final PermissionFilter permissionFilter;
	private final String[] publicEndpoints;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth ->{
					auth.requestMatchers(publicEndpoints).permitAll()
							.anyRequest().authenticated();
				})
				.exceptionHandling(ex ->{
					ex.authenticationEntryPoint(authenticationEntryPoint());
					ex.accessDeniedHandler(customAccessDeniedHandler);
				})
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.httpBasic(AbstractHttpConfigurer::disable)
				.oauth2ResourceServer(oauth2 ->{
					oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)
									.jwtAuthenticationConverter(this.jwtAuthenticationConverter())
							);
				}).addFilterAfter(permissionFilter, AuthorizationFilter.class)
		;
		return http.build();
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

		return jwtAuthenticationConverter;
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

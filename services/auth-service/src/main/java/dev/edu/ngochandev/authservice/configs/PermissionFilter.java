package dev.edu.ngochandev.authservice.configs;

import dev.edu.ngochandev.authservice.entities.PermissionEntity;
import dev.edu.ngochandev.authservice.repositories.PermissionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class PermissionFilter extends OncePerRequestFilter {
	private final PermissionRepository permissionRepository;
	private final AntPathMatcher antPathMatcher;
	private final String[] publicEndpoints;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String requestMethod = request.getMethod();
		if(isPublicEndpoint(requestURI)){
			filterChain.doFilter(request, response);
			return;
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			doFilter(request, response, filterChain);
			return;
		}

		if (!hasPermission(authentication, requestURI, requestMethod)) {
			throw new AccessDeniedException("");
		}
		filterChain.doFilter(request, response);
	}

	private boolean hasPermission(Authentication authentication, String requestURI, String requestMethod) {
		Set<String> userRoles = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
		List<PermissionEntity> permissions = permissionRepository.findAllByRoleNames(userRoles);
		return permissions.stream()
				.anyMatch(per -> antPathMatcher.match(per.getApiPath(), requestURI) && per.getMethod().name().equalsIgnoreCase(requestMethod));
	}

	private boolean isPublicEndpoint(String requestURI) {
		if (publicEndpoints == null || publicEndpoints.length == 0) {
			return false;
		}
		return Arrays.stream(publicEndpoints)
				.anyMatch(endpoint -> antPathMatcher.match(endpoint, requestURI));
	}
}

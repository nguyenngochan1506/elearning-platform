package dev.edu.ngochandev.socialservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalApiAuthFilter extends OncePerRequestFilter {
    @Value("${app.security.internal-secret-key}")
    private String expectedSecret;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String actualSecret = request.getHeader("X-Internal-Secret");
        String userIdStr = request.getHeader("X-User-Id");
        if (!StringUtils.hasLength(actualSecret) || !StringUtils.hasLength(userIdStr) || !actualSecret.equals(expectedSecret)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied: Invalid or missing internal secret.");
            return;
        }
        Long userId = Long.valueOf(userIdStr);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null,null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}

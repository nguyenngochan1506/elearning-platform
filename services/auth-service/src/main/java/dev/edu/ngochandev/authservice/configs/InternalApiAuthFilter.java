package dev.edu.ngochandev.authservice.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.common.dtos.res.ErrorResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class InternalApiAuthFilter extends OncePerRequestFilter {
    @Value("${app.security.internal-secret-key}")
    private String internalSecret;
    private final ObjectMapper objectMapper;
    private final Translator translator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if(!requestURI.contains("internal")){
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("X-Internal-Secret");

        if (authHeader == null || !authHeader.equals(internalSecret)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ErrorResponseDto errorResponse = new ErrorResponseDto();
            errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            errorResponse.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
            errorResponse.setMessage(translator.translate("error.unauthorized"));
            errorResponse.setPath(requestURI);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }
        filterChain.doFilter(request, response);
    }
}

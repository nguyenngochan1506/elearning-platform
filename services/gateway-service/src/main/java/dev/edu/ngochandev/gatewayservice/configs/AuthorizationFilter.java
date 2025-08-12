package dev.edu.ngochandev.gatewayservice.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.common.dtos.res.ErrorResponseDto;
import dev.edu.ngochandev.common.dtos.res.IntrospectTokenResponseDto;
import dev.edu.ngochandev.gatewayservice.commons.GatewayConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter implements GlobalFilter, Ordered {
    private final ObjectMapper objMapper;
    private final AntPathMatcher antPathMatcher;
    private final SecurityProperties securityProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(isPublicEndpoint(exchange.getRequest())){
            return chain.filter(exchange);
        }

        IntrospectTokenResponseDto introspectToken = exchange.getAttribute(GatewayConstants.INTROSPECTION_RESULT_ATTRIBUTE);
        if(introspectToken == null || !introspectToken.isActive()) {
            return handleAccessDenied(exchange);
        }
        String requestURI = exchange.getRequest().getURI().getPath();
        String requestMethod = exchange.getRequest().getMethod().name();
        if(!hasPermission(introspectToken, requestURI, requestMethod)) {
            return handleAccessDenied(exchange);
        }
        return chain.filter(exchange);
    }

    public boolean isPublicEndpoint(ServerHttpRequest req){
        return securityProperties.getPublicEndpoints().stream()
                .anyMatch(endpoint -> req.getURI().getPath().matches(endpoint));
    }

    private boolean hasPermission(IntrospectTokenResponseDto introspectToken, String requestURI, String requestMethod) {
        Set<String> permissions = introspectToken.getPermissions();
        return permissions.stream()
                .anyMatch(p -> {
                    String[] parts = p.split(":");
                    String method = parts[0];
                    String path = parts[1];

                    return antPathMatcher.match(path, requestURI) && method.equalsIgnoreCase(requestMethod);
                });
    }

    private Mono<Void> handleAccessDenied(ServerWebExchange exchange) {
        ServerHttpResponse res = exchange.getResponse();
        ServerHttpRequest req = exchange.getRequest();
        String path = req.getURI().getPath();

        ErrorResponseDto error = new ErrorResponseDto(HttpStatus.FORBIDDEN, "Acess Denied", null);
        error.setPath(path);

        try{
            String errorResponse = objMapper.writeValueAsString(error);
            res.setStatusCode(HttpStatus.FORBIDDEN);
            res.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return res.writeWith(Mono.just(res.bufferFactory().wrap(errorResponse.getBytes())));
        }catch (Exception e){
            log.error("Error while writing error response: {}", e.getMessage());
        }
        return res.writeWith(Mono.just(res.bufferFactory().wrap("Access Denied".getBytes())));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

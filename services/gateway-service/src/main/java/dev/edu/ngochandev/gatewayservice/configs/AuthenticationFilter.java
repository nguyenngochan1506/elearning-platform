package dev.edu.ngochandev.gatewayservice.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.common.dtos.res.ErrorResponseDto;
import dev.edu.ngochandev.gatewayservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private final AuthService authService;
    private final ObjectMapper mapper;

    private static final String[] publicEndpoints = {
            "/api/auth/register",
            "/api/auth/authenticate",
            "/api/auth/reset-password",
            "/api/auth/forgot-password",
            "/api/auth/verify-email",
            "/swagger-ui/.*",
            "/v3/api-docs/.*",
            "/api/auth/verify-token"
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(isPublicEndpoint(exchange.getRequest())){
            return chain.filter(exchange);
        }

        //get token from the request header
        List<String> bearerToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(CollectionUtils.isEmpty(bearerToken)) {
            return handleUnauthorized(exchange);
        }

        //verify token
        String token = bearerToken.get(0).replace("Bearer ", "");
        return authService.verifyToken(token)
                .flatMap(res->{
                    if(Boolean.TRUE.equals(res.getData())){
                        return chain.filter(exchange);
                    }else {
                        return handleUnauthorized(exchange);
                    }
                })
                .onErrorResume(throwable -> handleUnauthorized(exchange));
    }

    @Override
    public int getOrder() {
        return -1; // Set the order of this filter to be executed first
    }

    Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
        ServerHttpResponse res = exchange.getResponse();
        ServerHttpRequest req = exchange.getRequest();
        String path = req.getURI().getPath();

        ErrorResponseDto error = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, "Unauthorized", null);
        error.setPath(path);
        res.setStatusCode(HttpStatus.UNAUTHORIZED);

        try {
            String errorResponse = mapper.writeValueAsString(error);
            res.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            return res.writeWith(Mono.just(res.bufferFactory().wrap(errorResponse.getBytes())));
        } catch (Exception e) {
            log.error("Error serializing error response", e);
        }
        return res.writeWith(Mono.just(res.bufferFactory().wrap("Unauthorized".getBytes())));
    }

    public boolean isPublicEndpoint(ServerHttpRequest req){
        return Arrays.stream(publicEndpoints)
                .anyMatch(endpoint -> req.getURI().getPath().matches(endpoint));
    }
}

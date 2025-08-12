package dev.edu.ngochandev.gatewayservice.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.common.dtos.res.ErrorResponseDto;
import dev.edu.ngochandev.common.dtos.res.IntrospectTokenResponseDto;
import dev.edu.ngochandev.gatewayservice.commons.GatewayConstants;
import dev.edu.ngochandev.gatewayservice.commons.Translator;
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

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private final AuthService authService;
    private final ObjectMapper mapper;
    private final SecurityProperties securityProperties;
    private final Translator translator;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(isPublicEndpoint(exchange.getRequest())){
            return chain.filter(exchange);
        }

        //get token from the request header
        List<String> bearerToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(CollectionUtils.isEmpty(bearerToken) || !bearerToken.get(0).startsWith("Bearer ")) {
            return handleUnauthorized(exchange, "error.token.invalid");
        }
        //verify token
        String token = bearerToken.get(0).replace("Bearer ", "");
        return authService.verifyToken(token)
                .flatMap(res->{
                    if(!res.getData().isActive()){
                        return handleUnauthorized(exchange, "error.unauthorized");
                    }else {
                        IntrospectTokenResponseDto introspectToken = res.getData();
                        ServerHttpRequest modifiReq = exchange.getRequest()
                                .mutate()
                                .header("X-User-Id", String.valueOf(introspectToken.getUserId()))
                                .header("X-User-Roles", String.join(",", introspectToken.getRoles()))
                                .build();

                        ServerWebExchange modifiedExchange = exchange.mutate().request(modifiReq).build();
                        modifiedExchange.getAttributes().put(GatewayConstants.INTROSPECTION_RESULT_ATTRIBUTE, introspectToken);

                        return chain.filter(modifiedExchange);
                    }
                })
                .onErrorResume(throwable -> handleUnauthorized(exchange, "error.unauthorized"));
    }

    @Override
    public int getOrder() {
        return -2;
    }

    Mono<Void> handleUnauthorized(ServerWebExchange exchange, String messageCode) {
        ServerHttpResponse res = exchange.getResponse();
        ServerHttpRequest req = exchange.getRequest();
        String path = req.getURI().getPath();

        ErrorResponseDto error = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, translator.translate(exchange,messageCode ), null);
        error.setPath(path);
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
        return securityProperties.getPublicEndpoints().stream()
                .anyMatch(endpoint -> req.getURI().getPath().matches(endpoint));
    }
}

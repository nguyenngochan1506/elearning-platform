package dev.edu.ngochandev.gatewayservice.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.common.dtos.res.ErrorResponseDto;
import dev.edu.ngochandev.common.dtos.res.IntrospectTokenResponseDto;
import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.gatewayservice.commons.GatewayConstants;
import dev.edu.ngochandev.gatewayservice.commons.Translator;
import dev.edu.ngochandev.gatewayservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${app.security.internal-secret-key}")
    private String internalSecretKey;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isPublicEndpoint(request)) {
            return chain.filter(exchange);
        }

        String host = request.getHeaders().getFirst(HttpHeaders.HOST);
        String orgSlug = getSlugFromHost(host);
        if (orgSlug == null) {
            return handleUnauthorized(exchange, "error.organization.invalid-host");
        }

        List<String> bearerToken = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(bearerToken) || !bearerToken.get(0).startsWith("Bearer ")) {
            return handleUnauthorized(exchange, "error.token.invalid");
        }
        String token = bearerToken.get(0).replace("Bearer ", "");

        Mono<Long> orgIdMono = authService.getOrganizationIdBySlug(orgSlug);
        Mono<IntrospectTokenResponseDto> tokenMono = authService.verifyToken(token).map(SuccessResponseDto::getData);

        return Mono.zip(orgIdMono, tokenMono)
                .flatMap(tuple -> {
                    Long organizationId = tuple.getT1();
                    IntrospectTokenResponseDto introspectToken = tuple.getT2();

                    if (!introspectToken.isActive()) {
                        return handleUnauthorized(exchange, "error.unauthorized");
                    }

                    ServerHttpRequest modifiedReq = request.mutate()
                            .header("X-Organization-Id", String.valueOf(organizationId))
                            .header("X-Internal-Secret", internalSecretKey)
                            .build();

                    ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedReq).build();
                    modifiedExchange.getAttributes().put(GatewayConstants.INTROSPECTION_RESULT_ATTRIBUTE, introspectToken);

                    return chain.filter(modifiedExchange);
                })
                .onErrorResume(throwable -> {
                    log.error("Error during authentication/organization check: {}", throwable.getMessage());
                    return handleUnauthorized(exchange, "error.unauthorized");
                });
    }

    private String getSlugFromHost(String host) {
        if (host == null || host.isEmpty()) {
            return null;
        }
        if (host.contains(":")) {
            host = host.substring(0, host.indexOf(":"));
        }
        if (host.equals("localhost")) {
            return "system";
        }
        if (host.contains(".")) {
            return host.split("\\.")[0];
        }
        return null;
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

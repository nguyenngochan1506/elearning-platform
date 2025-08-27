package dev.edu.ngochandev.gatewayservice.repositories;

import dev.edu.ngochandev.common.dtos.res.IntrospectTokenResponseDto;
import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.gatewayservice.dtos.req.AuthVerifyTokenRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface AuthClient {

    @PostExchange(value = "/api/auth/internal/verify-token", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponseDto<IntrospectTokenResponseDto>> verifyToken(@RequestBody @Valid AuthVerifyTokenRequestDto req);

    @GetExchange("/api/internal/organizations/slug/{slug}")
    Mono<SuccessResponseDto<Long>> getOrganizationIdBySlug(@PathVariable("slug") String slug);
}

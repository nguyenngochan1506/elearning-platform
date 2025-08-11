package dev.edu.ngochandev.gatewayservice.services;

import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<SuccessResponseDto<Boolean>> verifyToken(String token);
}

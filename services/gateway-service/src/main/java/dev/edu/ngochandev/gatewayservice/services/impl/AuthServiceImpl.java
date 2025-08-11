package dev.edu.ngochandev.gatewayservice.services.impl;

import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.gatewayservice.dtos.req.AuthVerifyTokenRequestDto;
import dev.edu.ngochandev.gatewayservice.repositories.AuthClient;
import dev.edu.ngochandev.gatewayservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthClient authClient;
    @Override
    public Mono<SuccessResponseDto<Boolean>> verifyToken(String token) {
        AuthVerifyTokenRequestDto req = new AuthVerifyTokenRequestDto();
        req.setToken(token);
        return authClient.verifyToken(req);
    }
}

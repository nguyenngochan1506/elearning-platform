package dev.edu.ngochandev.authservice.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.enums.TokenType;

public interface JwtService {
    String generateToken(UserEntity user, TokenType type) throws JOSEException;

    boolean validateToken(String token, String username);

    String extractUsername(String token);

    String refreshToken(String token);
}

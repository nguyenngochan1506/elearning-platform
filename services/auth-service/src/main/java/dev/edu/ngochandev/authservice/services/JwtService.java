package dev.edu.ngochandev.authservice.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.enums.TokenType;

import java.text.ParseException;
import java.util.Date;

public interface JwtService {
    String generateToken(UserEntity user, TokenType type) throws JOSEException;

    boolean validateToken(String token, TokenType type) throws JOSEException, ParseException;

    String extractUsername(String token) throws ParseException;

    String extractJti(String token) throws ParseException;

    Date extractExpiration(String token) throws ParseException;
}

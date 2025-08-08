package dev.edu.ngochandev.authservice.services;

import com.nimbusds.jose.JOSEException;
import dev.edu.ngochandev.authservice.commons.enums.TokenType;
import dev.edu.ngochandev.authservice.entities.InvalidatedTokenEntity;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import java.text.ParseException;
import java.util.Date;

public interface JwtService {
    String generateToken(UserEntity user, TokenType type) throws JOSEException;

    boolean validateToken(String token, TokenType type) throws JOSEException, ParseException;

    String extractUsername(String token) throws ParseException;

    String extractJti(String token) throws ParseException;

    Date extractExpiration(String token) throws ParseException;

    String disableToken(InvalidatedTokenEntity invalidatedToken);
}

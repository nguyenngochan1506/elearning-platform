package dev.edu.ngochandev.authservice.services.impl;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dev.edu.ngochandev.authservice.entities.InvalidatedTokenEntity;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.commons.enums.TokenType;
import dev.edu.ngochandev.authservice.repositories.InvalidatedTokenRepository;
import dev.edu.ngochandev.authservice.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j(topic = "JWT-SERVICE")
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.access-token-secret}")
    private String accessSecretKey;

    @Value("${jwt.forgot-password-token-secret}")
    private String forgotPasswordSecretKey;

    @Value("${jwt.email-verification-token-secret}")
    private String emailSecretKey;

    @Value("${jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${jwt.forgot-password-expiration}")
    private Long forgotPasswordExpiration;

    @Value("${jwt.refresh-token-secret}")
    private String refreshSecretKey;

    @Value("${jwt.email-verification-expiration}")
    private Long emailVerificationExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Value("${jwt.issuer}")
    private String issuer;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Override
    public String generateToken(UserEntity user, TokenType type) throws JOSEException {
        return switch (type){
            case ACCESS_TOKEN -> this.generateToken(user, accessSecretKey, accessExpiration);
            case REFRESH_TOKEN -> this.generateToken(user, refreshSecretKey, refreshExpiration);
            case FORGOT_PASSWORD_TOKEN -> this.generateToken(user, forgotPasswordSecretKey, forgotPasswordExpiration);
            case EMAIL_VERIFICATION_TOKEN -> this.generateToken(user, emailSecretKey, emailVerificationExpiration);
        };
    }

    private String generateToken(UserEntity user, String secretKey, Long expirationTime) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        System.out.println(secretKey);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(expirationTime, ChronoUnit.MINUTES).toEpochMilli()))
                .subject(user.getUsername())
                .issuer(issuer)
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(new MACSigner(secretKey));
        return jwsObject.serialize();
    }

    @Override
    public boolean validateToken(String token, TokenType type) throws JOSEException, ParseException {
        byte[] secretKey = switch (type) {
            case ACCESS_TOKEN -> accessSecretKey.getBytes();
            case REFRESH_TOKEN -> refreshSecretKey.getBytes();
            case FORGOT_PASSWORD_TOKEN -> forgotPasswordSecretKey.getBytes();
            case EMAIL_VERIFICATION_TOKEN -> emailSecretKey.getBytes();
        };

        JWSVerifier verifier = new MACVerifier(secretKey);
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if(!signedJWT.verify(verifier) || expirationTime.before(new Date())){
            return false;
        }
        return !invalidatedTokenRepository.existsById(this.extractJti(token));
    }

    @Override
    public String extractUsername(String token) throws ParseException {
        return  SignedJWT.parse(token).getJWTClaimsSet().getSubject();
    }

    @Override
    public String extractJti(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getJWTID();
    }

    @Override
    public Date extractExpiration(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime();
    }

    @Override
    public String disableToken(InvalidatedTokenEntity invalidatedToken) {
        return invalidatedTokenRepository.save(invalidatedToken).getId();
    }

}

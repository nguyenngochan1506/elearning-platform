package dev.edu.ngochandev.authservice.services.impl;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.enums.TokenType;
import dev.edu.ngochandev.authservice.repositories.InvalidatedTokenRepository;
import dev.edu.ngochandev.authservice.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j(topic = "JWT-SERVICE")
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.accessTokenSecretKey}")
    private String accessSecretKey;
    @Value("${jwt.forgotPasswordTokenSecretKey}")
    private String forgotPasswordSecretKey;
    @Value("${jwt.accessExpiration}")
    private Long accessExpiration;
    @Value("${jwt.forgotPasswordExpiration}")
    private Long forgotPasswordExpiration;
    @Value("${jwt.issuer}")
    private String issuer;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Override
    public String generateToken(UserEntity user, TokenType type) throws JOSEException {
        return switch (type){
            case ACCESS_TOKEN -> this.generateToken(user, accessSecretKey, accessExpiration);
            case FORGOT_PASSWORD_TOKEN -> this.generateToken(user, forgotPasswordSecretKey, forgotPasswordExpiration);
        };
    }

    private String generateToken(UserEntity user, String secretKey, Long expirationTime) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
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
            case FORGOT_PASSWORD_TOKEN -> forgotPasswordSecretKey.getBytes();
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

}

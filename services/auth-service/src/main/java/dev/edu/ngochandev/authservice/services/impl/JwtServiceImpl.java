package dev.edu.ngochandev.authservice.services.impl;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.enums.TokenType;
import dev.edu.ngochandev.authservice.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.accessTokenSecretKey}")
    private String accessSecretKey;
    @Value("${jwt.refreshTokenSecretKey}")
    private String refreshSecretKey;
    @Value("${jwt.accessExpiration}")
    private Long accessExpiration;
    @Value("${jwt.refreshExpiration}")
    private Long refreshExpiration;
    @Value("${jwt.issuer}")
    private String issuer;

    @Override
    public String generateToken(UserEntity user, TokenType type) throws JOSEException {
        return switch (type){
            case ACCESS_TOKEN -> this.generateToken(user, accessSecretKey, accessExpiration);
            case REFRESH_TOKEN -> this.generateToken(user, refreshSecretKey, refreshExpiration);
        };
    }

    private String generateToken(UserEntity user, String secretKey, Long expirationTime) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
                .subject(user.getUsername())
                .issuer(issuer)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(new MACSigner(secretKey));
        return jwsObject.serialize();
    }

    @Override
    public boolean validateToken(String token, String username) {
        return false;
    }

    @Override
    public String extractUsername(String token) {
        return "";
    }

    @Override
    public String refreshToken(String token) {
        return "";
    }
}

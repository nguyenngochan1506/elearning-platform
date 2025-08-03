package dev.edu.ngochandev.authservice.configs;

import com.nimbusds.jose.JWSAlgorithm;
import dev.edu.ngochandev.authservice.repositories.InvalidatedTokenRepository;
import dev.edu.ngochandev.authservice.services.JwtService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.accessTokenSecretKey}")
    private String jwtSecretKey;

    private final JwtService jwtService;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) {
        Jwt jwtDecoder;
        jwtDecoder = nimbusJwtDecoder.decode(token); // check (expired, malformed, bad signature)

        try {
            boolean isExists = invalidatedTokenRepository.existsById(jwtService.extractJti(token));
            if(isExists){
                throw new BadJwtException("error.token.invalid"); //check token logout
            }
            return jwtDecoder;
        }catch (ParseException e){
            throw new BadJwtException("error.token.invalid");
        }
    }

    @PostConstruct
    public void init() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(jwtSecretKey.getBytes(), JWSAlgorithm.HS256.getName());
        nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}

package dev.edu.ngochandev.authservice.configs;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.SignedJWT;
import dev.edu.ngochandev.authservice.repositories.InvalidatedTokenRepository;
import dev.edu.ngochandev.authservice.services.JwtService;
import jakarta.annotation.PostConstruct;
import java.text.ParseException;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return new Jwt(token, signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(), signedJWT.getJWTClaimsSet().getClaims());
        } catch (ParseException e) {
            throw new BadJwtException("error.token.invalid");
        }
    }
}

package dev.edu.ngochandev.authservice.configs;

import java.util.Objects;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditorAwareConfig implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(auth -> auth.isAuthenticated() && auth instanceof JwtAuthenticationToken)
                .map(JwtAuthenticationToken.class::cast)
                .map(JwtAuthenticationToken::getTokenAttributes)
                .map(attrs -> attrs.get("userId"))
                .map(userId -> {
                    if (userId instanceof Number number) {
                        return number.longValue();
                    } else if (userId instanceof String str) {
                        return Long.valueOf(str);
                    }
                    return -1L;
                });
    }
}

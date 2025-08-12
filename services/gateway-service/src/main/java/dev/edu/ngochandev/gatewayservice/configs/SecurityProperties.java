package dev.edu.ngochandev.gatewayservice.configs;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "app.security")
@Getter
@Setter
@Validated
public class SecurityProperties {
    @NotEmpty
    private List<String> publicEndpoints = new ArrayList<>();
}

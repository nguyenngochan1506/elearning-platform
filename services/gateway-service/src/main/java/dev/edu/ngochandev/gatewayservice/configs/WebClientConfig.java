package dev.edu.ngochandev.gatewayservice.configs;

import dev.edu.ngochandev.gatewayservice.repositories.AuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    @Value("${app.security.internal-secret-key}")
    private String internalSecretKey;

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader("X-Internal-Secret", internalSecretKey)
                .build();
    }

    @Bean
    AuthClient authClient() {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(WebClientAdapter.create(webClient()))
                .build();
        return factory.createClient(AuthClient.class);
    }
}

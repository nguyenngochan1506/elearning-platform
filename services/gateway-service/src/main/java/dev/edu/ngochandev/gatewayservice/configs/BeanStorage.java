package dev.edu.ngochandev.gatewayservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.AntPathMatcher;

@Configuration
public class BeanStorage {
    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages_gateway", "messages_common");
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}

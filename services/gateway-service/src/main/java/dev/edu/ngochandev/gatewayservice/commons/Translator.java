package dev.edu.ngochandev.gatewayservice.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor

public class Translator {
    private final MessageSource messageSource;
    public String translate(ServerWebExchange exchange, String code) {
        Locale locale = resolveLocale(exchange);

        return messageSource.getMessage(code, null, code, locale);
    }
    private Locale resolveLocale(ServerWebExchange exchange) {
        List<String> langHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.ACCEPT_LANGUAGE);

        if (CollectionUtils.isEmpty(langHeaders) || !StringUtils.hasText(langHeaders.get(0))) {
            return Locale.getDefault();
        }

        List<Locale.LanguageRange> ranges = Locale.LanguageRange.parse(langHeaders.get(0));
        if (ranges.isEmpty()) {
            return Locale.getDefault();
        }

        return Locale.forLanguageTag(ranges.get(0).getRange());
    }
}

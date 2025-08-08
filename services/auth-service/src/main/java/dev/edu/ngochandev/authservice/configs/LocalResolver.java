package dev.edu.ngochandev.authservice.configs;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class LocalResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String language = request.getHeader("Accept-Language");
		List<Locale> listLocale = List.of(new Locale("en", "US"), new Locale("vi", "VN"));
		return StringUtils.hasLength(language) ? Locale.lookup(Locale.LanguageRange.parse(language), listLocale) : Locale.getDefault();
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(3600);
		return messageSource;
	}
}

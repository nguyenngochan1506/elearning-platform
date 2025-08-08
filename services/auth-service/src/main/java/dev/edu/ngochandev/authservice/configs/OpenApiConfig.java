package dev.edu.ngochandev.authservice.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
	private static final String SECURITY_SCHEME_NAME = "bearerAuth";
	private static final String LICENSE_NAME = "Apache 2.0";
	private static final String LICENSE_URL = "https://springdoc.org";
	private static final String API_DESCRIPTION = "API documents for Auth service";

	@Value("${openapi.service.title}")
	private String title;

	@Value("${openapi.service.version}")
	private String version;

	@Value("${openapi.service.server}")
	private String serverUrl;

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.servers(createServers())
				.components(createComponents())
				.security(createSecurityRequirements())
				.info(createApiInfo());
	}
	private List<Server> createServers() {
		return List.of(new Server().url(serverUrl));
	}
	private Components createComponents() {
		Components components = new Components();
		components.addSecuritySchemes(SECURITY_SCHEME_NAME, createSecurityScheme());
		return components;

	}
	private SecurityScheme createSecurityScheme() {
		return new SecurityScheme()
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT");
	}
	private List<SecurityRequirement> createSecurityRequirements() {
		return List.of(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
	}
	private Info createApiInfo() {
		return new Info()
				.title(title)
				.description(API_DESCRIPTION)
				.version(version)
				.license(createLicense());
	}
	private License createLicense() {
		return new License()
				.name(LICENSE_NAME)
				.url(LICENSE_URL);
	}
}

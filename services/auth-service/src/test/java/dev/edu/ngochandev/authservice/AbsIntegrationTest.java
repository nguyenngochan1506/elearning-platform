package dev.edu.ngochandev.authservice;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AbsIntegrationTest {
	@Container
	static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER ;

	static {
		POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:16-alpine");
		POSTGRES_SQL_CONTAINER.start();
	}
	@DynamicPropertySource
	static void configureDataSource(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
	}
}

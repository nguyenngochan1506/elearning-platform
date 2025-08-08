package dev.edu.ngochandev.authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.authservice.commons.Translator;
import dev.edu.ngochandev.authservice.dtos.req.UserRegisterRequestDto;
import dev.edu.ngochandev.authservice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class UserIntegrationTest {
    @Container
    static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:16-alpine");
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;


    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES_SQL_CONTAINER::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }
    @Test
    void testUserCreationSuccess() throws Exception {
        log.info("Starting testUserCreationSuccess");
        UserRegisterRequestDto req = new UserRegisterRequestDto();
        req.setEmail("test@test.com");
        req.setPassword("password123");
        req.setFullName("Test User");
        req.setUsername("testuser");
        // Convert the request DTO to JSON
        String requestJson = objectMapper.writeValueAsString(req);
        log.info("Request JSON: {}", requestJson);
        // Perform the request to create a user
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value(Translator.translate("user.register.success")));

        log.info("Response: {}", response.andReturn().getResponse().getContentAsString());
        log.info("Ending testUserCreationSuccess");
    }
}

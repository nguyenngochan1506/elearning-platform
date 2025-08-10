package dev.edu.ngochandev.authservice;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.authservice.commons.DataInitializer;
import dev.edu.ngochandev.authservice.commons.Translator;
import dev.edu.ngochandev.authservice.dtos.req.*;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.entities.UserRoleEntity;
import dev.edu.ngochandev.authservice.repositories.InvalidatedTokenRepository;
import dev.edu.ngochandev.authservice.repositories.RoleRepository;
import dev.edu.ngochandev.authservice.repositories.UserRepository;
import dev.edu.ngochandev.authservice.repositories.UserRoleRepository;
import dev.edu.ngochandev.authservice.services.JwtService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@Slf4j(topic = "USER-INTEGRATION-TEST")
class AuthIntegrationTest extends AbsIntegrationTest {
    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Autowired
    public AuthIntegrationTest(
            ObjectMapper objectMapper,
            MockMvc mockMvc,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            InvalidatedTokenRepository invalidatedTokenRepository) {
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        log.info("Database cleaned up before test.");
    }

    // =================================================================
    // == TEST CASES FOR /api/auth/register
    // =================================================================
    @Test
    void registerNewUser_validData_success(TestInfo testInfo) throws Exception {
        log.info("============Starting test: {} ============", testInfo.getDisplayName());
        // Arrange
        log.info("[ARRANGE] Preparing request data for user registration");
        UserRegisterRequestDto req = new UserRegisterRequestDto();
        req.setEmail("test@test.com");
        req.setPassword("password123");
        req.setFullName("Test User");
        req.setUsername("testuser");
        String requestJson = objectMapper.writeValueAsString(req);
        log.debug("[ARRANGE] Request JSON: {}", requestJson);
        // Act
        log.info("[Act] Performing POST request to /api/auth/register.");
        ResultActions response = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson));
        // Assert
        log.info("[Assert] Validating response status and content.");
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(Translator.translate("user.register.success")));
        log.debug(
                "[Assert] Response content: {}",
                response.andReturn().getResponse().getContentAsString());

        log.info("============Test {} completed successfully. ============", testInfo.getDisplayName());
    }

    @Test
    void whenRegister_withDuplicateEmail_thenReturns409Conflict(TestInfo testInfo) throws Exception {
        log.info("==========> STARTING TEST: {} <==========", testInfo.getDisplayName());

        // Arrange create a user with an existing email
        userRepository.save(UserEntity.builder()
                .username("existing")
                .email("duplicate.email@example.com")
                .password("pwd")
                .fullName("name")
                .build());

        UserRegisterRequestDto req = new UserRegisterRequestDto();
        req.setUsername("newuser");
        req.setFullName("New User");
        req.setEmail("duplicate.email@example.com");
        req.setPassword("password123");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(Translator.translate("error.duplicate.email")));

        log.info("==========> FINISHED TEST: {} - SUCCESS <==========", testInfo.getDisplayName());
    }

    @Test
    void whenRegister_withShortPassword_thenReturns400BadRequest(TestInfo testInfo) throws Exception {
        log.info("==========> STARTING TEST: {} <==========", testInfo.getDisplayName());

        // Arrange
        UserRegisterRequestDto req = new UserRegisterRequestDto();
        req.setUsername("newuser");
        req.setFullName("New User");
        req.setEmail("new.user@example.com");
        req.setPassword("123");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password").value(Translator.translate("error.password.too-short")));

        log.info("==========> FINISHED TEST: {} - SUCCESS <==========", testInfo.getDisplayName());
    }

    // =================================================================
    // == TEST CASES FOR /api/auth/authenticate
    // =================================================================
    @Test
    void whenAuthenticate_withValidCredentials_thenReturns200AndTokens(TestInfo testInfo) throws Exception {
        log.info("==========> STARTING TEST: {} <==========", testInfo.getDisplayName());

        // Arrange prepare a user for authentication
        userRepository.save(UserEntity.builder()
                .username("loginuser")
                .email("login.user@example.com")
                .password(passwordEncoder.encode("correctpassword"))
                .fullName("Login User")
                .build());

        AuthenticationRequestDto req = new AuthenticationRequestDto();
        req.setIdentifier("loginuser");
        req.setPassword("correctpassword");

        // Act & Assert
        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists());

        log.info("==========> FINISHED TEST: {} - SUCCESS <==========", testInfo.getDisplayName());
    }

    @Test
    void whenAuthenticate_withInvalidPassword_thenReturns401Unauthorized(TestInfo testInfo) throws Exception {
        log.info("==========> STARTING TEST: {} <==========", testInfo.getDisplayName());
        // Arrange
        userRepository.save(UserEntity.builder()
                .username("loginuser")
                .email("login.user@example.com")
                .password(passwordEncoder.encode("correctpassword"))
                .fullName("Login User")
                .build());

        AuthenticationRequestDto req = new AuthenticationRequestDto();
        req.setIdentifier("loginuser");
        req.setPassword("wrongpassword");

        // Act & Assert
        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(Translator.translate("error.invalid.username-or-email")));

        log.info("==========> FINISHED TEST: {} - SUCCESS <==========", testInfo.getDisplayName());
    }

    // =================================================================
    // == TEST CASES FOR /api/auth/change-password
    // =================================================================
    @Test
    void whenChangePassword_withValidData_thenReturns200OK(TestInfo testInfo) throws Exception {
        log.info("==========> STARTING TEST: {} <==========", testInfo.getDisplayName());
        // Arrange create a user and get access token
        UserEntity user = userRepository.save(UserEntity.builder()
                .username("changepass")
                .email("changepass@example.com")
                .password(passwordEncoder.encode("oldPassword123"))
                .fullName("Change Password User")
                .build());
        // assign change-password permission to the user
        assignDefaultRoleToUser(user);

        Map<String, String> tokens = getAuthTokens("changepass", "oldPassword123");

        UserChangePasswordRequestDto req = new UserChangePasswordRequestDto();
        req.setUserId(user.getId());
        req.setOldPassword("oldPassword123");
        req.setNewPassword("newPassword456");
        req.setConfirmPassword("newPassword456");

        // Act & Assert
        mockMvc.perform(patch("/api/auth/change-password")
                        .header("Authorization", "Bearer " + tokens.get("accessToken"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(Translator.translate("user.change-password.success")));

        log.info("==========> FINISHED TEST: {} - SUCCESS <==========", testInfo.getDisplayName());
    }

    // =================================================================
    // == TEST CASES FOR /api/auth/refresh-token
    // =================================================================
    @Test
    void whenRefreshToken_withValidToken_thenReturns200AndNewTokens(TestInfo testInfo) throws Exception {
        log.info("==========> STARTING TEST: {} <==========", testInfo.getDisplayName());

        // Arrange prepare a user and get initial tokens
        UserEntity savedUser = userRepository.save(UserEntity.builder()
                .username("refresher")
                .password(passwordEncoder.encode("pass123"))
                .email("ref@test.com")
                .fullName("n")
                .build());
        assignDefaultRoleToUser(savedUser);
        Map<String, String> tokens = getAuthTokens("refresher", "pass123");
        String oldRefreshTokenJti = jwtService.extractJti(tokens.get("refreshToken"));

        AuthRefreshTokenRequestDto req = new AuthRefreshTokenRequestDto();
        req.setToken(tokens.get("refreshToken"));

        // Act
        MvcResult result = mockMvc.perform(post("/api/auth/refresh-token")
                        .header("Authorization", "Bearer " + tokens.get("accessToken"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        JsonNode responseNode = objectMapper.readTree(result.getResponse().getContentAsString());
        String newAccessToken = responseNode.path("data").path("accessToken").asText();
        String newRefreshToken = responseNode.path("data").path("refreshToken").asText();

        assertThat(newAccessToken).isNotNull().isNotEqualTo(tokens.get("accessToken"));
        assertThat(newRefreshToken).isNotNull().isNotEqualTo(tokens.get("refreshToken"));

        // Verify that the old refresh token is invalidated
        assertThat(invalidatedTokenRepository.existsById(oldRefreshTokenJti)).isTrue();

        log.info("==========> FINISHED TEST: {} - SUCCESS <==========", testInfo.getDisplayName());
    }

    // =================================================================
    // == TEST CASES FOR /api/auth/logout
    // =================================================================

    @Test
    void whenLogout_withValidToken_thenInvalidatesToken(TestInfo testInfo) throws Exception {
        log.info("==========> STARTING TEST: {} <==========", testInfo.getDisplayName());
        // Arrange
        UserEntity savedUser = userRepository.save(UserEntity.builder()
                .username("logoutuser")
                .password(passwordEncoder.encode("pass123"))
                .email("logout@test.com")
                .fullName("n")
                .build());
        assignDefaultRoleToUser(savedUser);
        Map<String, String> tokens = getAuthTokens("logoutuser", "pass123");
        String accessTokenToLogout = tokens.get("accessToken");
        String accessTokenJti = jwtService.extractJti(accessTokenToLogout);

        AuthLogoutRequestDto req = new AuthLogoutRequestDto();
        req.setToken(accessTokenToLogout);

        // Act
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + accessTokenToLogout)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        // Assert:
        // 1. Check token is invalidated
        assertThat(invalidatedTokenRepository.existsById(accessTokenJti)).isTrue();

        // 2. try to access a protected endpoint with the invalidated token
        mockMvc.perform(
                        post("/api/v1/users/list") // checking a protected endpoint
                                .header("Authorization", "Bearer " + accessTokenToLogout)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isUnauthorized()); // must return 401 Unauthorized

        log.info("==========> FINISHED TEST: {} - SUCCESS <==========", testInfo.getDisplayName());
    }

    //    helper method to get authentication tokens
    private void assignDefaultRoleToUser(UserEntity user) {
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUser(user);
        userRole.setRole(roleRepository
                .findByName(DataInitializer.DEFAULT_ROLE)
                .orElseThrow(() -> new RuntimeException("Default role not found")));
        userRoleRepository.save(userRole);
        log.info("Assigned default role to user: {}", user.getUsername());
    }

    private Map<String, String> getAuthTokens(String username, String password) throws Exception {
        AuthenticationRequestDto authReq = new AuthenticationRequestDto();
        authReq.setIdentifier(username);
        authReq.setPassword(password);

        MvcResult result = mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authReq)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        return Map.of(
                "accessToken", root.path("data").path("accessToken").asText(),
                "refreshToken", root.path("data").path("refreshToken").asText());
    }
}

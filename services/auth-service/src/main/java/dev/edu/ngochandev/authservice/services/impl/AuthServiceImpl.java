package dev.edu.ngochandev.authservice.services.impl;

import com.nimbusds.jose.JOSEException;
import dev.edu.ngochandev.authservice.commons.DataInitializer;
import dev.edu.ngochandev.authservice.commons.enums.MailType;
import dev.edu.ngochandev.authservice.commons.enums.TokenType;
import dev.edu.ngochandev.authservice.commons.enums.UserStatus;
import dev.edu.ngochandev.authservice.dtos.req.*;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.entities.*;
import dev.edu.ngochandev.common.exceptions.DuplicateResourceException;
import dev.edu.ngochandev.common.exceptions.ResourceNotFoundException;
import dev.edu.ngochandev.common.exceptions.UnauthorizedException;
import dev.edu.ngochandev.common.events.UserRegisteredEvent;
import dev.edu.ngochandev.authservice.mappers.UserMapper;
import dev.edu.ngochandev.authservice.repositories.InvalidatedTokenRepository;
import dev.edu.ngochandev.authservice.repositories.RoleRepository;
import dev.edu.ngochandev.authservice.repositories.UserRepository;
import dev.edu.ngochandev.authservice.repositories.UserRoleRepository;
import dev.edu.ngochandev.authservice.services.AuthService;
import dev.edu.ngochandev.authservice.services.JwtService;
import dev.edu.ngochandev.authservice.services.MailService;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dev.edu.ngochandev.common.i18n.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final MailService mailService;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${app.frontend.main-url}")
    private String frontendUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long register(UserRegisterRequestDto req) throws JOSEException {
        if (userRepository.existsByUsername((req.getUsername()))) {
            throw new DuplicateResourceException("error.duplicate.username");
        }
        if (userRepository.existsByEmail((req.getEmail()))) {
            throw new DuplicateResourceException("error.duplicate.email");
        }

        UserEntity savedUser = userRepository.save(UserEntity.builder()
                .fullName(req.getFullName())
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .status(UserStatus.INACTIVE)
                .build());
        RoleEntity role =
                roleRepository.findByName(DataInitializer.DEFAULT_ROLE).orElseThrow();
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setRole(role);
        userRole.setUser(savedUser);
        savedUser.setUserRoles(Set.of(userRole));
        userRoleRepository.save(userRole);

        // generate email verification token
        String token = jwtService.generateToken(savedUser, TokenType.EMAIL_VERIFICATION_TOKEN);
        String verificationLink = frontendUrl + "/verify-email?token=" + token;
        // prepare email variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", savedUser.getFullName());
        variables.put("verificationLink", verificationLink);
        // send email
        MailEntity mail = new MailEntity();
        mail.setTo(savedUser.getEmail());
        mail.setSubject("Xác thực tài khoản");
        mail.setType(MailType.EMAIL_VERIFICATION);
        mailService.sendMail(mail, "email-verification-mail", variables);

        // publish user registered event
        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent(savedUser.getId(), savedUser.getFullName());
        eventPublisher.publishEvent(userRegisteredEvent);
        return savedUser.getId();
    }

    @Override
    public TokenResponseDto authenticate(AuthenticationRequestDto req) throws JOSEException, ParseException {
        UserEntity user = userRepository
                .findByUsernameOrEmail(req.getIdentifier())
                .orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("error.invalid.username-or-email");
        }
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public Long changePassword(UserChangePasswordRequestDto req) {
        UserEntity user = this.getUserById(req.getUserId());
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedException("error.invalid.old-password");
        }
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new UnauthorizedException("error.passwords.not-match");
        }
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        return userRepository.save(user).getId();
    }

    @Override
    public TokenResponseDto refreshToken(AuthRefreshTokenRequestDto req) throws ParseException, JOSEException {
        boolean isValid = jwtService.validateToken(req.getToken(), TokenType.REFRESH_TOKEN);
        if (!isValid) {
            throw new UnauthorizedException("error.token.invalid");
        }
        // disable current token
        String currentJti = jwtService.extractJti(req.getToken());
        Date currentExpiration = jwtService.extractExpiration(req.getToken());
        jwtService.disableToken(InvalidatedTokenEntity.builder()
                .id(currentJti)
                .expiredTime(currentExpiration)
                .build());
        // return new token
        String username = jwtService.extractUsername(req.getToken());
        UserEntity user = userRepository
                .findByUsernameOrEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));
        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public String logout(AuthLogoutRequestDto req) throws ParseException, JOSEException {
        boolean isValid = jwtService.validateToken(req.getToken(), TokenType.ACCESS_TOKEN);
        if (!isValid) {
            throw new UnauthorizedException("error.token.invalid");
        }
        String jti = jwtService.extractJti(req.getToken());
        Date expiration = jwtService.extractExpiration(req.getToken());

        InvalidatedTokenEntity invalidToken =
                InvalidatedTokenEntity.builder().id(jti).expiredTime(expiration).build();

        return invalidatedTokenRepository.save(invalidToken).getId();
    }

    @Override
    public Boolean forgotPassword(UserForgotPasswordRequestDto req) throws ParseException, JOSEException {
        UserEntity user = this.getUserByEmail(req.getEmail());

        String token = jwtService.generateToken(user, TokenType.FORGOT_PASSWORD_TOKEN);
        String resetLink = frontendUrl + "/reset-password?token=" + token;

        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", user.getFullName());
        variables.put("resetLink", resetLink);

        MailEntity mail = new MailEntity();
        mail.setTo(user.getEmail());
        mail.setSubject("Yêu cầu đặt lại mật khẩu");
        mail.setType(MailType.FORGOT_PASSWORD);

        mailService.sendMail(mail, "forgot-password-mail", variables);

        return true;
    }

    @Override
    public Boolean resetPassword(UserResetPasswordRequestDto req) throws ParseException, JOSEException {
        // check token validity
        boolean isValid = jwtService.validateToken(req.getToken(), TokenType.FORGOT_PASSWORD_TOKEN);
        if (!isValid) {
            throw new UnauthorizedException("error.token.invalid");
        }
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new UnauthorizedException("error.passwords.not-match");
        }
        // get user from token
        String email = jwtService.extractUsername(req.getToken());
        UserEntity user = this.getUserByEmail(email);
        // update password
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);

        // disable current token
        String jti = jwtService.extractJti(req.getToken());
        Date expiration = jwtService.extractExpiration(req.getToken());
        jwtService.disableToken(
                InvalidatedTokenEntity.builder().id(jti).expiredTime(expiration).build());
        return true;
    }

    @Override
    public Boolean verifyEmail(UserVerifyEmailRequestDto req) throws ParseException, JOSEException {
        // check token validity
        boolean isValid = jwtService.validateToken(req.getToken(), TokenType.EMAIL_VERIFICATION_TOKEN);
        if (!isValid) {
            throw new UnauthorizedException("error.token.invalid");
        }
        // get user from token
        String email = jwtService.extractUsername(req.getToken());
        UserEntity user = this.getUserByEmail(email);

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        // disable current token
        String jti = jwtService.extractJti(req.getToken());
        Date expiration = jwtService.extractExpiration(req.getToken());
        jwtService.disableToken(
                InvalidatedTokenEntity.builder().id(jti).expiredTime(expiration).build());

        return true;
    }

    @Override
    public Boolean verifyToken(AuthVerifyTokenRequestDto req)  {
        try{
            return jwtService.validateToken(req.getToken(), TokenType.ACCESS_TOKEN);
        }catch (Exception e){
            return false;
        }
    }

    private UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));
    }

    private UserEntity getUserByEmail(String email) {
        return userRepository
                .findByUsernameOrEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));
    }
}

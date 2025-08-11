package dev.edu.ngochandev.authservice.controllers;

import com.nimbusds.jose.JOSEException;
import dev.edu.ngochandev.authservice.dtos.req.AuthVerifyTokenRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.*;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.services.AuthService;
import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AUTH-CONTROLLER", description = "Manages user authentication and registration")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService userService;
    private final Translator translator;

    @PostMapping("/verify-token")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Verify Token",
            description = "Verifies the provided authentication token, note: this endpoint must be called by the gateway service to verify the token before allowing access to protected resources."
    )
    @SecurityRequirements
    public SuccessResponseDto<Boolean> verifyToken(@RequestBody @Valid AuthVerifyTokenRequestDto req)  {
        return SuccessResponseDto.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("user.verify-token.success"))
                .data(userService.verifyToken(req))
                .build();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details.")
    @SecurityRequirements
    public SuccessResponseDto<Long> register(@RequestBody @Valid UserRegisterRequestDto req) throws JOSEException {
        return SuccessResponseDto.<Long>builder()
                .status(HttpStatus.CREATED.value())
                .message(translator.translate("user.register.success"))
                .data(userService.register(req))
                .build();
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Authenticate user", description = "Authenticates a user with the provided credentials.")
    @SecurityRequirements
    public SuccessResponseDto<TokenResponseDto> login(@RequestBody @Valid AuthenticationRequestDto req)
            throws JOSEException, ParseException {
        return SuccessResponseDto.<TokenResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("user.authenticate.success"))
                .data(userService.authenticate(req))
                .build();
    }

    @PatchMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "permission.user.change-password",
            description = "Changes the password for the authenticated user.",
            extensions = {
                @Extension(
                        name = "x-module",
                        properties = {@ExtensionProperty(name = "value", value = "user")})
            })
    public SuccessResponseDto<Long> changePassword(@RequestBody @Valid UserChangePasswordRequestDto req) {
        return SuccessResponseDto.<Long>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("user.change-password.success"))
                .data(userService.changePassword(req))
                .build();
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "permission.user.refresh-token",
            description = "Refreshes the authentication token for the user.",
            extensions = {
                @Extension(
                        name = "x-module",
                        properties = {@ExtensionProperty(name = "value", value = "user")})
            })
    public SuccessResponseDto<TokenResponseDto> refreshToken(@RequestBody @Valid AuthRefreshTokenRequestDto req)
            throws JOSEException, ParseException {
        return SuccessResponseDto.<TokenResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("user.refresh-token.success"))
                .data(userService.refreshToken(req))
                .build();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "permission.user.logout",
            description = "Logs out the user by invalidating their authentication token.",
            extensions = {
                @Extension(
                        name = "x-module",
                        properties = {@ExtensionProperty(name = "value", value = "user")})
            })
    public SuccessResponseDto<String> logout(@RequestBody @Valid AuthLogoutRequestDto req)
            throws ParseException, JOSEException {
        return SuccessResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("user.logout.success"))
                .data(userService.logout(req))
                .build();
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Forgot Password",
            description = "Initiates the forgot password process for a user by sending a reset link to their email.")
    @SecurityRequirements
    public SuccessResponseDto<Boolean> forgotPassword(@RequestBody @Valid UserForgotPasswordRequestDto req)
            throws ParseException, JOSEException {
        return SuccessResponseDto.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("user.forgot-password.success"))
                .data(userService.forgotPassword(req))
                .build();
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Reset Password", description = "Resets the password for a user using a reset token.")
    @SecurityRequirements
    public SuccessResponseDto<Boolean> resetPassword(@RequestBody @Valid UserResetPasswordRequestDto req)
            throws ParseException, JOSEException {
        return SuccessResponseDto.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("user.reset-password.success"))
                .data(userService.resetPassword(req))
                .build();
    }

    @PostMapping("/verify-email")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Verify Email", description = "Verifies a user's email address using a verification token.")
    @SecurityRequirements
    public SuccessResponseDto<Boolean> verifyEmail(@RequestBody @Valid UserVerifyEmailRequestDto req)
            throws ParseException, JOSEException {
        return SuccessResponseDto.<Boolean>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("user.verify-email.success"))
                .data(userService.verifyEmail(req))
                .build();
    }
}

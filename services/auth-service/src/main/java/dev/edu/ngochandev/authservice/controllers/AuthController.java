package dev.edu.ngochandev.authservice.controllers;

import com.nimbusds.jose.JOSEException;
import dev.edu.ngochandev.authservice.common.Translator;
import dev.edu.ngochandev.authservice.dtos.req.*;
import dev.edu.ngochandev.authservice.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import dev.edu.ngochandev.authservice.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<UserResponseDto> register(@RequestBody @Valid UserRegisterRequestDto req) {
        return SuccessResponseDto.<UserResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("user.register.success"))
                .data(userService.register(req))
                .build();
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<TokenResponseDto> login(@RequestBody @Valid AuthenticationRequestDto req) throws JOSEException, ParseException {
        return SuccessResponseDto.<TokenResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.authenticate.success"))
                .data(userService.authenticate(req))
                .build();
    }
    @PatchMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<Long> changePassword(@RequestBody @Valid UserChangePasswordRequestDto req){
        return SuccessResponseDto.<Long>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.change-password.success"))
                .data(userService.changePassword(req))
                .build();
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<TokenResponseDto> refreshToken(@RequestBody @Valid AuthRefreshTokenRequestDto req) throws JOSEException {
        return SuccessResponseDto.<TokenResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.refresh-token.success"))
                .data(userService.refreshToken(req))
                .build();
    }
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<String> logout(@RequestBody @Valid AuthLogoutRequestDto req) throws ParseException, JOSEException {
        return SuccessResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.logout.success"))
                .data(userService.logout(req))
                .build();
    }
}

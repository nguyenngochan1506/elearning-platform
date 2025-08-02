package dev.edu.ngochandev.authservice.controllers;

import com.nimbusds.jose.JOSEException;
import dev.edu.ngochandev.authservice.dtos.reqs.AuthenticationRequestDto;
import dev.edu.ngochandev.authservice.dtos.reqs.UserRegisterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import dev.edu.ngochandev.authservice.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
                .message("User registered successfully")
                .data(userService.register(req))
                .build();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<TokenResponseDto> login(@RequestBody @Valid AuthenticationRequestDto req) throws JOSEException {
        return SuccessResponseDto.<TokenResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Login successful")
                .data(userService.authenticate(req))
                .build();
    }

}

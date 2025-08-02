package dev.edu.ngochandev.authservice.controllers;

import dev.edu.ngochandev.authservice.dtos.reqs.UserRegisterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.SuccessResponse;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import dev.edu.ngochandev.authservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse register(@RequestBody @Valid UserRegisterRequestDto req) {
        return SuccessResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("User registered successfully")
                .data(userService.register(req))
                .build();
    }
}

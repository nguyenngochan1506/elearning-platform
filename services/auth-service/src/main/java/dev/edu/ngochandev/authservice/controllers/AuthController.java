package dev.edu.ngochandev.authservice.controllers;

import dev.edu.ngochandev.authservice.dtos.reqs.UserRegisterRequestDto;
import dev.edu.ngochandev.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("register")
    public Object register(@RequestBody UserRegisterRequestDto req) {
        return userService.register(req);
    }
}

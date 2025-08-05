package dev.edu.ngochandev.authservice.controllers;

import dev.edu.ngochandev.authservice.commons.Translator;
import dev.edu.ngochandev.authservice.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import dev.edu.ngochandev.authservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<PageResponseDto<UserResponseDto>> listUsers(@RequestBody @Valid AdvancedFilterRequestDto filter) {
        return SuccessResponseDto.<PageResponseDto<UserResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.get.success"))
                .data(userService.listUsers(filter))
                .build();
    }
}

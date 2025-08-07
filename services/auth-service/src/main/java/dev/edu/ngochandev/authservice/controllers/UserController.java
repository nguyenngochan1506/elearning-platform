package dev.edu.ngochandev.authservice.controllers;

import dev.edu.ngochandev.authservice.commons.Translator;
import dev.edu.ngochandev.authservice.dtos.req.AdminUserCreateRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserManyDeleteRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserUpdateRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.AdminUserResponse;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.SuccessResponseDto;
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
    public SuccessResponseDto<PageResponseDto<AdminUserResponse>> listUsers(@RequestBody @Valid AdvancedFilterRequestDto filter) {
        return SuccessResponseDto.<PageResponseDto<AdminUserResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.get.success"))
                .data(userService.listUsers(filter))
                .build();
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<Long> createUser(@RequestBody @Valid AdminUserCreateRequestDto req) {
        return SuccessResponseDto.<Long>builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("user.create.success"))
                .data(userService.createUser(req))
                .build();
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<Long> deleteUser(@PathVariable Long id) {
        return SuccessResponseDto.<Long>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.delete.success"))
                .data(userService.deleteUser(id))
                .build();
    }
    @DeleteMapping("/batch")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<Void> deleteUsers(@RequestBody UserManyDeleteRequestDto req) {
        userService.deleteManyUsers(req);
        return SuccessResponseDto.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.delete.success"))
                .build();
    }
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<Long> updateUser(@RequestBody @Valid UserUpdateRequestDto req){
        return SuccessResponseDto.<Long>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.update.success"))
                .data(userService.updateUser(req))
                .build();
    }
}

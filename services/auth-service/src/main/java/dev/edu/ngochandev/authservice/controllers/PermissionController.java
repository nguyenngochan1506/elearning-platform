package dev.edu.ngochandev.authservice.controllers;

import dev.edu.ngochandev.authservice.commons.Translator;
import dev.edu.ngochandev.authservice.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.PermissionResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.authservice.services.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<PageResponseDto<PermissionResponseDto>> getAllPermissions(@RequestBody @Valid SimpleFilterRequestDto filter) {
        return SuccessResponseDto.<PageResponseDto<PermissionResponseDto>>builder()
                .data(permissionService.getAllPermissions(filter))
                .message(Translator.translate("permission.list.success"))
                .build();
    }
}

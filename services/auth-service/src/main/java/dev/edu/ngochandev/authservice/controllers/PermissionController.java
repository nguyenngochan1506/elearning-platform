package dev.edu.ngochandev.authservice.controllers;

import dev.edu.ngochandev.common.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.PermissionResponseDto;
import dev.edu.ngochandev.authservice.services.PermissionService;
import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Tag(name = "PERMISSION-CONTROLLER", description = "Manages permissions for users and roles")
public class PermissionController {
    private final PermissionService permissionService;
    private final Translator translator;

    @PostMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "permission.permission-list",
            description = "Lists all permissions based on the provided filter.",
            extensions = {
                @Extension(
                        name = "x-module",
                        properties = {@ExtensionProperty(name = "value", value = "permission")})
            })
    public SuccessResponseDto<PageResponseDto<PermissionResponseDto>> getAllPermissions(
            @RequestBody @Valid SimpleFilterRequestDto filter) {
        return SuccessResponseDto.<PageResponseDto<PermissionResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .data(permissionService.getAllPermissions(filter))
                .message(translator.translate("permission.list.success"))
                .build();
    }
}

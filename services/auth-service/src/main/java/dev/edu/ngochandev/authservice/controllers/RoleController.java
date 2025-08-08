package dev.edu.ngochandev.authservice.controllers;

import dev.edu.ngochandev.authservice.commons.Translator;
import dev.edu.ngochandev.authservice.dtos.req.RoleCreateRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.RoleManyDeleteRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.RoleDetailResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.RoleSummaryResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.authservice.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "ROLE-CONTROLLER", description = "Manages roles and their permissions")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "permission.role.list",
            description = "Lists all permissions based on the provided filter.",
            extensions = {
                @Extension(
                        name = "x-module",
                        properties = {@ExtensionProperty(name = "value", value = "role")})
            })
    public SuccessResponseDto<PageResponseDto<RoleSummaryResponseDto>> listRoles(
            @RequestBody @Valid SimpleFilterRequestDto filter) {
        return SuccessResponseDto.<PageResponseDto<RoleSummaryResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.list.success"))
                .data(roleService.getAllRoles(filter))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "permission.role.create",
            description = "Lists all permissions based on the provided filter.",
            extensions = {
                @Extension(
                        name = "x-module",
                        properties = {@ExtensionProperty(name = "value", value = "role")})
            })
    public SuccessResponseDto<Long> createRole(@RequestBody @Valid RoleCreateRequestDto req) {
        return SuccessResponseDto.<Long>builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("role.create.success"))
                .data(roleService.createRole(req))
                .build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "permission.role.get-detail",
            description = "Get role by ID.",
            extensions = {
                @Extension(
                        name = "x-module",
                        properties = {@ExtensionProperty(name = "value", value = "role")})
            })
    public SuccessResponseDto<RoleDetailResponseDto> getRoleById(@PathVariable Long id) {
        return SuccessResponseDto.<RoleDetailResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.get.success"))
                .data(roleService.getRoleById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "permission.role.delete",
            description = "Delete role by ID.",
            extensions = {
                @Extension(
                        name = "x-module",
                        properties = {@ExtensionProperty(name = "value", value = "role")})
            })
    public SuccessResponseDto<Long> deleteRoleById(@PathVariable Long id) {
        return SuccessResponseDto.<Long>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.delete.success"))
                .data(roleService.deleteRoleById(id))
                .build();
    }

    @DeleteMapping("/batch")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "permission.role.delete-many",
            description = "Delete multiple roles by their IDs.",
            extensions = {
                @Extension(
                        name = "x-module",
                        properties = {@ExtensionProperty(name = "value", value = "role")})
            })
    public SuccessResponseDto<Void> deleteRolesByIds(@RequestBody RoleManyDeleteRequestDto req) {
        roleService.deleteManyRoles(req);
        return SuccessResponseDto.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.delete.success"))
                .build();
    }
}

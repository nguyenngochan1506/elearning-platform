package dev.edu.ngochandev.authservice.controllers;

import dev.edu.ngochandev.authservice.commons.Translator;
import dev.edu.ngochandev.authservice.dtos.req.RoleCreateRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.RoleDetailResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.RoleSummaryResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.authservice.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<PageResponseDto<RoleSummaryResponseDto>> listRoles(@RequestBody @Valid SimpleFilterRequestDto filter){
        return  SuccessResponseDto.<PageResponseDto<RoleSummaryResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.list.success"))
                .data(roleService.getAllRoles(filter))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<Long> createRole(@RequestBody @Valid RoleCreateRequestDto req){
        return SuccessResponseDto.<Long>builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("role.create.success"))
                .data(roleService.createRole(req))
                .build();
    }
}

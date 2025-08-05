package dev.edu.ngochandev.authservice.services;

import dev.edu.ngochandev.authservice.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.PermissionResponseDto;

public interface PermissionService {
    PageResponseDto<PermissionResponseDto> getAllPermissions(SimpleFilterRequestDto filter);
}

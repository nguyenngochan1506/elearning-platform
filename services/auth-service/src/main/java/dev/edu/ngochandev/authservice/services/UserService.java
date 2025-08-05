package dev.edu.ngochandev.authservice.services;

import dev.edu.ngochandev.authservice.dtos.req.AdminUserCreateRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.AdminUserResponse;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import jakarta.validation.Valid;

public interface UserService {
    PageResponseDto<AdminUserResponse> listUsers(AdvancedFilterRequestDto filter);

    Long createUser( AdminUserCreateRequestDto req);

    Long deleteUser(Long id);
}

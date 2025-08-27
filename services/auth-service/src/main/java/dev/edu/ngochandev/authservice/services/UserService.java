package dev.edu.ngochandev.authservice.services;

import dev.edu.ngochandev.authservice.dtos.req.AdminUserCreateRequestDto;
import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserManyDeleteRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserUpdateRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.AdminUserResponse;

public interface UserService {
    PageResponseDto<AdminUserResponse> listUsers(AdvancedFilterRequestDto filter, Long organizationId);

    Long createUser(AdminUserCreateRequestDto req, Long organizationId);

    Long deleteUser(Long id);

    void deleteManyUsers(UserManyDeleteRequestDto req);

    Long updateUser(UserUpdateRequestDto req, Long organizationId);
}

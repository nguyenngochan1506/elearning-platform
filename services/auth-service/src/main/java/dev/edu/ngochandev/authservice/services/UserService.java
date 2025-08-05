package dev.edu.ngochandev.authservice.services;

import dev.edu.ngochandev.authservice.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;

public interface UserService {
    PageResponseDto<UserResponseDto> listUsers(AdvancedFilterRequestDto filter);
}

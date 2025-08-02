package dev.edu.ngochandev.authservice.services;

import dev.edu.ngochandev.authservice.dtos.reqs.UserRegisterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegisterRequestDto req);
}

package dev.edu.ngochandev.authservice.services;

import dev.edu.ngochandev.authservice.dtos.reqs.UserRegisterRequestDto;

public interface UserService {
    String register(UserRegisterRequestDto req);
}

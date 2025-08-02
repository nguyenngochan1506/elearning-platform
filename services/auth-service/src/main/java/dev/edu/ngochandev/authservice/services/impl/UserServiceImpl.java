package dev.edu.ngochandev.authservice.services.impl;

import dev.edu.ngochandev.authservice.dtos.reqs.UserRegisterRequestDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.enums.UserStatus;
import dev.edu.ngochandev.authservice.repositories.UserRepository;
import dev.edu.ngochandev.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public String register(UserRegisterRequestDto req) {
        UserEntity user = new UserEntity();
        user.setUsername(req.getUsername());
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        return "User registered successfully with username: " + user.getUsername();
    }
}

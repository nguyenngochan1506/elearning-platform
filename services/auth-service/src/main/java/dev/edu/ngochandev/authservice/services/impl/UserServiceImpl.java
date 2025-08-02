package dev.edu.ngochandev.authservice.services.impl;

import dev.edu.ngochandev.authservice.dtos.reqs.UserRegisterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.enums.UserStatus;
import dev.edu.ngochandev.authservice.exceptions.DuplicateResourceException;
import dev.edu.ngochandev.authservice.repositories.UserRepository;
import dev.edu.ngochandev.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponseDto register(UserRegisterRequestDto req) {
        if(userRepository.existsByUsername((req.getUsername()))){
            throw new DuplicateResourceException(req.getUsername() + " already exists");
        }
        if(userRepository.existsByEmail((req.getEmail()))){
            throw new DuplicateResourceException(req.getEmail() + " already exists");
        }
        UserEntity savedUser = userRepository.save(UserEntity.builder()
                        .fullName(req.getFullName())
                        .username(req.getUsername())
                        .email(req.getEmail())
                        .password(req.getPassword())
                        .status(UserStatus.INACTIVE)
                        .build());
        return UserResponseDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .status(savedUser.getStatus())
                .createdAt(savedUser.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                .build();
    }
}

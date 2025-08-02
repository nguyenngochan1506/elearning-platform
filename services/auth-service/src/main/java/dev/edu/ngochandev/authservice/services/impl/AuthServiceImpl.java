package dev.edu.ngochandev.authservice.services.impl;

import com.nimbusds.jose.JOSEException;
import dev.edu.ngochandev.authservice.dtos.req.AuthenticationRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserChangePasswordRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserRegisterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.enums.TokenType;
import dev.edu.ngochandev.authservice.enums.UserStatus;
import dev.edu.ngochandev.authservice.exceptions.DuplicateResourceException;
import dev.edu.ngochandev.authservice.exceptions.ResourceNotFoundException;
import dev.edu.ngochandev.authservice.exceptions.UnauthorizedException;
import dev.edu.ngochandev.authservice.mapper.UserMapper;
import dev.edu.ngochandev.authservice.repositories.UserRepository;
import dev.edu.ngochandev.authservice.services.AuthService;
import dev.edu.ngochandev.authservice.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Value("${jwt.accessExpiration}")
    private Long accessExpiration;

    @Override
    public UserResponseDto register(UserRegisterRequestDto req) {
        if(userRepository.existsByUsername((req.getUsername()))){
            throw new DuplicateResourceException("error.duplicate.username");
        }
        if(userRepository.existsByEmail((req.getEmail()))){
            throw new DuplicateResourceException("error.duplicate.email");
        }
        UserEntity savedUser = userRepository.save(UserEntity.builder()
                        .fullName(req.getFullName())
                        .username(req.getUsername())
                        .email(req.getEmail())
                        .password(passwordEncoder.encode(req.getPassword()))
                        .status(UserStatus.INACTIVE)
                        .build());
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public TokenResponseDto authenticate(AuthenticationRequestDto req) throws JOSEException {
        UserEntity user = userRepository.findByUsernameOrEmail(req.getIdentifier())
                .orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));
        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            throw new UnauthorizedException("error.invalid.username-or-email");
        }
        return TokenResponseDto.builder()
                .accessToken(jwtService.generateToken(user, TokenType.ACCESS_TOKEN))
                .expirationTime(new Date(System.currentTimeMillis() + accessExpiration))
                .build();
    }

    @Override
    public Long changePassword(UserChangePasswordRequestDto req) {
        UserEntity user = this.getUserById(req.getUserId());
        if(!passwordEncoder.matches(req.getOldPassword(), user.getPassword())){
            throw new UnauthorizedException("error.invalid.old-password");
        }
        if(!req.getNewPassword().equals(req.getConfirmPassword())){
            throw new UnauthorizedException("error.passwords.not-match");
        }
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        return userRepository.save(user).getId();
    }

    private UserEntity getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("error.user.not-found"));
    }
}

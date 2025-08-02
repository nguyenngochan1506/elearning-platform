package dev.edu.ngochandev.authservice.services.impl;

import com.nimbusds.jose.JOSEException;
import dev.edu.ngochandev.authservice.dtos.reqs.AuthenticationRequestDto;
import dev.edu.ngochandev.authservice.dtos.reqs.UserRegisterRequestDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserResponseDto register(UserRegisterRequestDto req) {
        if(userRepository.existsByUsername((req.getUsername()))){
            throw new DuplicateResourceException("Username already exists" + req.getUsername());
        }
        if(userRepository.existsByEmail((req.getEmail()))){
            throw new DuplicateResourceException( "Email already exists" +req.getEmail() );
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found with identifier: " + req.getIdentifier()));
        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            throw new UnauthorizedException("Username or password is incorrect");
        }
        return TokenResponseDto.builder()
                .accessToken(jwtService.generateToken(user, TokenType.ACCESS_TOKEN))
                .refreshToken(jwtService.generateToken(user, TokenType.REFRESH_TOKEN))
                .build();
    }
}

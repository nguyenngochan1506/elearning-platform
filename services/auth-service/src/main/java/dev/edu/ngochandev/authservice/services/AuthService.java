package dev.edu.ngochandev.authservice.services;

import com.nimbusds.jose.JOSEException;
import dev.edu.ngochandev.authservice.dtos.req.AuthenticationRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserChangePasswordRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserRegisterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import jakarta.validation.Valid;

public interface AuthService {
    UserResponseDto register(UserRegisterRequestDto req);
    TokenResponseDto authenticate(AuthenticationRequestDto req) throws JOSEException;

    Long changePassword(UserChangePasswordRequestDto req);
}

package dev.edu.ngochandev.authservice.services;

import com.nimbusds.jose.JOSEException;
import dev.edu.ngochandev.authservice.dtos.reqs.AuthenticationRequestDto;
import dev.edu.ngochandev.authservice.dtos.reqs.UserRegisterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;

public interface AuthService {
    UserResponseDto register(UserRegisterRequestDto req);
    TokenResponseDto authenticate(AuthenticationRequestDto req) throws JOSEException;
}

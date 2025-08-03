package dev.edu.ngochandev.authservice.services;

import com.nimbusds.jose.JOSEException;
import dev.edu.ngochandev.authservice.dtos.req.*;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;

import java.text.ParseException;

public interface AuthService {
    UserResponseDto register(UserRegisterRequestDto req);
    TokenResponseDto authenticate(AuthenticationRequestDto req) throws JOSEException, ParseException;

    Long changePassword(UserChangePasswordRequestDto req);

    TokenResponseDto refreshToken( AuthRefreshTokenRequestDto req);

    String logout(AuthLogoutRequestDto req) throws ParseException, JOSEException;
}

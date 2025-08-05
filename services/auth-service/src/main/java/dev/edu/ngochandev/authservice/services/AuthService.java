package dev.edu.ngochandev.authservice.services;

import com.nimbusds.jose.JOSEException;
import dev.edu.ngochandev.authservice.dtos.req.*;
import dev.edu.ngochandev.authservice.dtos.res.TokenResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import jakarta.validation.Valid;

import java.text.ParseException;

public interface AuthService {
    Long register(UserRegisterRequestDto req) throws JOSEException;
    TokenResponseDto authenticate(AuthenticationRequestDto req) throws JOSEException, ParseException;

    Long changePassword(UserChangePasswordRequestDto req);

    TokenResponseDto refreshToken( AuthRefreshTokenRequestDto req) throws ParseException, JOSEException;

    String logout(AuthLogoutRequestDto req) throws ParseException, JOSEException;

    Boolean forgotPassword(UserForgotPasswordRequestDto req) throws ParseException, JOSEException;
    Boolean resetPassword(UserResetPasswordRequestDto req) throws ParseException, JOSEException;

    Boolean verifyEmail(UserVerifyEmailRequestDto req) throws ParseException, JOSEException;
}

package dev.edu.ngochandev.authservice.dtos.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthRefreshTokenRequestDto {
    @NotBlank(message = "error.token.not-blank")
    private String token;
}

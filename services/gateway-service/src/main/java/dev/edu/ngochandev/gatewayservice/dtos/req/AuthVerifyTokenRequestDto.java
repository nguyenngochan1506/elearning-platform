package dev.edu.ngochandev.gatewayservice.dtos.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthVerifyTokenRequestDto {
    @NotBlank(message = "error.token.not-blank")
    private String token;
}

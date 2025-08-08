package dev.edu.ngochandev.authservice.dtos.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVerifyEmailRequestDto {
    @NotBlank(message = "error.token.not-blank")
    private String token;
}

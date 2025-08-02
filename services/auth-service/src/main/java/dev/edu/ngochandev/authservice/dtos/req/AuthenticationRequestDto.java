package dev.edu.ngochandev.authservice.dtos.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthenticationRequestDto {
    @NotBlank(message = "error.identifier.not-blank")
    private String identifier; // can be username or email
    @NotBlank(message = "error.password.not-blank")
    private String password;
}

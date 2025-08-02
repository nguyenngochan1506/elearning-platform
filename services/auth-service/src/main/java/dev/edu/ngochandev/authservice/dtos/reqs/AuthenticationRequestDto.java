package dev.edu.ngochandev.authservice.dtos.reqs;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthenticationRequestDto {
    @NotBlank(message = "Identifier cannot be blank")
    private String identifier; // can be username or email
    @NotBlank(message = "Password cannot be blank")
    private String password;
}

package dev.edu.ngochandev.authservice.dtos.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForgotPasswordRequestDto {
    @NotBlank(message = "error.email.not-blank")
    @Email(message = "error.email.invalid")
    private String email;
}

package dev.edu.ngochandev.authservice.dtos.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserRegisterRequestDto implements Serializable {
    @NotBlank(message = "error.username.not-blank")
    private String username;

    @NotBlank(message = "error.fullname.not-blank")
    private String fullName;

    @NotBlank(message = "error.email.not-blank")
    @Email(message = "error.email.invalid")
    private String email;

    @NotBlank(message = "error.password.not-blank")
    @Size(min = 8, message = "error.password.too-short")
    private String password;
}

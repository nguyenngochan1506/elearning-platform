package dev.edu.ngochandev.authservice.dtos.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePasswordRequestDto {
    @NotNull(message = "error.user-id.not-null")
    @Min(value = 1, message = "error.user-id.invalid")
    private Long userId;

    @NotBlank(message = "error.password.not-blank")
    private String oldPassword;

    @Size(min = 8, message = "error.password.too-short")
    @NotBlank(message = "error.password.not-blank")
    private String newPassword;

    @NotBlank(message = "error.password.not-blank")
    @Size(min = 8, message = "error.password.too-short")
    private String confirmPassword;
}

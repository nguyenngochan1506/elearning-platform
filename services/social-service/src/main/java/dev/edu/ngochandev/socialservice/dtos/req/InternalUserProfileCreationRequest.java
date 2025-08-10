package dev.edu.ngochandev.socialservice.dtos.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalUserProfileCreationRequest {
    @NotNull(message = "User ID cannot be null")
    @Min(value = 1, message = "User ID must be greater than 0")
    private Long userId;
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;
}

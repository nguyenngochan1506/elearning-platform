package dev.edu.ngochandev.authservice.dtos.req;

import dev.edu.ngochandev.authservice.commons.enums.UserStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserUpdateRequestDto{
    @NotNull(message = "error.user-id.not-null")
    @Min(value = 1, message = "error.user-id.invalid")
    private Long id;
    private UserStatus status;
    private final List<Long> roleIds = List.of();
    @NotBlank(message = "error.fullname.not-blank")
    private String fullName;
}

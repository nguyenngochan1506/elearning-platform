package dev.edu.ngochandev.authservice.dtos.req;

import dev.edu.ngochandev.authservice.commons.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class AdminUserCreateRequestDto extends UserRegisterRequestDto{
    @NotNull(message = "user.status.not-null")
    private UserStatus status;
    private final List<Long> roleIds = List.of();
}

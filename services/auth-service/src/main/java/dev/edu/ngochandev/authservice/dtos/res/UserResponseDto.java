package dev.edu.ngochandev.authservice.dtos.res;

import dev.edu.ngochandev.authservice.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponseDto {
    private Long id;

    private String username;

    private String fullName;

    private String email;

    private UserStatus status;

    private String createdAt;
}

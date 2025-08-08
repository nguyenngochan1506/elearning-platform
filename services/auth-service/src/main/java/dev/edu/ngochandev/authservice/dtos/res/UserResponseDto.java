package dev.edu.ngochandev.authservice.dtos.res;

import dev.edu.ngochandev.authservice.commons.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private UserStatus status;
    private String createdAt;
}

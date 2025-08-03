package dev.edu.ngochandev.authservice.dtos.res;

import dev.edu.ngochandev.authservice.commons.enums.UserStatus;
import lombok.Builder;

@Builder
public record UserResponseDto (Long id, String username, String fullName, String email, UserStatus status, String createdAt){}

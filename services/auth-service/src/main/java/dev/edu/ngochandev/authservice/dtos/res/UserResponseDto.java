package dev.edu.ngochandev.authservice.dtos.res;

import dev.edu.ngochandev.authservice.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
@Builder
public record UserResponseDto (Long id, String username, String fullName, String email, UserStatus status, String createdAt){}

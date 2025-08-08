package dev.edu.ngochandev.authservice.dtos.res;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserResponse extends UserResponseDto {
    private LocalDateTime lastLoginAt;
    private LocalDateTime lastUpdatedAt;
    private Long createdBy;
    private Long updatedBy;
    private List<RoleSummaryResponseDto> roles;
}

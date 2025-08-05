package dev.edu.ngochandev.authservice.dtos.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RoleCreateRequestDto {
    @NotBlank(message = "error.role.name.not-blank")
    private String name;
    private String description;
    private Long[] permissionIds;
}

package dev.edu.ngochandev.authservice.dtos.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleCreateRequestDto {
    @NotBlank(message = "error.role.name.not-blank")
    private String name;
    private String description;
    private Long[] permissionIds;
}

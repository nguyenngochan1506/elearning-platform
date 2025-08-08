package dev.edu.ngochandev.authservice.dtos.res;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDetailResponseDto extends RoleSummaryResponseDto {
    List<PermissionResponseDto> permissions;
}

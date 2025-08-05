package dev.edu.ngochandev.authservice.dtos.res;

import java.util.List;

public class RoleDetailResponseDto extends RoleSummaryResponseDto{
    List<PermissionResponseDto> permissions;
}

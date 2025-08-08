package dev.edu.ngochandev.authservice.dtos.res;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleDetailResponseDto extends RoleSummaryResponseDto{
	List<PermissionResponseDto> permissions;
}

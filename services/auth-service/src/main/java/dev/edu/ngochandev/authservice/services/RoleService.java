package dev.edu.ngochandev.authservice.services;

import dev.edu.ngochandev.authservice.dtos.req.RoleCreateRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.RoleManyDeleteRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.RoleDetailResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.RoleSummaryResponseDto;

public interface RoleService {
	PageResponseDto<RoleSummaryResponseDto> getAllRoles(SimpleFilterRequestDto filter);
	RoleDetailResponseDto getRoleById(Long id);
	Long createRole(RoleCreateRequestDto req);

	Long deleteRoleById(Long id);

	void deleteManyRoles(RoleManyDeleteRequestDto req);
}

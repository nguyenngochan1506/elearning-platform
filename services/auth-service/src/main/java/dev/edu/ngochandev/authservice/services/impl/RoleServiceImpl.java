package dev.edu.ngochandev.authservice.services.impl;

import dev.edu.ngochandev.authservice.commons.MyUtils;
import dev.edu.ngochandev.authservice.dtos.req.RoleCreateRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.RoleManyDeleteRequestDto;
import dev.edu.ngochandev.common.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.RoleDetailResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.RoleSummaryResponseDto;
import dev.edu.ngochandev.authservice.entities.PermissionEntity;
import dev.edu.ngochandev.authservice.entities.RoleEntity;
import dev.edu.ngochandev.authservice.entities.RolePermissionEntity;
import dev.edu.ngochandev.common.exceptions.ResourceNotFoundException;
import dev.edu.ngochandev.authservice.mappers.RoleMapper;
import dev.edu.ngochandev.authservice.repositories.PermissionRepository;
import dev.edu.ngochandev.authservice.repositories.RolePermissionRepository;
import dev.edu.ngochandev.authservice.repositories.RoleRepository;
import dev.edu.ngochandev.authservice.services.RoleService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleMapper roleMapper;

    @Override
    public PageResponseDto<RoleSummaryResponseDto> getAllRoles(SimpleFilterRequestDto filter) {
        // pageable
        Pageable pageable = MyUtils.createPageable(filter);
        // search
        Page<RoleEntity> pageItems = null;
        if (StringUtils.hasLength(filter.getSearch())) {
            pageItems = roleRepository.findGlobalRolesBySearch("%" + filter.getSearch() + "%", pageable);
        } else {
            pageItems = roleRepository.findAll(pageable);
        }
        List<RoleSummaryResponseDto> itemsResponse =
                pageItems.map(roleMapper::mapToSummaryResponseDto).toList();

        return PageResponseDto.<RoleSummaryResponseDto>builder()
                .currentPage(filter.getPage())
                .totalElements(pageItems.getTotalElements())
                .totalPages(pageItems.getTotalPages())
                .items(itemsResponse)
                .build();
    }

    @Override
    public RoleDetailResponseDto getRoleById(Long id) {
        RoleEntity foundRole =
                roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("error.role.not.found"));
        return roleMapper.mapToDetailResponseDto(foundRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleCreateRequestDto req) {
        List<Long> permissionIds = List.of(req.getPermissionIds());

        List<PermissionEntity> foundPermissions = permissionRepository.findAllById(permissionIds);
        if (foundPermissions.size() != permissionIds.size()) {
            throw new ResourceNotFoundException("error.permission.not.found");
        }

        RoleEntity newRole = RoleEntity.builder()
                .name(req.getName())
                .description(req.getDescription())
                .build();
        roleRepository.save(newRole);

        Set<RolePermissionEntity> permissionsToAssign = new HashSet<>();
        for (PermissionEntity permission : foundPermissions) {
            RolePermissionEntity assignment = new RolePermissionEntity();
            assignment.setRole(newRole);
            assignment.setPermission(permission);
            permissionsToAssign.add(assignment);
        }
        rolePermissionRepository.saveAll(permissionsToAssign);
        return newRole.getId();
    }

    @Override
    public Long deleteRoleById(Long id) {
        RoleEntity foundRole =
                roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("error.role.not.found"));
        // Soft delete
        foundRole.setIsDeleted(true);
        foundRole.getRolePermissions().forEach(rolePermission -> {
            rolePermission.setIsDeleted(true);
            rolePermissionRepository.save(rolePermission);
        });
        roleRepository.save(foundRole);
        return foundRole.getId();
    }

    @Override
    public void deleteManyRoles(RoleManyDeleteRequestDto req) {
        List<RoleEntity> roles = roleRepository.findAllById(req.getIds());
        if (roles.size() != req.getIds().size()) {
            throw new ResourceNotFoundException("error.role.not.found");
        }
        for (RoleEntity role : roles) {
            role.setIsDeleted(true);
            roleRepository.save(role);
            // Soft delete role permissions
            role.getRolePermissions().forEach(rolePermission -> {
                rolePermission.setIsDeleted(true);
                rolePermissionRepository.save(rolePermission);
            });
        }
    }
}

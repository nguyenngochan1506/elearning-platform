package dev.edu.ngochandev.authservice.mappers;

import dev.edu.ngochandev.authservice.dtos.res.AdminUserResponse;
import dev.edu.ngochandev.authservice.dtos.res.RoleSummaryResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class})
public interface UserMapper {
    UserResponseDto toResponseDto(UserEntity entity);

    @Mapping(source = "entity", target = "roles", qualifiedByName = "mapRolesInOrganization")
    @Mapping(source = "entity.updatedAt", target = "lastUpdatedAt")
    AdminUserResponse toAdminResponseDto(UserEntity entity, @Context Long organizationId);

    @Named("mapRolesInOrganization")
    default List<RoleSummaryResponseDto> mapRolesInOrganization(UserEntity user, @Context Long organizationId) {
        if (user == null || user.getUserOrganizationRoles() == null || organizationId == null) {
            return List.of();
        }
        RoleMapper roleMapper = new RoleMapperImpl();
        return user.getUserOrganizationRoles().stream()
                .filter(uor -> uor.getOrganization().getId().equals(organizationId) && !uor.getIsDeleted())
                .map(uor -> roleMapper.mapToSummaryResponseDto(uor.getRole()))
                .toList();
    }
}
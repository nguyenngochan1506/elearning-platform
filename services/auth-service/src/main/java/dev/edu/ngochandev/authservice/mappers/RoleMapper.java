package dev.edu.ngochandev.authservice.mappers;

import dev.edu.ngochandev.authservice.dtos.res.RoleDetailResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.RoleSummaryResponseDto;
import dev.edu.ngochandev.authservice.entities.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    RoleSummaryResponseDto mapToSummaryResponseDto(RoleEntity roleEntity);

    RoleDetailResponseDto mapToDetailResponseDto(RoleEntity roleEntity);
}

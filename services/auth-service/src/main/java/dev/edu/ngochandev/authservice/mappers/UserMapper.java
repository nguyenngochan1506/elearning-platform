package dev.edu.ngochandev.authservice.mappers;

import dev.edu.ngochandev.authservice.dtos.res.AdminUserResponse;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
	UserResponseDto toResponseDto(UserEntity entity);

	@Mapping(source = "updatedAt", target = "lastUpdatedAt")
	AdminUserResponse toAdminResponseDto(UserEntity entity);
}

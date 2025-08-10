package dev.edu.ngochandev.authservice.mappers;

import dev.edu.ngochandev.authservice.dtos.res.PermissionResponseDto;
import dev.edu.ngochandev.authservice.entities.PermissionEntity;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(PermissionMapperDecorator.class)
public interface PermissionMapper {
    @Mapping(source = "apiPath", target = "endpoint")
    PermissionResponseDto toResponseDto(PermissionEntity entity);
}

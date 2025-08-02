package dev.edu.ngochandev.authservice.mapper;

import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toResponseDto(UserEntity entity);
}

package dev.edu.ngochandev.socialservice.mappers;

import dev.edu.ngochandev.common.events.UserRegisteredEvent;
import dev.edu.ngochandev.socialservice.dtos.req.UserProfileUpdateRequestDto;
import dev.edu.ngochandev.socialservice.entities.UserProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
     UserProfileEntity toEntity(UserRegisteredEvent event);

     UserProfileEntity toEntity(UserProfileUpdateRequestDto requestDto);
}

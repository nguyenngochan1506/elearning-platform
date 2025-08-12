package dev.edu.ngochandev.socialservice.mappers;

import dev.edu.ngochandev.common.events.UserRegisteredEvent;
import dev.edu.ngochandev.socialservice.dtos.res.UserProfileResponseDto;
import dev.edu.ngochandev.socialservice.entities.UserProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
     UserProfileEntity toEntity(UserRegisteredEvent event);

     @Mapping(source = "id", target = "profileId")
     @Mapping(target = "socialMediaLinks", ignore = true)
     UserProfileResponseDto toDto(UserProfileEntity entity);
}

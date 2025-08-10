package dev.edu.ngochandev.socialservice.mappers;

import dev.edu.ngochandev.socialservice.entities.UserProfileEntity;
import dev.edu.ngochandev.socialservice.kafka.events.UserRegisteredEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
     UserProfileEntity toEntity(UserRegisteredEvent event);
}

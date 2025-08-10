package dev.edu.ngochandev.socialservice.mappers;

import dev.edu.ngochandev.socialservice.dtos.req.InternalUserProfileCreationRequest;
import dev.edu.ngochandev.socialservice.entities.UserProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
     UserProfileEntity toEntity(InternalUserProfileCreationRequest request);
}

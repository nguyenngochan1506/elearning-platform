package dev.edu.ngochandev.socialservice.services;

import dev.edu.ngochandev.socialservice.dtos.req.InternalUserProfileCreationRequest;

public interface UserProfileService
{
    String createUserProfile(InternalUserProfileCreationRequest req);
}

package dev.edu.ngochandev.socialservice.services;

import dev.edu.ngochandev.socialservice.dtos.req.UserProfileUpdateRequestDto;

public interface UserProfileService
{
    String updateProfile(UserProfileUpdateRequestDto req);
}

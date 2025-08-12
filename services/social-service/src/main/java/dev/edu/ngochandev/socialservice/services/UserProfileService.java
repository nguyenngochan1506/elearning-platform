package dev.edu.ngochandev.socialservice.services;

import dev.edu.ngochandev.socialservice.dtos.req.UserProfileUpdateRequestDto;
import dev.edu.ngochandev.socialservice.dtos.res.UserProfileResponseDto;

public interface UserProfileService
{
    String updateProfile(UserProfileUpdateRequestDto req);

    UserProfileResponseDto getMe(Long userId);
}

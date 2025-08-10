package dev.edu.ngochandev.socialservice.services.impl;

import dev.edu.ngochandev.socialservice.dtos.req.UserProfileUpdateRequestDto;
import dev.edu.ngochandev.socialservice.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    @Override
    public String updateProfile(UserProfileUpdateRequestDto req) {
        return "";
    }
}

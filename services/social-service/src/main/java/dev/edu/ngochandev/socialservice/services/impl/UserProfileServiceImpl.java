package dev.edu.ngochandev.socialservice.services.impl;

import dev.edu.ngochandev.socialservice.dtos.req.UserProfileUpdateRequestDto;
import dev.edu.ngochandev.socialservice.dtos.res.UserProfileResponseDto;
import dev.edu.ngochandev.socialservice.entities.UserProfileEntity;
import dev.edu.ngochandev.socialservice.mappers.UserProfileMapper;
import dev.edu.ngochandev.socialservice.repositories.UserProfileRepository;
import dev.edu.ngochandev.socialservice.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    @Override
    public String updateProfile(UserProfileUpdateRequestDto req) {
        return "";
    }

    @Override
    public UserProfileResponseDto getMe(Long userId) {
        UserProfileEntity userProfile = userProfileRepository.findByUserId(userId);
        return userProfileMapper.toDto(userProfile);
    }
}

package dev.edu.ngochandev.socialservice.services.impl;

import dev.edu.ngochandev.socialservice.dtos.req.UserProfileUpdateRequestDto;
import dev.edu.ngochandev.socialservice.dtos.res.UserProfileResponseDto;
import dev.edu.ngochandev.socialservice.entities.UserProfileEntity;
import dev.edu.ngochandev.socialservice.mappers.UserProfileMapper;
import dev.edu.ngochandev.socialservice.repositories.UserProfileRepository;
import dev.edu.ngochandev.socialservice.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    @Override
    public UserProfileResponseDto updateProfile(UserProfileUpdateRequestDto req, Long userId) {
        UserProfileEntity userProfile = userProfileRepository.findByUserId(userId);
        userProfile.setAvatar(req.getAvatar() != null ? req.getAvatar() : userProfile.getAvatar());
        userProfile.setDateOfBirth(req.getDateOfBirth() != null ? req.getDateOfBirth() : userProfile.getDateOfBirth());
        userProfile.setFullName(req.getFullName() != null ? req.getFullName() : userProfile.getFullName());
        userProfile.setPhoneNumber(req.getPhoneNumber() != null ? req.getPhoneNumber() : userProfile.getPhoneNumber());
        userProfile.setSchool(req.getSchool() != null ? req.getSchool() : userProfile.getSchool());
        userProfile.setSocialMediaLinks(req.getSocialMediaLinks() != null ? req.getSocialMediaLinks() : userProfile.getSocialMediaLinks());
        userProfile = userProfileRepository.save(userProfile);

        UserProfileResponseDto res = userProfileMapper.toDto(userProfile);
        res.setSocialMediaLinks(req.getSocialMediaLinks());
        return res;
    }

    @Override
    public UserProfileResponseDto getMe(Long userId) {
        UserProfileEntity userProfile = userProfileRepository.findByUserId(userId);
        return userProfileMapper.toDto(userProfile);
    }
}

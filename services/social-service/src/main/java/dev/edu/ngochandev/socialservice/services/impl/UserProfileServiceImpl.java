package dev.edu.ngochandev.socialservice.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.socialservice.dtos.req.UserProfileUpdateRequestDto;
import dev.edu.ngochandev.socialservice.dtos.res.UserProfileResponseDto;
import dev.edu.ngochandev.socialservice.entities.UserProfileEntity;
import dev.edu.ngochandev.socialservice.mappers.UserProfileMapper;
import dev.edu.ngochandev.socialservice.repositories.UserProfileRepository;
import dev.edu.ngochandev.socialservice.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final ObjectMapper objectMapper;
    @Override
    public UserProfileResponseDto updateProfile(UserProfileUpdateRequestDto req, Long userId) {
        UserProfileEntity userProfile = userProfileRepository.findByUserId(userId);
        userProfile.setAvatar(req.getAvatar() != null ? req.getAvatar() : userProfile.getAvatar());
        userProfile.setDateOfBirth(req.getDateOfBirth() != null ? req.getDateOfBirth() : userProfile.getDateOfBirth());
        userProfile.setFullName(req.getFullName() != null ? req.getFullName() : userProfile.getFullName());
        userProfile.setPhoneNumber(req.getPhoneNumber() != null ? req.getPhoneNumber() : userProfile.getPhoneNumber());
        userProfile.setSchool(req.getSchool() != null ? req.getSchool() : userProfile.getSchool());
        try{
            if (req.getSocialMediaLinks() != null) {
                String socialMediaLinksJson = objectMapper.writeValueAsString(req.getSocialMediaLinks());
                userProfile.setSocialMediaLinks(socialMediaLinksJson);
            }
        }catch (Exception e){
            log.error("Error while updating user profile: {}", e.getMessage());
            throw new RuntimeException("Failed to update user profile");
        }
        userProfile = userProfileRepository.save(userProfile);

        UserProfileResponseDto res = userProfileMapper.toDto(userProfile);
        res.setSocialMediaLinks(req.getSocialMediaLinks());
        return res;
    }

    @Override
    public UserProfileResponseDto getMe(Long userId) {
        UserProfileEntity userProfile = userProfileRepository.findByUserId(userId);
        UserProfileResponseDto dto = userProfileMapper.toDto(userProfile);
        try {
            if (userProfile.getSocialMediaLinks() != null) {
                Map<String, String> links = objectMapper.readValue(
                        userProfile.getSocialMediaLinks(),
                        new TypeReference<>() {}
                );
                dto.setSocialMediaLinks(links);
            }
        } catch (JsonProcessingException e) {
            log.error("Error deserializing social media links", e);
        }

        return dto;
    }
}

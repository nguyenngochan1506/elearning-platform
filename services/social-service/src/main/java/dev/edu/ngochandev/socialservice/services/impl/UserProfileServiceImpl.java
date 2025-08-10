package dev.edu.ngochandev.socialservice.services.impl;

import dev.edu.ngochandev.socialservice.dtos.req.InternalUserProfileCreationRequest;
import dev.edu.ngochandev.socialservice.entities.UserProfileEntity;
import dev.edu.ngochandev.socialservice.mappers.UserProfileMapper;
import dev.edu.ngochandev.socialservice.repositories.UserProfileRepository;
import dev.edu.ngochandev.socialservice.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public String createUserProfile(InternalUserProfileCreationRequest req) {
        UserProfileEntity userProfile = userProfileMapper.toEntity(req);
        return userProfileRepository.save(userProfile).getId();
    }
}

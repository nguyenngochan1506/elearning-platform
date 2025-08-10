package dev.edu.ngochandev.socialservice.services.impl;

import dev.edu.ngochandev.socialservice.repositories.UserProfileRepository;
import dev.edu.ngochandev.socialservice.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
}

package dev.edu.ngochandev.socialservice.controllers;

import dev.edu.ngochandev.socialservice.dtos.req.InternalUserProfileCreationRequest;
import dev.edu.ngochandev.socialservice.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.socialservice.services.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/internal/user-profile")
public class InternalUserProfileController {
    private final UserProfileService userProfileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<String> createUserProfile(@RequestBody @Valid InternalUserProfileCreationRequest req) {
        return SuccessResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("User profile created successfully")
                .data(userProfileService.createUserProfile(req))
                .build();
    }
}

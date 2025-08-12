package dev.edu.ngochandev.socialservice.controllers;

import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.socialservice.dtos.req.UserProfileUpdateRequestDto;
import dev.edu.ngochandev.socialservice.dtos.res.UserProfileResponseDto;
import dev.edu.ngochandev.socialservice.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<String> updateProfile(@RequestBody UserProfileUpdateRequestDto req) {
        return null;
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<UserProfileResponseDto> getMyProfile(@AuthenticationPrincipal Long userId) {
        return SuccessResponseDto.<UserProfileResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("user.profile.get-me.success")
                .data(userProfileService.getMe(userId))
                .build();
    }
}

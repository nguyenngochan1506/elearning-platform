package dev.edu.ngochandev.socialservice.controllers;

import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import dev.edu.ngochandev.socialservice.dtos.req.UserProfileUpdateRequestDto;
import dev.edu.ngochandev.socialservice.dtos.res.UserProfileResponseDto;
import dev.edu.ngochandev.socialservice.services.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final Translator translator;
    @PatchMapping()
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<UserProfileResponseDto> updateProfile(@RequestBody @Valid UserProfileUpdateRequestDto req, @AuthenticationPrincipal Long userId) {
        return SuccessResponseDto.<UserProfileResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("user.profile.update.success"))
                .data(userProfileService.updateProfile(req, userId))
                .build();
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<UserProfileResponseDto> getMyProfile(@AuthenticationPrincipal Long userId) {
        return SuccessResponseDto.<UserProfileResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("user.profile.get-me.success"))
                .data(userProfileService.getMe(userId))
                .build();
    }
}

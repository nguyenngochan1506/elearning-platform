package dev.edu.ngochandev.socialservice.controllers;

import dev.edu.ngochandev.socialservice.dtos.req.UserProfileUpdateRequestDto;
import dev.edu.ngochandev.socialservice.dtos.res.SuccessResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-profile")
public class UserProfileController {
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<String> updateProfile(@RequestBody UserProfileUpdateRequestDto req) {
        return null;
    }
}

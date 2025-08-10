package dev.edu.ngochandev.socialservice.dtos.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@Builder
public class UserProfileUpdateRequestDto {
    private String profileId;
    @NotBlank(message = "fullName must not be blank")
    private String fullName;
    private String avatar;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String school;
    private Map<String, String> socialMediaLinks;
}

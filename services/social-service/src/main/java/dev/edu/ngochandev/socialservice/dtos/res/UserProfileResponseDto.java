package dev.edu.ngochandev.socialservice.dtos.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@Builder
public class UserProfileResponseDto {
    private Long id;
    private String fullName;
    private String avatar;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String school;
    private Map<String, String> socialMediaLinks;
}

package dev.edu.ngochandev.socialservice.dtos.req;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class UserProfileCreationRequest {
    private Long userId;

    private String fullName;

    private String avatar;

    private String dateOfBirth;

    private String phoneNumber;

    private String school;

    private Map<String, String> socialMediaLinks;
}

package dev.edu.ngochandev.socialservice.dtos.req;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String fullName;
    private String avatar;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String school;
    private Map<String, String> socialMediaLinks;
}

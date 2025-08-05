package dev.edu.ngochandev.authservice.dtos.res;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleSummaryResponseDto {
    private Long id;
    private String name;
    private String description;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
}

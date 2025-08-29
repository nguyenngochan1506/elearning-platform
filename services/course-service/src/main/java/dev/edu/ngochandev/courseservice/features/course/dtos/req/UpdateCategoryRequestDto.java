package dev.edu.ngochandev.courseservice.features.course.dtos.req;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryRequestDto extends CreateCategoryRequestDto {
    @NotNull(message = "error.id.not-null")
    private String uuid;
}

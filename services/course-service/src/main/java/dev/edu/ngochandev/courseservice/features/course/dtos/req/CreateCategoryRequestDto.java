package dev.edu.ngochandev.courseservice.features.course.dtos.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequestDto {
    @NotBlank(message = "error.name.not-blank")
    private String name;

    private String description;

    @NotBlank(message = "error.slug.not-blank")
    private String slug;

    private Long parentId;
}

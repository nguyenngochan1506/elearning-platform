package dev.edu.ngochandev.courseservice.features.course.dtos.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateCourseRequestDto {
    @NotBlank(message = "error.name.not-blank", groups = OnCreate.class)
    private String name;
    private String description;
    private String thumbnail;
    private Boolean isPublic;
    @NotBlank(message = "error.slug.not-blank", groups = OnCreate.class)
    private String slug;

    private List<String> categoryUuids;
}

package dev.edu.ngochandev.courseservice.features.course.dtos.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChapterRequestDto {
    @NotBlank(message = "error.name.not-blank")
    private String name;
    private String description;
    private Integer order;
    @NotBlank(message = "error.id.not-blank")
    private String courseUuid;
}

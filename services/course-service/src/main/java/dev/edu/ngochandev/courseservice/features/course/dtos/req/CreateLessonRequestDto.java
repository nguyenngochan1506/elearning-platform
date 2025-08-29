package dev.edu.ngochandev.courseservice.features.course.dtos.req;

import dev.edu.ngochandev.courseservice.commons.enums.LessonType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLessonRequestDto {
    @NotBlank(message = "error.name.not-blank")
    private String name;
    private String description;
    @NotNull(message = "error.lesson-type.not-blank")
    private LessonType lessonType;
    private String content;
    private Integer order;
    @NotBlank(message = "error.uuid.not-blank")
    private String chapterUuid;
}

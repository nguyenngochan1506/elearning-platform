package dev.edu.ngochandev.courseservice.features.course.dtos.req;

import dev.edu.ngochandev.courseservice.commons.enums.LessonType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLessonRequestDto {
    private String uuid;
    private String name;
    private String description;
    private LessonType lessonType;
    private String content;
    private Integer order;
}

package dev.edu.ngochandev.courseservice.features.course.dtos.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCourseRequestDto extends CreateCourseRequestDto{
    private String uuid;
    private String slug;
}

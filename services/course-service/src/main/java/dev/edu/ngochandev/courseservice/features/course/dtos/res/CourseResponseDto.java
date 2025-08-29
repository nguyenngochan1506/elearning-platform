package dev.edu.ngochandev.courseservice.features.course.dtos.res;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseResponseDto {
    private String uuid;
    private String name;
    private String description;
    private String thumbnail;
    private Boolean isPublic;
    private String slug;
    private List<CategoryResponseDto> categories;
}

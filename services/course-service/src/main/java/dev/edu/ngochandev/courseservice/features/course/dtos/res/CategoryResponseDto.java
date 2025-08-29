package dev.edu.ngochandev.courseservice.features.course.dtos.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String description;
    private String slug;
    private String breadcrumb;
}

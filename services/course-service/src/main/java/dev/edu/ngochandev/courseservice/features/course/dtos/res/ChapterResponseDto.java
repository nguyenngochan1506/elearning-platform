package dev.edu.ngochandev.courseservice.features.course.dtos.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterResponseDto {
    private String uuid;
    private String name;
    private String description;
    private Integer order;
}

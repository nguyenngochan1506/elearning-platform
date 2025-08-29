package dev.edu.ngochandev.courseservice.features.course.dtos.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateChapterRequestDto {
    private String uuid;
    private String name;
    private String description;
    private Integer order;
}

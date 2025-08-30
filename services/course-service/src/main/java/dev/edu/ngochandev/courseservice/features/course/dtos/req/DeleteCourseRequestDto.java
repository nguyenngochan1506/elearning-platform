package dev.edu.ngochandev.courseservice.features.course.dtos.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeleteCourseRequestDto {
    private List<String> uuids;
}

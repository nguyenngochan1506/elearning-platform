package dev.edu.ngochandev.courseservice.features.course.dtos.res;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseDetailResponseDto extends CourseResponseDto{
    private Integer numberOfLessons;
    private Integer numberOfChapters;
    private List<ChapterDetailResponseDto> chapters;
}

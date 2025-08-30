package dev.edu.ngochandev.courseservice.features.course.dtos.res;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChapterDetailResponseDto extends ChapterResponseDto{
    private Integer numberOfLessons;
    private List<LessonResponseDto> lessons;
}

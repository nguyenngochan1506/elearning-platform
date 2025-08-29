package dev.edu.ngochandev.courseservice.features.course.services;

import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateLessonRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.LessonResponseDto;

public interface LessonService {
    LessonResponseDto createLesson(CreateLessonRequestDto req);
    LessonResponseDto updateLesson()
}

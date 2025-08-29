package dev.edu.ngochandev.courseservice.features.course.services;

import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateCourseRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseResponseDto;

public interface CourseService {
    CourseResponseDto createCourse(CreateCourseRequestDto req);
}

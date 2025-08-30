package dev.edu.ngochandev.courseservice.features.course.services;

import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateCourseRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.UpdateCourseRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseDetailResponseDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseResponseDto;

public interface CourseService {
    CourseResponseDto createCourse(CreateCourseRequestDto req, Long userId);

    CourseResponseDto updateCourse(UpdateCourseRequestDto req);

    PageResponseDto<CourseResponseDto> getCourses(AdvancedFilterRequestDto filter);

    CourseDetailResponseDto getCourseDetail(String uuid);
}

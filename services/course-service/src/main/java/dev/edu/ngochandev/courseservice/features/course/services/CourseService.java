package dev.edu.ngochandev.courseservice.features.course.services;

import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateCourseRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.UpdateCourseRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseDetailResponseDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseResponseDto;

import java.util.List;

public interface CourseService {
    CourseResponseDto createCourse(CreateCourseRequestDto req, Long userId);

    CourseResponseDto updateCourse(UpdateCourseRequestDto req, Long userId);

    PageResponseDto<CourseResponseDto> getCourses(AdvancedFilterRequestDto filter);

    CourseDetailResponseDto getCourseDetail(String uuid);

    Integer deleteCourse(List<String> uuids, Long userId);
}

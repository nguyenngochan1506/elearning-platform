package dev.edu.ngochandev.courseservice.features.course.controllers;

import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateCourseRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.OnCreate;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.OnUpdate;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.UpdateCourseRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseResponseDto;
import dev.edu.ngochandev.courseservice.features.course.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final Translator translator;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<CourseResponseDto> createCourse(@RequestBody @Validated(OnCreate.class) CreateCourseRequestDto req) {
        return SuccessResponseDto.<CourseResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(translator.translate("course.create.success"))
                .data(courseService.createCourse(req))
                .build();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<CourseResponseDto> updateCourse(@RequestBody @Validated(OnUpdate.class) UpdateCourseRequestDto req) {
        return SuccessResponseDto.<CourseResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("course.update.success"))
                .data(courseService.updateCourse(req))
                .build();
    }

}

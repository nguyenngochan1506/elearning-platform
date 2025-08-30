package dev.edu.ngochandev.courseservice.features.course.controllers;

import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.*;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseDetailResponseDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseResponseDto;
import dev.edu.ngochandev.courseservice.features.course.services.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final Translator translator;


    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<CourseDetailResponseDto> getCourseDetail(@PathVariable String uuid){
        return SuccessResponseDto.<CourseDetailResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("course.get-detail.success"))
                .data(courseService.getCourseDetail(uuid))
                .build();
    }

    @PostMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<PageResponseDto<CourseResponseDto>> getCourses(@RequestBody @Valid AdvancedFilterRequestDto filter){
        return SuccessResponseDto.<PageResponseDto<CourseResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("course.get-all.success"))
                .data(courseService.getCourses(filter))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<CourseResponseDto> createCourse(@RequestBody @Validated(OnCreate.class) CreateCourseRequestDto req, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("userId");
        return SuccessResponseDto.<CourseResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(translator.translate("course.create.success"))
                .data(courseService.createCourse(req, userId))
                .build();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('SCOPE_super_admin') or @courseSecurityService.isOwner(#req.uuid, #jwt)")
    public SuccessResponseDto<CourseResponseDto> updateCourse(@RequestBody @Validated(OnUpdate.class) UpdateCourseRequestDto req, @AuthenticationPrincipal Jwt jwt) {
        return SuccessResponseDto.<CourseResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("course.update.success"))
                .data(courseService.updateCourse(req))
                .build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('SCOPE_super_admin') or @courseSecurityService.isOwner(#uuids, #jwt)")
    public SuccessResponseDto<Integer> deleteCourse(@RequestBody DeleteCourseRequestDto req, @AuthenticationPrincipal Jwt jwt) {
        return SuccessResponseDto.<Integer>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("course.delete.success"))
                .data(courseService.deleteCourse(req.getUuids()))
                .build();
    }
}

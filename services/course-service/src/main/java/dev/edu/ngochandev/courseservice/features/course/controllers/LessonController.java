package dev.edu.ngochandev.courseservice.features.course.controllers;

import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateLessonRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.UpdateLessonRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.LessonResponseDto;
import dev.edu.ngochandev.courseservice.features.course.services.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;
    private final Translator translator;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<LessonResponseDto> createLesson(@RequestBody @Valid CreateLessonRequestDto req) {
        return SuccessResponseDto.<LessonResponseDto>builder()
                .message(translator.translate("lesson.create.success"))
                .status(HttpStatus.OK.value())
                .data(lessonService.createLesson(req))
                .build();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<LessonResponseDto> updateLesson(@RequestBody UpdateLessonRequestDto req) {
        return SuccessResponseDto.<LessonResponseDto>builder()
                .message(translator.translate("lesson.updated.success"))
                .status(HttpStatus.OK.value())
                .data(lessonService.updateLesson(req))
                .build();
    }
}

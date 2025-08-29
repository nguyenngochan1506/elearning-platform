package dev.edu.ngochandev.courseservice.features.course.controllers;

import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateChapterRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.UpdateChapterRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.ChapterResponseDto;
import dev.edu.ngochandev.courseservice.features.course.services.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses/chapters")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;
    private final Translator translator;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<ChapterResponseDto> createChapter(@RequestBody @Valid CreateChapterRequestDto req){
        return SuccessResponseDto.<ChapterResponseDto>builder()
                .message(translator.translate("chapter.create.success"))
                .status(HttpStatus.OK.value())
                .data(chapterService.createChapter(req))
                .build();
    }
    @PatchMapping
    public SuccessResponseDto<ChapterResponseDto> updateChapter(@RequestBody @Valid UpdateChapterRequestDto req){
        return SuccessResponseDto.<ChapterResponseDto>builder()
                .message(translator.translate("chapter.update.success"))
                .status(HttpStatus.OK.value())
                .data(chapterService.updateChapter(req))
                .build();
    }
}

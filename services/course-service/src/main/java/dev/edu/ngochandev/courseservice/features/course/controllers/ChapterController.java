package dev.edu.ngochandev.courseservice.features.course.controllers;

import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateChapterRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.DeleteChaptersRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.UpdateChapterRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.ChapterResponseDto;
import dev.edu.ngochandev.courseservice.features.course.services.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses/chapters")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;
    private final Translator translator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<ChapterResponseDto> createChapter(@RequestBody @Valid CreateChapterRequestDto req, @AuthenticationPrincipal Jwt jwt){
        Long userId = jwt.getClaim("userId");
        return SuccessResponseDto.<ChapterResponseDto>builder()
                .message(translator.translate("chapter.create.success"))
                .status(HttpStatus.OK.value())
                .data(chapterService.createChapter(req, userId))
                .build();
    }
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('SCOPE_super_admin') or @courseSecurityService.isOwner(#req.uuid, #jwt)")
    public SuccessResponseDto<ChapterResponseDto> updateChapter(@RequestBody @Valid UpdateChapterRequestDto req, @AuthenticationPrincipal Jwt jwt){
        return SuccessResponseDto.<ChapterResponseDto>builder()
                .message(translator.translate("chapter.update.success"))
                .status(HttpStatus.OK.value())
                .data(chapterService.updateChapter(req))
                .build();
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('SCOPE_super_admin') or @courseSecurityService.isOwner(#req.uuids, #jwt)")
    public SuccessResponseDto<Integer> deleteChapters(@RequestBody @Valid DeleteChaptersRequestDto req, @AuthenticationPrincipal Jwt jwt){
        return SuccessResponseDto.<Integer>builder()
                .message(translator.translate("chapter.deleted.success"))
                .status(HttpStatus.OK.value())
                .data(chapterService.deleteChapter(req))
                .build();
    }
}

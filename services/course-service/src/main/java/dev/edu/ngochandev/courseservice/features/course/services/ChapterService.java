package dev.edu.ngochandev.courseservice.features.course.services;

import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateChapterRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.ChapterResponseDto;

public interface ChapterService {
    ChapterResponseDto createChapter(CreateChapterRequestDto req);
}

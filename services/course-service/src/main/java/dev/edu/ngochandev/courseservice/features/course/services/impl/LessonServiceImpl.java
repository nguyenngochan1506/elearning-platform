package dev.edu.ngochandev.courseservice.features.course.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.common.exceptions.ResourceNotFoundException;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateLessonRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.LessonResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.ChapterEntity;
import dev.edu.ngochandev.courseservice.features.course.entities.LessonEntity;
import dev.edu.ngochandev.courseservice.features.course.mappers.LessonMapper;
import dev.edu.ngochandev.courseservice.features.course.repositories.ChapterRepository;
import dev.edu.ngochandev.courseservice.features.course.repositories.LessonRepository;
import dev.edu.ngochandev.courseservice.features.course.services.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "LESSON-SERVICE")
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final ChapterRepository chapterRepository;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final ObjectMapper objectMapper;

    @Override
    public LessonResponseDto createLesson(CreateLessonRequestDto req) {
        ChapterEntity chapter = chapterRepository.findByUuid(req.getChapterUuid()).orElseThrow(() -> new ResourceNotFoundException("error.chapter.not-found"));

        LessonEntity lessonEntity = new LessonEntity();
        lessonEntity.setName(req.getName());
        lessonEntity.setDescription(req.getDescription());
        lessonEntity.setLessonType(req.getLessonType());
        lessonEntity.setContent(req.getContent());
        lessonEntity.setChapter(chapter);

        lessonRepository.save(lessonEntity);
        return lessonMapper.toResponseDto(lessonEntity);
    }
}

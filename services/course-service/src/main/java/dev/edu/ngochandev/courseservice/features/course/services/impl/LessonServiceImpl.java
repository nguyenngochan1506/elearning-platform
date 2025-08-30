package dev.edu.ngochandev.courseservice.features.course.services.impl;

import dev.edu.ngochandev.common.exceptions.ResourceNotFoundException;
import dev.edu.ngochandev.courseservice.commons.enums.AuthorshipType;
import dev.edu.ngochandev.courseservice.commons.enums.ResourceType;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateLessonRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.UpdateLessonRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.LessonResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.ChapterEntity;
import dev.edu.ngochandev.courseservice.features.course.entities.LessonEntity;
import dev.edu.ngochandev.courseservice.features.course.entities.ResourceAuthorEntity;
import dev.edu.ngochandev.courseservice.features.course.mappers.LessonMapper;
import dev.edu.ngochandev.courseservice.features.course.repositories.ChapterRepository;
import dev.edu.ngochandev.courseservice.features.course.repositories.LessonRepository;
import dev.edu.ngochandev.courseservice.features.course.repositories.ResourceAuthorRepository;
import dev.edu.ngochandev.courseservice.features.course.services.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "LESSON-SERVICE")
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final ChapterRepository chapterRepository;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final ResourceAuthorRepository resourceAuthorRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LessonResponseDto createLesson(CreateLessonRequestDto req, Long userId) {
        ChapterEntity chapter = chapterRepository.findByUuid(req.getChapterUuid()).orElseThrow(() -> new ResourceNotFoundException("error.chapter.not-found"));

        LessonEntity lessonEntity = new LessonEntity();
        lessonEntity.setName(req.getName());
        lessonEntity.setDescription(req.getDescription());
        lessonEntity.setLessonType(req.getLessonType());
        lessonEntity.setContent(req.getContent());
        lessonEntity.setChapter(chapter);

        lessonEntity = lessonRepository.save(lessonEntity);

        ResourceAuthorEntity author = new ResourceAuthorEntity();
        author.setUserId(userId);
        author.setResourceUuid(lessonEntity.getUuid());
        author.setAuthorshipType(AuthorshipType.AUTHOR);
        author.setResourceType(ResourceType.LESSON);
        resourceAuthorRepository.save(author);

        return lessonMapper.toResponseDto(lessonEntity);
    }

    @Override
    public LessonResponseDto updateLesson(UpdateLessonRequestDto req) {
        LessonEntity lesson = lessonRepository.findByUuid(req.getUuid()).orElseThrow(() -> new ResourceNotFoundException("error.lesson.not-found"));
        lesson.setName(req.getName() != null ? req.getName() : lesson.getName());
        lesson.setDescription(req.getDescription() != null ? req.getDescription() : lesson.getDescription());
        lesson.setContent(req.getContent() != null ? req.getContent() : lesson.getContent());
        lesson.setLessonType(req.getLessonType() != null ? req.getLessonType() : lesson.getLessonType());
        lesson.setOrder(req.getOrder() != null ? req.getOrder() : lesson.getOrder());
        lessonRepository.save(lesson);

        return lessonMapper.toResponseDto(lesson);
    }
}

package dev.edu.ngochandev.courseservice.features.course.services.impl;

import dev.edu.ngochandev.common.exceptions.ResourceNotFoundException;
import dev.edu.ngochandev.courseservice.commons.enums.AuthorshipType;
import dev.edu.ngochandev.courseservice.commons.enums.ResourceType;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateChapterRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.UpdateChapterRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.ChapterResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.ChapterEntity;
import dev.edu.ngochandev.courseservice.features.course.entities.CourseEntity;
import dev.edu.ngochandev.courseservice.features.course.entities.ResourceAuthorEntity;
import dev.edu.ngochandev.courseservice.features.course.mappers.ChapterMapper;
import dev.edu.ngochandev.courseservice.features.course.repositories.ChapterRepository;
import dev.edu.ngochandev.courseservice.features.course.repositories.CourseRepository;
import dev.edu.ngochandev.courseservice.features.course.repositories.ResourceAuthorRepository;
import dev.edu.ngochandev.courseservice.features.course.services.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "CHAPTER-SERVICE")
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final ChapterMapper chapterMapper;
    private final ResourceAuthorRepository resourceAuthorRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChapterResponseDto createChapter(CreateChapterRequestDto req, Long userId) {
        ChapterEntity chapter = new ChapterEntity();
        chapter.setName(req.getName());
        chapter.setDescription(req.getDescription());
        chapter.setOrder(req.getOrder());

        CourseEntity course = courseRepository.findByUuid(req.getCourseUuid()).orElseThrow(() -> new ResourceNotFoundException("error.course.not-found"));
        chapter.setCourse(course);

        chapterRepository.save(chapter);

        ResourceAuthorEntity author = new ResourceAuthorEntity();
        author.setAuthorshipType(AuthorshipType.AUTHOR);
        author.setResourceType(ResourceType.CHAPTER);
        author.setResourceUuid(chapter.getUuid());
        author.setUserId(userId);
        resourceAuthorRepository.save(author);
        return chapterMapper.toResponseDto(chapter);
    }

    @Override
    public ChapterResponseDto updateChapter(UpdateChapterRequestDto req) {
        ChapterEntity chapter = chapterRepository.findByUuid(req.getUuid()).orElseThrow(() -> new ResourceNotFoundException("error.chapter.not-found"));
        chapter.setName(req.getName() != null ? req.getName() : chapter.getName());
        chapter.setDescription(req.getDescription() != null ? req.getDescription() : chapter.getDescription());
        chapter.setOrder(req.getOrder() != null ? req.getOrder() : chapter.getOrder());
        chapterRepository.save(chapter);
        return chapterMapper.toResponseDto(chapter);
    }
}

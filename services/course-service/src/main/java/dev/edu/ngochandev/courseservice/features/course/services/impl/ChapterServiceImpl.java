package dev.edu.ngochandev.courseservice.features.course.services.impl;

import dev.edu.ngochandev.common.exceptions.ResourceNotFoundException;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateChapterRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.ChapterResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.ChapterEntity;
import dev.edu.ngochandev.courseservice.features.course.entities.CourseEntity;
import dev.edu.ngochandev.courseservice.features.course.mappers.ChapterMapper;
import dev.edu.ngochandev.courseservice.features.course.repositories.ChapterRepository;
import dev.edu.ngochandev.courseservice.features.course.repositories.CourseRepository;
import dev.edu.ngochandev.courseservice.features.course.services.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "CHAPTER-SERVICE")
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final ChapterMapper chapterMapper;

    @Override
    public ChapterResponseDto createChapter(CreateChapterRequestDto req) {
        ChapterEntity chapter = new ChapterEntity();
        chapter.setName(req.getName());
        chapter.setDescription(req.getDescription());
        chapter.setOrder(req.getOrder());

        CourseEntity course = courseRepository.findByUuid(req.getCourseUuid()).orElseThrow(() -> new ResourceNotFoundException("error.course.not-found"));
        chapter.setCourse(course);

        chapterRepository.save(chapter);
        return chapterMapper.toResponseDto(chapter);
    }
}

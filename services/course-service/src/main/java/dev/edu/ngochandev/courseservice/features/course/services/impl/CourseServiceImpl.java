package dev.edu.ngochandev.courseservice.features.course.services.impl;

import dev.edu.ngochandev.common.exceptions.DuplicateResourceException;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateCourseRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.CategoryEntity;
import dev.edu.ngochandev.courseservice.features.course.entities.CourseEntity;
import dev.edu.ngochandev.courseservice.features.course.mappers.CourseMapper;
import dev.edu.ngochandev.courseservice.features.course.repositories.CategoryRepository;
import dev.edu.ngochandev.courseservice.features.course.repositories.CourseRepository;
import dev.edu.ngochandev.courseservice.features.course.services.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "COURSE-SERVICE")
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponseDto createCourse(CreateCourseRequestDto req) {
        if(courseRepository.existsBySlug(req.getSlug())) {
            throw new DuplicateResourceException("error.slug.exists");
        }
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setName(req.getName());
        courseEntity.setSlug(req.getSlug());
        courseEntity.setDescription(req.getDescription());
        courseEntity.setThumbnail(req.getThumbnail());
        courseEntity.setIsPublic(req.getIsPublic());

        if(req.getCategoryUuids() != null && !req.getCategoryUuids().isEmpty()){
            List<CategoryEntity> categoryEntities = categoryRepository.findAllByUuid(req.getCategoryUuids());
            courseEntity.setCategories(new HashSet<>(categoryEntities));
        }
        courseRepository.save(courseEntity);
        return courseMapper.toResponseDto(courseEntity);
    }
}

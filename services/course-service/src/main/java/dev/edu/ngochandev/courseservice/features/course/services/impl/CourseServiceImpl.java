package dev.edu.ngochandev.courseservice.features.course.services.impl;

import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import dev.edu.ngochandev.common.exceptions.DuplicateResourceException;
import dev.edu.ngochandev.common.exceptions.ResourceNotFoundException;
import dev.edu.ngochandev.courseservice.commons.MyUtils;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateCourseRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.UpdateCourseRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.CategoryEntity;
import dev.edu.ngochandev.courseservice.features.course.entities.CourseEntity;
import dev.edu.ngochandev.courseservice.features.course.mappers.CourseMapper;
import dev.edu.ngochandev.courseservice.features.course.repositories.CategoryRepository;
import dev.edu.ngochandev.courseservice.features.course.repositories.CourseRepository;
import dev.edu.ngochandev.courseservice.features.course.services.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;

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

    @Override
    public CourseResponseDto updateCourse(UpdateCourseRequestDto req) {
        CourseEntity course = courseRepository.findByUuid(req.getUuid()).orElseThrow(() -> new ResourceNotFoundException("error.course.not-found"));
        course.setName(req.getName() != null ? req.getName() : course.getName());
        course.setDescription(req.getDescription() != null ? req.getDescription() : course.getDescription());
        course.setThumbnail(req.getThumbnail() != null ? req.getThumbnail() : course.getThumbnail());
        course.setIsPublic(req.getIsPublic() != null ? req.getIsPublic() : course.getIsPublic());
        course.setSlug(req.getSlug() != null ? req.getSlug() : course.getSlug());

        if(req.getCategoryUuids() != null && !req.getCategoryUuids().isEmpty()){
            List<CategoryEntity> categoryEntities = categoryRepository.findAllByUuid(req.getCategoryUuids());
            course.setCategories(new HashSet<>(categoryEntities));
        }
        courseRepository.save(course);
        return courseMapper.toResponseDto(course);
    }

    @Override
    public PageResponseDto<CourseResponseDto> getCourses(AdvancedFilterRequestDto filter) {
        // pageable
        Pageable pageable = MyUtils.createPageable(filter);
        // search
        Page<CourseEntity> pageItems = null;

        if (StringUtils.hasLength(filter.getSearch())) {
            pageItems = courseRepository.findBySearch("%" + filter.getSearch() + "%", pageable);
        } else {
            pageItems = courseRepository.findAll(pageable);
        }

        List<CourseResponseDto> itemsResponse =
                pageItems.map(courseMapper::toResponseDto).toList();

        return PageResponseDto.<CourseResponseDto>builder()
                .currentPage(filter.getPage())
                .totalElements(pageItems.getTotalElements())
                .totalPages(pageItems.getTotalPages())
                .items(itemsResponse)
                .build();
    }
}

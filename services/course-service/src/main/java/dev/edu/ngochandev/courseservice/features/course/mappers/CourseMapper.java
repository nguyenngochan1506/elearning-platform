package dev.edu.ngochandev.courseservice.features.course.mappers;

import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseDetailResponseDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, ChapterMapper.class})
public interface CourseMapper {

    CourseResponseDto toResponseDto(CourseEntity entity);

    @Mapping(target = "numberOfChapters", expression = "java(entity.getChapters() != null ? entity.getChapters().size() : 0)")
    @Mapping(target = "numberOfLessons", expression = "java(entity.getChapters() != null ? entity.getChapters().stream().mapToInt(chapter -> chapter.getLessons() != null ? chapter.getLessons().size() : 0).sum() : 0)")
    CourseDetailResponseDto toDetailResponseDto(CourseEntity entity);
}

package dev.edu.ngochandev.courseservice.features.course.mappers;

import dev.edu.ngochandev.courseservice.features.course.dtos.res.LessonResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.LessonEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    LessonResponseDto toResponseDto(LessonEntity entity);
}

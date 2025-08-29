package dev.edu.ngochandev.courseservice.features.course.mappers;

import dev.edu.ngochandev.courseservice.features.course.dtos.res.CourseResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface CourseMapper {

    CourseResponseDto toResponseDto(CourseEntity entity);
}

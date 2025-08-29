package dev.edu.ngochandev.courseservice.features.course.mappers;

import dev.edu.ngochandev.courseservice.features.course.dtos.res.ChapterResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.ChapterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChapterMapper {
    @Mapping(source = "uuid", target = "uuid")
    ChapterResponseDto toResponseDto(ChapterEntity entity);
}

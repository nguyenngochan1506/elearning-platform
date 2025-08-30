package dev.edu.ngochandev.courseservice.features.course.mappers;

import dev.edu.ngochandev.courseservice.features.course.dtos.res.ChapterDetailResponseDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.ChapterResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.ChapterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface ChapterMapper {
    @Mapping(source = "uuid", target = "uuid")
    ChapterResponseDto toResponseDto(ChapterEntity entity);

    @Mapping(target = "numberOfLessons", expression = "java(entity.getLessons() != null ? entity.getLessons().size() : 0)")
    ChapterDetailResponseDto toDetailResponseDto(ChapterEntity entity);
}

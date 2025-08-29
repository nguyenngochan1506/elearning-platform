package dev.edu.ngochandev.courseservice.features.course.mappers;

import dev.edu.ngochandev.courseservice.features.course.dtos.res.CategoryResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "entity", target = "breadcrumb", qualifiedByName = "generateBreadcrumb")
    CategoryResponseDto toResponseDto(CategoryEntity entity);

    @Named("generateBreadcrumb")
    default String generateBreadcrumb(CategoryEntity entity) {
        StringBuilder breadcrumb = new StringBuilder();

        CategoryEntity current = entity;
        while (current != null) {
            if (breadcrumb.isEmpty()) {
                breadcrumb.insert(0, current.getName());
            } else {
                breadcrumb.insert(0, current.getName() + " > ");
            }
            current = current.getParent();
        }
        return breadcrumb.toString();
    }
}

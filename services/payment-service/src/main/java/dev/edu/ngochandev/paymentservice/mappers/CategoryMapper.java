package dev.edu.ngochandev.paymentservice.mappers;

import dev.edu.ngochandev.paymentservice.dtos.res.CategoryResponseDto;
import dev.edu.ngochandev.paymentservice.entities.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toResponseDto(CategoryEntity entity);
}

package dev.edu.ngochandev.courseservice.features.course.services;

import dev.edu.ngochandev.common.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateCategoryRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.DeleteCategoryRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.UpdateCategoryRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CategoryResponseDto;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto createCategory(CreateCategoryRequestDto req);

    PageResponseDto<CategoryResponseDto> getAllCategories(SimpleFilterRequestDto filter);

    CategoryResponseDto updateCate( UpdateCategoryRequestDto req);

    List<Long> deleteCate(DeleteCategoryRequestDto req) throws BadRequestException;
}

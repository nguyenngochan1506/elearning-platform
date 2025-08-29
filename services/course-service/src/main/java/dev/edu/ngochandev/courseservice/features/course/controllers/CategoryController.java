package dev.edu.ngochandev.courseservice.features.course.controllers;

import dev.edu.ngochandev.common.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateCategoryRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.DeleteCategoryRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.UpdateCategoryRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CategoryResponseDto;
import dev.edu.ngochandev.courseservice.features.course.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final Translator translator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<CategoryResponseDto> createCategory(@RequestBody @Valid CreateCategoryRequestDto req) {
        return SuccessResponseDto.<CategoryResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(translator.translate("category.created.success"))
                .data(categoryService.createCategory(req))
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<PageResponseDto<CategoryResponseDto>> getAllCategories(@RequestBody @Valid SimpleFilterRequestDto filter){
        return SuccessResponseDto.<PageResponseDto<CategoryResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("category.list.success"))
                .data(categoryService.getAllCategories(filter))
                .build();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<CategoryResponseDto> updateCategory(@RequestBody @Valid UpdateCategoryRequestDto req) {
        return SuccessResponseDto.<CategoryResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(translator.translate("category.updated.success"))
                .data(categoryService.updateCate(req))
                .build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<List<Long>> deleteCate(@RequestBody @Valid DeleteCategoryRequestDto req) throws BadRequestException {
        return SuccessResponseDto.<List<Long>>builder()
                .status(HttpStatus.OK.value())
                .message(translator.translate("category.deleted.success"))
                .data(categoryService.deleteCate(req))
                .build();
    }
}

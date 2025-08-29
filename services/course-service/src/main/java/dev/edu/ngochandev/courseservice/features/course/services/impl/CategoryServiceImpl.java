package dev.edu.ngochandev.courseservice.features.course.services.impl;

import dev.edu.ngochandev.common.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import dev.edu.ngochandev.common.exceptions.DuplicateResourceException;
import dev.edu.ngochandev.common.exceptions.ResourceNotFoundException;
import dev.edu.ngochandev.courseservice.commons.MyUtils;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.CreateCategoryRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.DeleteCategoryRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.req.UpdateCategoryRequestDto;
import dev.edu.ngochandev.courseservice.features.course.dtos.res.CategoryResponseDto;
import dev.edu.ngochandev.courseservice.features.course.entities.CategoryEntity;
import dev.edu.ngochandev.courseservice.features.course.mappers.CategoryMapper;
import dev.edu.ngochandev.courseservice.features.course.repositories.CategoryRepository;
import dev.edu.ngochandev.courseservice.features.course.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j(topic = "CATEGORY-SERVICE")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    @Override
    public CategoryResponseDto createCategory(CreateCategoryRequestDto req) {
        if(categoryRepository.existsBySlug(req.getSlug())) {
            throw new DuplicateResourceException("error.slug.exists");
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(req.getName());
        categoryEntity.setDescription(req.getDescription());
        categoryEntity.setSlug(req.getSlug());

        if(req.getParentId() != null) {
            CategoryEntity parent = categoryRepository.findById(req.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("error.category.not_found"));
            categoryEntity.setParent(parent);
        }
        categoryRepository.save(categoryEntity);
        return mapper.toResponseDto(categoryEntity);
    }

    @Override
    public PageResponseDto<CategoryResponseDto> getAllCategories(SimpleFilterRequestDto filter) {
        // pageable
        Pageable pageable = MyUtils.createPageable(filter);
        // search
        Page<CategoryEntity> pageItems = null;

        if (StringUtils.hasLength(filter.getSearch())) {
            pageItems = categoryRepository.findBySearch("%" + filter.getSearch() + "%", pageable);
        } else {
            pageItems = categoryRepository.findAll(pageable);
        }

        List<CategoryResponseDto> itemsResponse =
                pageItems.map(mapper::toResponseDto).toList();

        return PageResponseDto.<CategoryResponseDto>builder()
                .currentPage(filter.getPage())
                .totalElements(pageItems.getTotalElements())
                .totalPages(pageItems.getTotalPages())
                .items(itemsResponse)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CategoryResponseDto updateCate(UpdateCategoryRequestDto req) {
        CategoryEntity cate = categoryRepository.findById(req.getId()).orElseThrow(() -> new ResourceNotFoundException("error.category.not_found"));
        cate.setName(req.getName() != null ? req.getName() : cate.getName());
        cate.setDescription(req.getDescription() != null ? req.getDescription() : cate.getDescription());
        cate.setSlug(req.getSlug() != null ? req.getSlug() : cate.getSlug());
        if(req.getParentId() != null) {
            CategoryEntity parent = categoryRepository.findById(req.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("error.category.not_found"));
            cate.setParent(parent);
        }
        categoryRepository.save(cate);
       return mapper.toResponseDto(cate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> deleteCate(DeleteCategoryRequestDto req) throws BadRequestException {
        List<CategoryEntity> categories = categoryRepository.findAllById(req.getIds());
        for(CategoryEntity category : categories) {
            boolean hasCourses = !category.getCourses().isEmpty();
            boolean hasSubCategories = !category.getChildren().isEmpty();

            if(!hasCourses && !hasSubCategories) {
                categoryRepository.delete(category);
            }
            else {
                throw new BadRequestException("error.category.has_courses_or_children");
            }
        }
        return req.getIds();
    }
}

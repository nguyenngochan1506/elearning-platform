package dev.edu.ngochandev.authservice.services.impl;

import dev.edu.ngochandev.authservice.commons.MyUtils;
import dev.edu.ngochandev.authservice.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.PermissionResponseDto;
import dev.edu.ngochandev.authservice.entities.PermissionEntity;
import dev.edu.ngochandev.authservice.mappers.PermissionMapper;
import dev.edu.ngochandev.authservice.repositories.PermissionRepository;
import dev.edu.ngochandev.authservice.services.PermissionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public PageResponseDto<PermissionResponseDto> getAllPermissions(SimpleFilterRequestDto filter) {
        // create pageable
        Pageable pageable = MyUtils.createPageable(filter);

        Page<PermissionEntity> pageItems = null;
        if (StringUtils.hasLength(filter.getSearch())) {
            pageItems = permissionRepository.findBySearch("%" + filter.getSearch() + "%", pageable);
        } else {
            pageItems = permissionRepository.findAll(pageable);
        }

        List<PermissionResponseDto> itemsResponse =
                pageItems.map(permissionMapper::toResponseDto).toList();

        return PageResponseDto.<PermissionResponseDto>builder()
                .currentPage(filter.getPage())
                .totalElements(pageItems.getTotalElements())
                .totalPages(pageItems.getTotalPages())
                .items(itemsResponse)
                .build();
    }
}

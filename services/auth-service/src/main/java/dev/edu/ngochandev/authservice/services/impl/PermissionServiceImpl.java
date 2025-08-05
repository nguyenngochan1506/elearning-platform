package dev.edu.ngochandev.authservice.services.impl;

import dev.edu.ngochandev.authservice.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.PermissionResponseDto;
import dev.edu.ngochandev.authservice.entities.PermissionEntity;
import dev.edu.ngochandev.authservice.mappers.PermissionMapper;
import dev.edu.ngochandev.authservice.repositories.PermissionRepository;
import dev.edu.ngochandev.authservice.services.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public PageResponseDto<PermissionResponseDto> getAllPermissions(SimpleFilterRequestDto filter) {
        //sort
        String[] sort = filter.getSort().split(":");
        String sortField = sort[0];
        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        //pageable
        int page = filter.getPage() > 0 ? filter.getPage() - 1 : 0;
        int size = filter.getSize();

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<PermissionEntity> pageItems = null;
         if(StringUtils.hasLength(filter.getSearch())){
             pageItems = permissionRepository.findBySearch("%"+filter.getSearch()+"%", pageable);
         }else{
             pageItems = permissionRepository.findAll(pageable);
         }

        List<PermissionResponseDto> itemsResponse = pageItems.map(permissionMapper::toResponseDto).toList();

        return PageResponseDto.<PermissionResponseDto>builder()
                .currentPage(filter.getPage())
                .totalElements(pageItems.getTotalElements())
                .totalPages(pageItems.getTotalPages())
                .items(itemsResponse)
                .build();
    }
}

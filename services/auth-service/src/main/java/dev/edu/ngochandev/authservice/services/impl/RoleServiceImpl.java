package dev.edu.ngochandev.authservice.services.impl;

import dev.edu.ngochandev.authservice.commons.MyUtils;
import dev.edu.ngochandev.authservice.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.RoleDetailResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.RoleSummaryResponseDto;
import dev.edu.ngochandev.authservice.entities.RoleEntity;
import dev.edu.ngochandev.authservice.mappers.RoleMapper;
import dev.edu.ngochandev.authservice.repositories.RoleRepository;
import dev.edu.ngochandev.authservice.services.RoleService;
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
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public PageResponseDto<RoleSummaryResponseDto> getAllRoles(SimpleFilterRequestDto filter) {
        //pageable
        Pageable pageable = MyUtils.createPageable(filter);
        //search
        Page<RoleEntity> pageItems = null;
        if(StringUtils.hasLength(filter.getSearch())){
            pageItems = roleRepository.findBySearch("%" + filter.getSearch() + "%", pageable);
        }else{
            pageItems = roleRepository.findAll(pageable);
        }
        List<RoleSummaryResponseDto> itemsResponse = pageItems.map(roleMapper::mapToSummaryResponseDto).toList();

        return PageResponseDto.<RoleSummaryResponseDto>builder()
                .currentPage(filter.getPage())
                .totalElements(pageItems.getTotalElements())
                .totalPages(pageItems.getTotalPages())
                .items(itemsResponse)
                .build();
    }

    @Override
    public RoleDetailResponseDto getRoleById(Long id) {
        return null;
    }
}

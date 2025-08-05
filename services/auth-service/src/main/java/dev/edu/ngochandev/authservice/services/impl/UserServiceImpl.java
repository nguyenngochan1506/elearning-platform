package dev.edu.ngochandev.authservice.services.impl;

import dev.edu.ngochandev.authservice.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.mappers.UserMapper;
import dev.edu.ngochandev.authservice.repositories.UserRepository;
import dev.edu.ngochandev.authservice.services.UserService;
import dev.edu.ngochandev.authservice.specifications.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public PageResponseDto<UserResponseDto> listUsers(AdvancedFilterRequestDto filter) {
        Pageable pageable = createPageable(filter);
        Specification<UserEntity> spec = new UserSpecification(filter.getFilters(), filter.getSearch());
        Page<UserEntity> pageOfUsers = userRepository.findAll(spec, pageable);

        List<UserResponseDto> userDtos = pageOfUsers.map(userMapper::toResponseDto).toList();

        return PageResponseDto.<UserResponseDto>builder()
                .currentPage(filter.getPage())
                .totalElements(pageOfUsers.getTotalElements())
                .totalPages(pageOfUsers.getTotalPages())
                .items(userDtos)
                .build();
    }

    private Pageable createPageable(AdvancedFilterRequestDto filter) {
        String[] sortParams = filter.getSort().split(":");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        int page = filter.getPage() > 0 ? filter.getPage() - 1 : 0;
        int size = filter.getSize();
        return PageRequest.of(page, size, Sort.by(direction, sortField));
    }
}

package dev.edu.ngochandev.authservice.services.impl;

import dev.edu.ngochandev.authservice.commons.MyUtils;
import dev.edu.ngochandev.authservice.commons.enums.UserStatus;
import dev.edu.ngochandev.authservice.dtos.req.AdminUserCreateRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.AdminUserResponse;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.dtos.res.UserResponseDto;
import dev.edu.ngochandev.authservice.entities.RoleEntity;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.authservice.entities.UserRoleEntity;
import dev.edu.ngochandev.authservice.exceptions.DuplicateResourceException;
import dev.edu.ngochandev.authservice.exceptions.ResourceNotFoundException;
import dev.edu.ngochandev.authservice.mappers.UserMapper;
import dev.edu.ngochandev.authservice.repositories.RoleRepository;
import dev.edu.ngochandev.authservice.repositories.UserRepository;
import dev.edu.ngochandev.authservice.repositories.UserRoleRepository;
import dev.edu.ngochandev.authservice.services.UserService;
import dev.edu.ngochandev.authservice.specifications.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResponseDto<AdminUserResponse> listUsers(AdvancedFilterRequestDto filter) {
        Pageable pageable = MyUtils.createPageable(filter);
        Specification<UserEntity> spec = new UserSpecification(filter.getFilters(), filter.getSearch());
        Page<UserEntity> pageOfUsers = userRepository.findAll(spec, pageable);

        List<AdminUserResponse> userDtos = pageOfUsers.map(userMapper::toAdminResponseDto).toList();

        return PageResponseDto.<AdminUserResponse>builder()
                .currentPage(filter.getPage())
                .totalElements(pageOfUsers.getTotalElements())
                .totalPages(pageOfUsers.getTotalPages())
                .items(userDtos)
                .build();
    }

    @Override
    public Long createUser(AdminUserCreateRequestDto req) {
        if(userRepository.existsByUsername((req.getUsername()))){
            throw new DuplicateResourceException("error.duplicate.username");
        }
        if(userRepository.existsByEmail((req.getEmail()))){
            throw new DuplicateResourceException("error.duplicate.email");
        }
        //check role ids
        List<RoleEntity> roles = roleRepository.findAllById(req.getRoleIds());
        if(roles.size() != req.getRoleIds().size()){
            throw new ResourceNotFoundException("error.role.not-found");
        }
        //save user
        UserEntity savedUser = userRepository.save(UserEntity.builder()
                .fullName(req.getFullName())
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .status(req.getStatus())
                .build());
        //save users-roles
        userRoleRepository.saveAll(roles.stream()
                .map(role ->{
                    UserRoleEntity userRole = new UserRoleEntity();
                    userRole.setUser(savedUser);
                    userRole.setRole(role);
                    return userRole;
                })
                .toList());

        return savedUser.getId();
    }

    @Override
    public Long deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));
        user.setIsDeleted(true);
        userRepository.save(user);
        //soft delete user-roles
        user.getUserRoles().forEach(userRole -> {
            userRole.setIsDeleted(true);
            userRoleRepository.save(userRole);
        });

        return user.getId();
    }

}

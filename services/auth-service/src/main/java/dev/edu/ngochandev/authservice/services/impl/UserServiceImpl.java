package dev.edu.ngochandev.authservice.services.impl;

import dev.edu.ngochandev.authservice.commons.MyUtils;
import dev.edu.ngochandev.authservice.dtos.req.AdminUserCreateRequestDto;
import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserManyDeleteRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserUpdateRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.AdminUserResponse;
import dev.edu.ngochandev.authservice.entities.BaseEntity;
import dev.edu.ngochandev.authservice.entities.RoleEntity;
import dev.edu.ngochandev.authservice.entities.UserEntity;
import dev.edu.ngochandev.common.events.UserRegisteredEvent;
import dev.edu.ngochandev.common.exceptions.DuplicateResourceException;
import dev.edu.ngochandev.common.exceptions.ResourceNotFoundException;
import dev.edu.ngochandev.authservice.mappers.UserMapper;
import dev.edu.ngochandev.authservice.repositories.RoleRepository;
import dev.edu.ngochandev.authservice.repositories.UserRepository;
import dev.edu.ngochandev.authservice.services.UserService;
import dev.edu.ngochandev.authservice.specifications.UserSpecification;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dev.edu.ngochandev.common.dtos.res.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public PageResponseDto<AdminUserResponse> listUsers(AdvancedFilterRequestDto filter) {
        Pageable pageable = MyUtils.createPageable(filter);
        Specification<UserEntity> spec = new UserSpecification(filter.getFilters(), filter.getSearch());
        Page<UserEntity> pageOfUsers = userRepository.findAll(spec, pageable);

        return PageResponseDto.<AdminUserResponse>builder()
                .currentPage(filter.getPage())
                .totalElements(pageOfUsers.getTotalElements())
                .totalPages(pageOfUsers.getTotalPages())
                .items(pageOfUsers.map(userMapper::toAdminResponseDto).getContent())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(AdminUserCreateRequestDto req) {
        if (userRepository.existsByUsername((req.getUsername()))) {
            throw new DuplicateResourceException("error.duplicate.username");
        }
        if (userRepository.existsByEmail((req.getEmail()))) {
            throw new DuplicateResourceException("error.duplicate.email");
        }
        // check role ids
        List<RoleEntity> rolesToAssign = roleRepository.findAllById(req.getRoleIds());
        if (rolesToAssign.size() != req.getRoleIds().size()) {
            throw new ResourceNotFoundException("error.role.not-found");
        }
        // save user
        UserEntity newUser = UserEntity.builder()
                .fullName(req.getFullName())
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .status(req.getStatus())
                .build();
        UserEntity savedUser = userRepository.save(newUser);
        // save users-roles
        List<UserRoleEntity> userRoles = rolesToAssign.stream()
                .map(role -> new UserRoleEntity(newUser, role))
                .toList();
        userRoleRepository.saveAll(userRoles);
        // publish user registered event
        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent(savedUser.getId(), savedUser.getFullName());
        eventPublisher.publishEvent(userRegisteredEvent);

        return savedUser.getId();
    }

    @Override
    @Transactional
    public Long deleteUser(Long id) {
        UserEntity user =
                userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));

        // soft delete user roles
        Set<Long> userRoleIds =
                user.getUserRoles().stream().map(BaseEntity::getId).collect(Collectors.toSet());
        userRoleRepository.softDeleteAllByIds(userRoleIds);
        // soft delete user
        userRepository.softDeleteAllByIds(Set.of(id));
        return id;
    }

    @Override
    @Transactional
    public void deleteManyUsers(UserManyDeleteRequestDto req) {
        Set<Long> userIds = new HashSet<>(req.getIds());
        if (userIds.isEmpty()) {
            return;
        }
        List<UserEntity> users = userRepository.findAllById(req.getIds());
        if (users.size() != userIds.size()) {
            throw new ResourceNotFoundException("error.user.not-found");
        }
        Set<Long> userRoleIds = users.stream()
                .flatMap(user -> user.getUserRoles().stream())
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());
        userRoleRepository.softDeleteAllByIds(userRoleIds);
        userRepository.softDeleteAllByIds(new HashSet<>(req.getIds()));
    }

    @Override
    @Transactional
    public Long updateUser(UserUpdateRequestDto req) {
        // check user
        UserEntity user = userRepository
                .findById(req.getId())
                .orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));
        user.setStatus(req.getStatus());
        user.setFullName(req.getFullName());

        // check role ids
        if (!req.getRoleIds().isEmpty()) {
            // update user roles
            List<RoleEntity> roles = roleRepository.findAllById(req.getRoleIds());
            // valid role ids
            if (roles.size() != req.getRoleIds().size()) {
                throw new ResourceNotFoundException("error.role.not-found");
            }
            // get current user roles
            List<UserRoleEntity> currentUserRoles = userRoleRepository.findAllById(
                    user.getUserRoles().stream().map(BaseEntity::getId).toList());
            Set<Long> currentRoleIds = currentUserRoles.stream()
                    .map(userRole -> userRole.getRole().getId())
                    .collect(Collectors.toSet());
            // new role ids
            Set<Long> newRoleIds = new HashSet<>(req.getRoleIds());
            // remove roles that are not in new role ids
            Set<Long> rolesToDelete = new HashSet<>(currentRoleIds);
            rolesToDelete.removeAll(newRoleIds);

            if (!rolesToDelete.isEmpty()) {
                Set<Long> rolesToDeleteEntities = user.getUserRoles().stream()
                        .filter(userRole ->
                                rolesToDelete.contains(userRole.getRole().getId()))
                        .map(UserRoleEntity::getId)
                        .collect(Collectors.toSet());
                userRoleRepository.softDeleteAllByIds(rolesToDeleteEntities);
            }
            // add new roles
            List<Long> rolesToAdd = newRoleIds.stream()
                    .filter(roleId -> !currentRoleIds.contains(roleId))
                    .toList();
            if (!rolesToAdd.isEmpty()) {
                List<UserRoleEntity> newUserRoles = rolesToAdd.stream()
                        .map(roleId -> {
                            UserRoleEntity userRole = new UserRoleEntity();
                            userRole.setUser(user);
                            userRole.setRole(roleRepository
                                    .findById(roleId)
                                    .orElseThrow(() -> new ResourceNotFoundException("error.role.not-found")));
                            return userRole;
                        })
                        .toList();
                userRoleRepository.saveAll(newUserRoles);
            }
        } else {
            // if no role ids, remove all user-roles
            userRoleRepository.softDeleteAllByIds(
                    user.getUserRoles().stream().map(BaseEntity::getId).collect(Collectors.toSet()));
        }
        userRepository.save(user);

        return req.getId();
    }
}

package dev.edu.ngochandev.authservice.services.impl;

import dev.edu.ngochandev.authservice.commons.MyUtils;
import dev.edu.ngochandev.authservice.dtos.req.AdminUserCreateRequestDto;
import dev.edu.ngochandev.authservice.entities.*;
import dev.edu.ngochandev.authservice.repositories.OrganizationRepository;
import dev.edu.ngochandev.authservice.repositories.UserOrganizationRoleRepository;
import dev.edu.ngochandev.common.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserManyDeleteRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserUpdateRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.AdminUserResponse;
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
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final OrganizationRepository organizationRepository;
    private final UserOrganizationRoleRepository userOrganizationRoleRepository;

    @Override
    public PageResponseDto<AdminUserResponse> listUsers(AdvancedFilterRequestDto filter, Long organizationId) {
        Pageable pageable = MyUtils.createPageable(filter);
        Specification<UserEntity> spec = new UserSpecification(filter.getFilters(), filter.getSearch(), organizationId);
        Page<UserEntity> pageOfUsers = userRepository.findAll(spec, pageable);

        List<AdminUserResponse> responseItems = pageOfUsers.getContent().stream()
                .map(user -> userMapper.toAdminResponseDto(user, organizationId))
                .collect(Collectors.toList());

        return PageResponseDto.<AdminUserResponse>builder()
                .currentPage(filter.getPage())
                .totalElements(pageOfUsers.getTotalElements())
                .totalPages(pageOfUsers.getTotalPages())
                .items(responseItems)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(AdminUserCreateRequestDto req, Long organizationId) {
        OrganizationEntity organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("error.organization.not-found"));

        if (userRepository.existsByUsername(req.getUsername())) {
            throw new DuplicateResourceException("error.duplicate.username");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateResourceException("error.duplicate.email");
        }

        List<RoleEntity> rolesToAssign = roleRepository.findByIdsInOrganizationOrGlobal(req.getRoleIds(), organizationId);
        if (rolesToAssign.size() != req.getRoleIds().size()) {
            throw new ResourceNotFoundException("error.role.not-found-in-organization");
        }

        UserEntity newUser = UserEntity.builder()
                .fullName(req.getFullName())
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .status(req.getStatus())
                .build();
        UserEntity savedUser = userRepository.save(newUser);

        List<UserOrganizationRoleEntity> userOrgRoles = rolesToAssign.stream()
                .map(role -> UserOrganizationRoleEntity.builder()
                        .user(savedUser)
                        .organization(organization)
                        .role(role)
                        .build())
                .toList();
        userOrganizationRoleRepository.saveAll(userOrgRoles);

        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent(savedUser.getId(), savedUser.getFullName());
        eventPublisher.publishEvent(userRegisteredEvent);

        return savedUser.getId();
    }

    @Override
    @Transactional
    public Long deleteUser(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));

        user.getUserOrganizationRoles().forEach(uor -> uor.setIsDeleted(true));
        userOrganizationRoleRepository.saveAll(user.getUserOrganizationRoles());

        user.setIsDeleted(true);
        userRepository.save(user);
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

        Set<UserOrganizationRoleEntity> rolesToDelete = users.stream()
                .flatMap(user -> user.getUserOrganizationRoles().stream())
                .collect(Collectors.toSet());

        if (!rolesToDelete.isEmpty()) {
            rolesToDelete.forEach(uor -> uor.setIsDeleted(true));
            userOrganizationRoleRepository.saveAll(rolesToDelete);
        }

        users.forEach(user -> user.setIsDeleted(true));
        userRepository.saveAll(users);
    }

    @Override
    @Transactional
    public Long updateUser(UserUpdateRequestDto req, Long organizationId) { // Thêm organizationId
        // get user and organization
        UserEntity user = userRepository.findById(req.getId())
                .orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));

        OrganizationEntity organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("error.organization.not-found"));

        // update basic info
        user.setStatus(req.getStatus());
        user.setFullName(req.getFullName());

        // update roles in this organization
        if (req.getRoleIds() != null) {
            // get current assignments in this organization
            Set<UserOrganizationRoleEntity> currentAssignmentsInOrg = user.getUserOrganizationRoles().stream()
                    .filter(uor -> uor.getOrganization().getId().equals(organizationId) && !uor.getIsDeleted())
                    .collect(Collectors.toSet());

            Set<Long> currentRoleIdsInOrg = currentAssignmentsInOrg.stream()
                    .map(uor -> uor.getRole().getId())
                    .collect(Collectors.toSet());

            Set<Long> newRoleIds = new HashSet<>(req.getRoleIds());

            // determining roles to REMOVE
            // (those that are in current but not in new)
            Set<Long> roleIdsToRemove = new HashSet<>(currentRoleIdsInOrg);
            roleIdsToRemove.removeAll(newRoleIds);

            if (!roleIdsToRemove.isEmpty()) {
                List<UserOrganizationRoleEntity> assignmentsToRemove = currentAssignmentsInOrg.stream()
                        .filter(assignment -> roleIdsToRemove.contains(assignment.getRole().getId()))
                        .toList();
                assignmentsToRemove.forEach(assignment -> assignment.setIsDeleted(true));
                userOrganizationRoleRepository.saveAll(assignmentsToRemove);
            }

            // determining roles to ADD
            // (those that are in new but not in current)
            Set<Long> roleIdsToAdd = new HashSet<>(newRoleIds);
            roleIdsToAdd.removeAll(currentRoleIdsInOrg);

            if (!roleIdsToAdd.isEmpty()) {
                // validate roles to add
                List<RoleEntity> rolesToAdd = roleRepository.findByIdsInOrganizationOrGlobal(List.copyOf(roleIdsToAdd), organizationId);
                if (rolesToAdd.size() != roleIdsToAdd.size()) {
                    throw new ResourceNotFoundException("error.role.not-found-in-organization");
                }

                List<UserOrganizationRoleEntity> newAssignments = rolesToAdd.stream()
                        .map(role -> UserOrganizationRoleEntity.builder()
                                .user(user)
                                .organization(organization)
                                .role(role)
                                .build())
                        .toList();
                userOrganizationRoleRepository.saveAll(newAssignments);
            }
        } else {
            // if roles not provided, remove all current assignments in this organization
            Set<UserOrganizationRoleEntity> currentAssignmentsInOrg = user.getUserOrganizationRoles().stream()
                    .filter(uor -> uor.getOrganization().getId().equals(organizationId) && !uor.getIsDeleted())
                    .collect(Collectors.toSet());
            if (!currentAssignmentsInOrg.isEmpty()) {
                currentAssignmentsInOrg.forEach(assignment -> assignment.setIsDeleted(true));
                userOrganizationRoleRepository.saveAll(currentAssignmentsInOrg);
            }
        }

        userRepository.save(user);

        return req.getId();
    }
}

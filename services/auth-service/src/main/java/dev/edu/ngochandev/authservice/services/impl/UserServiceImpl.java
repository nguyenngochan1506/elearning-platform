package dev.edu.ngochandev.authservice.services.impl;

import dev.edu.ngochandev.authservice.commons.MyUtils;
import dev.edu.ngochandev.authservice.commons.enums.UserStatus;
import dev.edu.ngochandev.authservice.dtos.req.AdminUserCreateRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.AdvancedFilterRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserManyDeleteRequestDto;
import dev.edu.ngochandev.authservice.dtos.req.UserUpdateRequestDto;
import dev.edu.ngochandev.authservice.dtos.res.AdminUserResponse;
import dev.edu.ngochandev.authservice.dtos.res.PageResponseDto;
import dev.edu.ngochandev.authservice.entities.BaseEntity;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public void deleteManyUsers(UserManyDeleteRequestDto req) {
        List<UserEntity> users = userRepository.findAllById(req.getIds());
        if(users.size() != req.getIds().size()){
            throw new ResourceNotFoundException("error.user.not-found");
        }
        users.forEach(user -> {
            user.setIsDeleted(true);
            userRepository.save(user);
            //soft delete user-roles
            user.getUserRoles().forEach(userRole -> {
                userRole.setIsDeleted(true);
                userRoleRepository.save(userRole);
            });
        });
    }

    @Override
    public Long updateUser(UserUpdateRequestDto req) {
        //check user
        UserEntity user = userRepository.findById(req.getId())
                .orElseThrow(() -> new ResourceNotFoundException("error.user.not-found"));
        user.setStatus(req.getStatus());
        user.setFullName(req.getFullName());

        //check role ids
        if(!req.getRoleIds().isEmpty()){
            //update user roles
            List<RoleEntity> roles = roleRepository.findAllById(req.getRoleIds());
            // valid role ids
            if(roles.size() != req.getRoleIds().size()){
                throw new ResourceNotFoundException("error.role.not-found");
            }
            //get current user roles
            List<UserRoleEntity> currentUserRoles = userRoleRepository.findAllById(user.getUserRoles().stream().map(BaseEntity::getId).collect(Collectors.toList()));
            Set<Long> currentRoleIds = currentUserRoles.stream()
                    .map(userRole -> userRole.getRole().getId())
                    .collect(Collectors.toSet());
            //new role ids
            Set<Long> newRoleIds = new HashSet<>(req.getRoleIds());
            //remove roles that are not in new role ids
            Set<Long> rolesToDelete = new HashSet<>(currentRoleIds);
            rolesToDelete.removeAll(newRoleIds);
            System.out.println("Roles to delete: " + rolesToDelete);

            if(!rolesToDelete.isEmpty()){
                for(UserRoleEntity currentUserRole : currentUserRoles) {
                    if(rolesToDelete.contains(currentUserRole.getRole().getId())){
                        currentUserRole.setIsDeleted(true);
                        userRoleRepository.save(currentUserRole);
                        }
                    }
                }
            //add new roles
            List<Long> rolesToAdd = newRoleIds.stream().filter(roleId -> !currentRoleIds.contains(roleId)).toList();
            if(!rolesToAdd.isEmpty()){
                List<UserRoleEntity> newUserRoles = rolesToAdd.stream()
                        .map(roleId -> {
                            UserRoleEntity userRole = new UserRoleEntity();
                            userRole.setUser(user);
                            userRole.setRole(roleRepository.findById(roleId)
                                    .orElseThrow(() -> new ResourceNotFoundException("error.role.not-found")));
                            return userRole;
                        })
                        .toList();
                userRoleRepository.saveAll(newUserRoles);
            }
        }else{
            //if no role ids, remove all roles
            user.getUserRoles().forEach(userRole -> {
                userRole.setIsDeleted(true);
                userRoleRepository.save(userRole);
            });
        }
        userRepository.save(user);

        return req.getId();
    }

}

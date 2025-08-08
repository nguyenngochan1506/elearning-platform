package dev.edu.ngochandev.authservice.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.edu.ngochandev.authservice.commons.enums.HttpMethod;
import dev.edu.ngochandev.authservice.commons.enums.UserStatus;
import dev.edu.ngochandev.authservice.entities.*;
import dev.edu.ngochandev.authservice.repositories.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "DATA-INITIALIZER")
public class DataInitializer implements CommandLineRunner {
    public static final String DEFAULT_ROLE = "default_user";
    public static final String SUPER_ADMIN_ROLE = "super_admin";

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PasswordEncoder passwordEncoder;

    @Value("${app.super-admin.username}")
    private String superAdminUsername;
    @Value("${app.super-admin.password}")
    private String superAdminPassword;
    @Value("${app.super-admin.email}")
    private String superAdminEmail;
    @Value("${app.super-admin.full-name}")
    private String superAdminFullName;

    @Override
    public void run(String... args) throws Exception {
        log.info("===== Starting Data Initialization =====");
        synchronizePermissionsFromFile();
        synchronizeCoreRoles();
        createSuperAdminUser();
        log.info("===== Data Initialization Finished =====");
    }

    private void synchronizePermissionsFromFile() {
        log.info("Step 1: Synchronizing permissions from 'permissions.json'...");
        try (InputStream inputStream = new ClassPathResource("migrations/permissions.json").getInputStream()) {
            List<PermissionMigrationDto> permissionsFromFile = objectMapper.readValue(inputStream, new TypeReference<>() {});

            Map<String, PermissionEntity> existingPermissionsMap = permissionRepository.findAll()
                    .stream()
                    .collect(Collectors.toMap(
                            p -> p.getMethod().name() + ":" + p.getApiPath(),
                            Function.identity()
                    ));

            for (PermissionMigrationDto dto : permissionsFromFile) {
                String key = dto.getMethod() + ":" + dto.getApiPath();
                String translatedName = dto.getNameKey();

                PermissionEntity existingPermission = existingPermissionsMap.remove(key);

                if (existingPermission == null) {
                    PermissionEntity newPermission = new PermissionEntity();
                    newPermission.setName(translatedName);
                    newPermission.setApiPath(dto.getApiPath());
                    newPermission.setMethod(HttpMethod.valueOf(dto.getMethod()));
                    newPermission.setModule(dto.getModule());
                    permissionRepository.save(newPermission);
                    log.info("  -> CREATED Permission: [{} {}]", dto.getMethod(), dto.getApiPath());
                } else {
                    boolean needsUpdate = !existingPermission.getName().equals(translatedName) || !existingPermission.getModule().equals(dto.getModule());
                    if (needsUpdate) {
                        existingPermission.setName(translatedName);
                        existingPermission.setModule(dto.getModule());
                        permissionRepository.save(existingPermission);
                        log.info("  -> UPDATED Permission: [{} {}]", dto.getMethod(), dto.getApiPath());
                    }
                }
            }

            if (!existingPermissionsMap.isEmpty()) {
                log.warn("  -> Found {} obsolete permissions to be DELETED (soft).", existingPermissionsMap.size());
                for (PermissionEntity obsoletePermission : existingPermissionsMap.values()) {
                    obsoletePermission.setIsDeleted(true);
                    permissionRepository.save(obsoletePermission);
                    log.info("  -> DELETED (Soft) Permission: [{} {}]", obsoletePermission.getMethod(), obsoletePermission.getApiPath());
                }
            }
            log.info("Step 1: Finished synchronizing permissions.");

        } catch (Exception e) {
            log.error("FATAL: Failed to synchronize permissions from file.", e);
            throw new RuntimeException(e);
        }
    }

    private void synchronizeCoreRoles() {
        log.info("Step 2: Synchronizing core roles (super_admin, default_user)...");

        // --- SỬA ĐỔI CHO SUPER ADMIN ---
        // 1. Kiểm tra sự tồn tại, nếu không có thì tạo mới
        roleRepository.findByName(SUPER_ADMIN_ROLE).orElseGet(() -> {
            log.info("  -> Role '{}' not found. Creating it.", SUPER_ADMIN_ROLE);
            return roleRepository.save(RoleEntity.builder().name(SUPER_ADMIN_ROLE).description("Full system access").build());
        });
        // 2. Luôn lấy lại role bằng câu query JOIN FETCH để đảm bảo collection được khởi tạo
        RoleEntity superAdminRole = roleRepository.findByNameWithPermissions(SUPER_ADMIN_ROLE)
                .orElseThrow(() -> new RuntimeException("CRITICAL: Failed to create or find Super Admin Role!"));
        // 3. Lấy tất cả các quyền và gán
        List<PermissionEntity> allPermissions = permissionRepository.findAll();
        assignPermissionsToRole(superAdminRole, allPermissions);

        // --- SỬA ĐỔI CHO DEFAULT USER ---
        // 1. Kiểm tra sự tồn tại, nếu không có thì tạo mới
        roleRepository.findByName(DEFAULT_ROLE).orElseGet(() -> {
            log.info("  -> Role '{}' not found. Creating it.", DEFAULT_ROLE);
            return roleRepository.save(RoleEntity.builder().name(DEFAULT_ROLE).description("Default permissions for new users").build());
        });
        // 2. Luôn lấy lại role bằng câu query JOIN FETCH
        RoleEntity defaultUserRole = roleRepository.findByNameWithPermissions(DEFAULT_ROLE)
                .orElseThrow(() -> new RuntimeException("CRITICAL: Failed to create or find Default User Role!"));
        // 3. Tìm các quyền mặc định và gán
        List<PermissionEntity> defaultPermissions = findDefaultPermissions();
        assignPermissionsToRole(defaultUserRole, defaultPermissions);

        log.info("Step 2: Finished synchronizing core roles.");
    }

    private List<PermissionEntity> findDefaultPermissions() {
        try (InputStream inputStream = new ClassPathResource("migrations/permissions.json").getInputStream()) {
            List<PermissionMigrationDto> allDtos = objectMapper.readValue(inputStream, new TypeReference<>() {});
            Set<String> defaultPermissionKeys = allDtos.stream()
                    .filter(PermissionMigrationDto::isDefault)
                    .map(dto -> dto.getMethod() + ":" + dto.getApiPath())
                    .collect(Collectors.toSet());

            return permissionRepository.findAll().stream()
                    .filter(p -> defaultPermissionKeys.contains(p.getMethod().name() + ":" + p.getApiPath()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("FATAL: Could not read 'permissions.json' to determine default permissions.", e);
            return List.of();
        }
    }

    private void assignPermissionsToRole(RoleEntity role, List<PermissionEntity> permissionsToAssign) {
        Set<Long> existingPermissionIds = role.getRolePermissions().stream()
                .map(rp -> rp.getPermission().getId())
                .collect(Collectors.toSet());

        Set<Long> permissionsToAssignIds = permissionsToAssign.stream()
                .map(PermissionEntity::getId)
                .collect(Collectors.toSet());

        List<RolePermissionEntity> newAssignments = permissionsToAssign.stream()
                .filter(p -> !existingPermissionIds.contains(p.getId()))
                .map(p -> {
                    RolePermissionEntity assignment = new RolePermissionEntity();
                    assignment.setRole(role);
                    assignment.setPermission(p);
                    return assignment;
                })
                .collect(Collectors.toList());

        if (!newAssignments.isEmpty()) {
            rolePermissionRepository.saveAll(newAssignments);
            log.info("  -> Assigned {} new permissions to role '{}'.", newAssignments.size(), role.getName());
        }

        Set<RolePermissionEntity> assignmentsToRemove = role.getRolePermissions().stream()
                .filter(rp -> !permissionsToAssignIds.contains(rp.getPermission().getId()))
                .collect(Collectors.toSet());

        if (!assignmentsToRemove.isEmpty()) {
            rolePermissionRepository.deleteAll(assignmentsToRemove);
            log.warn("  -> Removed {} obsolete permissions from role '{}'.", assignmentsToRemove.size(), role.getName());
        }
    }
    private void createSuperAdminUser() {
        log.info("Step 3: Creating Super Admin user if not exists...");
        if (!userRepository.existsByUsername(superAdminUsername)) {
            RoleEntity superAdminRole = roleRepository.findByNameWithPermissions(SUPER_ADMIN_ROLE)
                    .orElseThrow(() -> new RuntimeException("CRITICAL: Super Admin Role not found!"));

            UserEntity adminUser = UserEntity.builder()
                    .username(superAdminUsername)
                    .password(passwordEncoder.encode(superAdminPassword))
                    .email(superAdminEmail)
                    .fullName(superAdminFullName)
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepository.save(adminUser);

            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setUser(adminUser);
            userRole.setRole(superAdminRole);
            userRoleRepository.save(userRole);

            log.info("  -> CREATED Super Admin user '{}'.", superAdminUsername);
        } else {
            log.info("  -> Super Admin user '{}' already exists. Skipping creation.", superAdminUsername);
        }
        log.info("Step 3: Finished Super Admin user check.");
    }

    @Data
    private static class PermissionMigrationDto {
        private String nameKey;
        private String apiPath;
        private String method;
        private String module;
        @JsonProperty("isDefault")
        private boolean isDefault = false;
    }
}

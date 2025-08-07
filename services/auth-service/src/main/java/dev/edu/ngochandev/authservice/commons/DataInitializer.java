package dev.edu.ngochandev.authservice.commons;

import dev.edu.ngochandev.authservice.commons.enums.HttpMethod;
import dev.edu.ngochandev.authservice.entities.PermissionEntity;
import dev.edu.ngochandev.authservice.entities.RoleEntity;
import dev.edu.ngochandev.authservice.repositories.PermissionRepository;
import dev.edu.ngochandev.authservice.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "DATA-INITIALIZER")
public class DataInitializer implements CommandLineRunner {
    public static final String DEFAULT_ROLE = "default_user";
    private final RoleRepository roleRepository;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        initDefaultRole();
    }
    private void initDefaultRole() {
        Optional<RoleEntity> role =  roleRepository.findByName(DEFAULT_ROLE);
        if(role.isPresent()){
            System.out.println("Role '" + DEFAULT_ROLE + "' already exists.");
        } else {
            RoleEntity newRole = new RoleEntity();
            newRole.setName(DEFAULT_ROLE);
            roleRepository.save(newRole);
            System.out.println("Created default role: " + DEFAULT_ROLE);
        }
    }
}

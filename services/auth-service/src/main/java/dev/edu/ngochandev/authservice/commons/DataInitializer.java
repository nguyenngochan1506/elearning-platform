package dev.edu.ngochandev.authservice.commons;

import dev.edu.ngochandev.authservice.entities.RoleEntity;
import dev.edu.ngochandev.authservice.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    public static final String DEFAULT_ROLE = "default_user";
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
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

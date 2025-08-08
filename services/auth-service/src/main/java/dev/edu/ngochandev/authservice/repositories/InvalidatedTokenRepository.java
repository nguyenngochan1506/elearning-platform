package dev.edu.ngochandev.authservice.repositories;

import dev.edu.ngochandev.authservice.entities.InvalidatedTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedTokenEntity, String> {}

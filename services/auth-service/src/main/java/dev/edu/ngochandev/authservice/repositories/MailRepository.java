package dev.edu.ngochandev.authservice.repositories;

import dev.edu.ngochandev.authservice.entities.MailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<MailEntity, Long> {
}

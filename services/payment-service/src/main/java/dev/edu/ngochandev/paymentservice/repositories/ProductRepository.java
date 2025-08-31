package dev.edu.ngochandev.paymentservice.repositories;

import dev.edu.ngochandev.paymentservice.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}

package dev.edu.ngochandev.paymentservice.repositories;

import dev.edu.ngochandev.paymentservice.entities.ProductItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItemEntity, Long> {
}

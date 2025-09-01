package dev.edu.ngochandev.paymentservice.repositories;

import dev.edu.ngochandev.paymentservice.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByUuid(String uuid);

    @Query("SELECT p FROM ProductEntity p JOIN p.items i WHERE i.itemUuid IN :uuids")
    List<ProductEntity> findAllByCourseUuidIn(List<String> uuids);
}

package dev.edu.ngochandev.paymentservice.repositories;

import dev.edu.ngochandev.paymentservice.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    ItemEntity findByItemUuid(String itemUuid);
}

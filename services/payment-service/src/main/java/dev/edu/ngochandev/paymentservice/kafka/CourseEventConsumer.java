package dev.edu.ngochandev.paymentservice.kafka;


import dev.edu.ngochandev.common.events.CategoryInfo;
import dev.edu.ngochandev.common.events.CourseCreateOrUpdateEvent;
import dev.edu.ngochandev.paymentservice.commons.enums.ProductItemType;
import dev.edu.ngochandev.paymentservice.entities.CategoryEntity;
import dev.edu.ngochandev.paymentservice.entities.ItemEntity;
import dev.edu.ngochandev.paymentservice.repositories.CategoryRepository;
import dev.edu.ngochandev.paymentservice.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "COURSE-EVENT-CONSUMER")
public class CourseEventConsumer {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @KafkaListener(topics = "course-create-or-update")
    @Transactional
    public void consumeCourseEvent(CourseCreateOrUpdateEvent event) {
        log.info("CourseCreateOrUpdateEvent received for course_uuid: {}", event.getUuid());

        try {
            ItemEntity itemEntity = itemRepository.findByItemUuid(event.getUuid());
            if (itemEntity == null) {
                itemEntity = new ItemEntity();
                itemEntity.setItemUuid(event.getUuid());
                itemEntity.setItemType(ProductItemType.COURSE);
                itemEntity.setCreatedBy(event.getUserId());
                log.info("Creating new ItemEntity for course_uuid: {}", event.getUuid());
            } else {
                log.info("Updating existing ItemEntity for course_uuid: {}", event.getUuid());
            }

            itemEntity.setName(event.getName());
            itemEntity.setThumbnail(event.getThumbnail());
            itemEntity.setIsActive(event.getIsActive());
            ItemEntity savedItem = itemRepository.save(itemEntity);


            savedItem.setCategories(new HashSet<>());
            log.debug("Cleared old categories for item_id: {}", savedItem.getId());

            if (event.getCategories() != null && !event.getCategories().isEmpty()) {
                for (CategoryInfo categoryInfo : event.getCategories()) {
                    CategoryEntity categoryEntity = categoryRepository.findByUuid(categoryInfo.getUuid());
                    if (categoryEntity == null) {
                        categoryEntity = new CategoryEntity();
                        categoryEntity.setUuid(categoryInfo.getUuid());
                        categoryEntity.setName(categoryInfo.getName());
                        categoryEntity.setSlug(categoryInfo.getSlug());
                        categoryEntity = categoryRepository.save(categoryEntity);
                        log.info("Created new CategoryEntity for category_uuid: {}", categoryInfo.getUuid());
                    }
                    savedItem.getCategories().add(categoryEntity);
                }
                itemRepository.save(savedItem);
                log.info("Updated categories for item_id: {}", savedItem.getId());
            }

        } catch (Exception e) {
            log.error("Error processing CourseCreateOrUpdateEvent for course_uuid: {}: {}", event.getUuid(), e.getMessage(), e);
        }
    }


}

package dev.edu.ngochandev.paymentservice.kafka;


import dev.edu.ngochandev.common.events.CourseCreateOrUpdateEvent;
import dev.edu.ngochandev.paymentservice.commons.enums.ProductItemType;
import dev.edu.ngochandev.paymentservice.entities.ItemEntity;
import dev.edu.ngochandev.paymentservice.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "COURSE-EVENT-CONSUMER")
public class CourseEventConsumer {
    private final ItemRepository itemRepository;

    @KafkaListener(topics = "course-create-or-update")
    public void consumeCourseEvent(CourseCreateOrUpdateEvent event) {
        log.info("CourseCreateOrUpdateEvent received for course_uuid: {}", event.getUuid());

        try{
            ItemEntity item = new ItemEntity();
            item.setItemUuid(event.getUuid());
            item.setItemType(ProductItemType.COURSE);
            itemRepository.save(item);
            log.info("ItemEntity created for course_uuid: {}", event.getUuid());
        }catch (Exception e){
            log.error("Error processing CourseCreateOrUpdateEvent for course_uuid: {}: {}", event.getUuid(), e.getMessage());
        }
    }
}

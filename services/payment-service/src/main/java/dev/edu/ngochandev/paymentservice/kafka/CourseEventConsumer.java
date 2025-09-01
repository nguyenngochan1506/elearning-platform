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
            ItemEntity itemEntity = itemRepository.findByItemUuid(event.getUuid());
            if(itemEntity == null){
//                create new ItemEntity
                ItemEntity newItem = new ItemEntity();
                newItem.setItemUuid(event.getUuid());
                newItem.setItemType(ProductItemType.COURSE);
                newItem.setIsActive(event.getIsActive());
                newItem.setCreatedBy(event.getUserId());
                newItem.setName(event.getName());
                newItem.setThumbnail(event.getThumbnail());
                itemRepository.save(newItem);
                log.info("ItemEntity created for course_uuid: {}", event.getUuid());
            }else{
//                update
                itemEntity.setName(event.getName() == null ? itemEntity.getName() : event.getName());
                itemEntity.setThumbnail(event.getThumbnail() == null ? itemEntity.getThumbnail() : event.getThumbnail());
                itemEntity.setIsActive(event.getIsActive());
                itemRepository.save(itemEntity);
                log.info("ItemEntity updated for course_uuid: {}", event.getUuid());
            }
        }catch (Exception e){
            log.error("Error processing CourseCreateOrUpdateEvent for course_uuid: {}: {}", event.getUuid(), e.getMessage());
        }
    }
}

package dev.edu.ngochandev.courseservice.features.course.kafka;

import dev.edu.ngochandev.common.events.CourseCreateOrUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "COURSE-EVENT-KAFKA-PRODUCER")
public class CourseEventKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCourseCreateOrUpdateEvent(CourseCreateOrUpdateEvent event) {
        log.info("Handling CourseCreateOrUpdateEvent course_uuid: {}", event.getUuid());
        try {
            kafkaTemplate.send("course-create-or-update", event.getUuid(), event);
            log.info("CourseCreateOrUpdateEvent sent to Kafka topic 'course-create-or-update' for course_uuid: {}", event.getUuid());
        } catch (Exception e) {
            log.error("Failed to send CourseCreateOrUpdateEvent to Kafka for course_uuid: {}. Error: {}", event.getUuid(), e.getMessage());
        }
    }
}

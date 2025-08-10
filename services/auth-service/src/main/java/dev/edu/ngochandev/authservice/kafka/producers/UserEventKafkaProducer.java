package dev.edu.ngochandev.authservice.kafka.producers;

import dev.edu.ngochandev.authservice.kafka.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-EVENT-KAFKA-PRODUCER")
public class UserEventKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        log.info("Producing user registered event for user ID: {}", event.getUserId());
        try {
            kafkaTemplate.send("user-registered-topic", event);
        }catch (Exception e){
            log.error("Failed to send user registered event for user ID: {}. Error: {}", event.getUserId(), e.getMessage());
        }
    }
}

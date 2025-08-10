package dev.edu.ngochandev.socialservice.kafka.consumers;


import dev.edu.ngochandev.common.events.UserRegisteredEvent;
import dev.edu.ngochandev.socialservice.entities.UserProfileEntity;
import dev.edu.ngochandev.socialservice.mappers.UserProfileMapper;
import dev.edu.ngochandev.socialservice.repositories.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-EVENT-CONSUMER")
public class UserEventConsumer {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @KafkaListener(topics = "user-registered-topic")
    public void consumeUserRegisteredEvent(UserRegisteredEvent event){
        log.info("Received user registered event: {}", event);
        try {
            if(userProfileRepository.existsByUserId(event.getUserId())) {
                log.warn("User profile already exists for user ID: {}", event.getUserId());
                return;
            }
            // Create a new user profile entity
            UserProfileEntity userProfile = userProfileMapper.toEntity(event);

            userProfileRepository.save(userProfile);
            log.info("User profile created for user ID: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to create user profile for user ID: {}. Error: {}", event.getUserId(), e.getMessage());
        }
    }
}

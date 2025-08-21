package dev.edu.ngochandev.socialservice.entities;

import dev.edu.ngochandev.socialservice.commons.enums.ConnectionStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "connections")
public class ConnectionRelation {
    @Id
    private String id;

    @Field("requester_id")
    private Long requesterId;

    @Field("recipient_id")
    private Long recipientId;

    @Field("status")
    private ConnectionStatus status;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

}

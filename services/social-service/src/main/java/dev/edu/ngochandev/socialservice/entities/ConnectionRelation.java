package dev.edu.ngochandev.socialservice.entities;

import dev.edu.ngochandev.socialservice.commons.enums.ConnectionStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.LocalDateTime;

@RelationshipProperties
@Getter
@Setter
public class ConnectionRelation {
    @RelationshipId
    @Property("id")
    private Long id;

    @Property("initiator_user_id")
    private Long initiatorUserId;

    @TargetNode
    private UserProfileEntity targetUser;

    @Property("status")
    private ConnectionStatus status;

    @CreatedDate
    @Property("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Property("updated_at")
    private LocalDateTime updatedAt;

}

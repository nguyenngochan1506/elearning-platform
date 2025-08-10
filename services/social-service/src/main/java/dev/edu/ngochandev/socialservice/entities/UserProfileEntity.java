package dev.edu.ngochandev.socialservice.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Node("UserProfile")
@Getter
@Setter
public class UserProfileEntity extends BaseNodeEntity{
    @Property("user_id")
    private Long userId;

    @Property("full_name")
    private String fullName;

    @Property("avatar")
    private String avatar;

    @Property("date_of_birth")
    private String dateOfBirth;

    @Property("phone_number")
    private String phoneNumber;

    @Property("school")
    private String school;

    @Property("social_media_links")
    private Map<String, String> socialMediaLinks;

    @Relationship(type = "CONNECTION", direction = Relationship.Direction.OUTGOING)
    private Set<ConnectionRelation> connections = new HashSet<>();
}

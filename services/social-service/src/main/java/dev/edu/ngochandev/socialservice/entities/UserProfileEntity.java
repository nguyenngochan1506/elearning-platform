package dev.edu.ngochandev.socialservice.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@Document(collection = "user_profiles")
public class UserProfileEntity extends BaseNodeEntity{

    @Field("user_id")
    @Indexed(unique = true)
    private Long userId;

    @Field("full_name")
    private String fullName;

    @Field("avatar")
    private String avatar;

    @Field("date_of_birth")
    private LocalDate dateOfBirth;

    @Field("phone_number")
    private String phoneNumber;

    @Field("school")
    private String school;

    @Field("social_media_links")
    private Map<String, String> socialMediaLinks;
}

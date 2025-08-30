package dev.edu.ngochandev.courseservice.features.course.services;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface CourseSecurityService {
    boolean isOwner(String resourceUuid, Jwt jwt);

    boolean isOwner(List<String> resourceUuid, Jwt jwt);

}

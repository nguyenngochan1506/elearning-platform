package dev.edu.ngochandev.courseservice.features.course.services;

import org.springframework.security.oauth2.jwt.Jwt;

public interface CourseSecurityService {
    boolean isOwner(String courseUuid, Jwt jwt);
}

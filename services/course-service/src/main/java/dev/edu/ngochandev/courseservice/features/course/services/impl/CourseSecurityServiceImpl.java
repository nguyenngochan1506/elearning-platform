package dev.edu.ngochandev.courseservice.features.course.services.impl;

import dev.edu.ngochandev.courseservice.commons.enums.AuthorshipType;
import dev.edu.ngochandev.courseservice.features.course.repositories.ResourceAuthorRepository;
import dev.edu.ngochandev.courseservice.features.course.services.CourseSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("courseSecurityService")
@RequiredArgsConstructor
public class CourseSecurityServiceImpl implements CourseSecurityService {
     private final ResourceAuthorRepository resourceAuthorRepository;

    @Override
    public boolean isOwner(String courseUuid, Jwt jwt) {
        if (courseUuid == null || jwt == null) {
            return false;
        }

        Long userId = jwt.getClaim("userId");
        if (userId == null) {
            return false;
        }

         return resourceAuthorRepository.existsByResourceUuidAndUserIdAndAuthorshipRoleIn(
             courseUuid, userId, List.of(AuthorshipType.AUTHOR, AuthorshipType.MAINTAINER)
         );
    }
}

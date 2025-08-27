package dev.edu.ngochandev.authservice.controllers;

import dev.edu.ngochandev.authservice.entities.OrganizationEntity;
import dev.edu.ngochandev.authservice.repositories.OrganizationRepository;
import dev.edu.ngochandev.common.dtos.res.SuccessResponseDto;
import dev.edu.ngochandev.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationRepository organizationRepository;

    @GetMapping("/slug/{slug}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<Long> getOrganizationIdBySlug(@PathVariable String slug) {
        OrganizationEntity org = organizationRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("error.organization.not-found"));
        return SuccessResponseDto.<Long>builder()
                .status(HttpStatus.OK.value())
                .data(org.getId())
                .build();
    }
}
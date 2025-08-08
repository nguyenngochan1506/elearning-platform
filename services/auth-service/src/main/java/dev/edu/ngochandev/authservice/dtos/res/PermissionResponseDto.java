package dev.edu.ngochandev.authservice.dtos.res;

import lombok.Builder;

@Builder
public record PermissionResponseDto (
	Long id,
	String name,
	String method,
	String endpoint,
	String module
){}

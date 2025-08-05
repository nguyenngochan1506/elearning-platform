package dev.edu.ngochandev.authservice.dtos.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PermissionResponseDto {
    private Long id;
    private String name;
    private String method;
    private String endpoint;
    private String module;
}

package dev.edu.ngochandev.authservice.dtos.req;

import lombok.Getter;

import java.util.List;

@Getter
public class RoleManyDeleteRequestDto {
    private List<Long> ids = List.of();
}

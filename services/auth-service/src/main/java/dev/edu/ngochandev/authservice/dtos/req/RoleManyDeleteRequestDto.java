package dev.edu.ngochandev.authservice.dtos.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleManyDeleteRequestDto {
    private List<Long> ids = List.of();
}

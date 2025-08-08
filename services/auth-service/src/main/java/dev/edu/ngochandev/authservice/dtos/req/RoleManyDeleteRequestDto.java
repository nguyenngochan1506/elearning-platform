package dev.edu.ngochandev.authservice.dtos.req;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleManyDeleteRequestDto {
    private List<Long> ids = List.of();
}

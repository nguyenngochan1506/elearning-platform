package dev.edu.ngochandev.authservice.dtos.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UserManyDeleteRequestDto{
    private final List<Long> ids = List.of();
}

package dev.edu.ngochandev.authservice.dtos.req;

import java.util.List;

public record UserManyDeleteRequestDto(List<Long> ids) {
}

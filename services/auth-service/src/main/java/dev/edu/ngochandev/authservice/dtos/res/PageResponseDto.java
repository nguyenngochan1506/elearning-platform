package dev.edu.ngochandev.authservice.dtos.res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PageResponseDto<T> {
    private int currentPage;
    private long totalElements;
    private int totalPages;
    private List<T> items;
}

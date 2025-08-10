package dev.edu.ngochandev.common.dtos.res;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageResponseDto<T> {
    private int currentPage;
    private long totalElements;
    private int totalPages;
    private List<T> items;
}

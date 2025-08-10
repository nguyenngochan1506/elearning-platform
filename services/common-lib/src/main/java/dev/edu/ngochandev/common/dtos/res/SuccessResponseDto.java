package dev.edu.ngochandev.common.dtos.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessResponseDto<T> {
    private int status;
    private String message;
    private T data;
}

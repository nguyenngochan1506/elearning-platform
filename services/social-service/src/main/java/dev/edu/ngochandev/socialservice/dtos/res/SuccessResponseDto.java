package dev.edu.ngochandev.socialservice.dtos.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SuccessResponseDto <T>{
    private int status;
    private String message;
    private T data;
}

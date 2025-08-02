package dev.edu.ngochandev.authservice.dtos.res;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessResponse {
    private int status;
    private String message;
    private Object data;
}

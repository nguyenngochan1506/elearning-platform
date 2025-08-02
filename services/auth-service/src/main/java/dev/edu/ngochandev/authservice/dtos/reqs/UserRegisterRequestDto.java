package dev.edu.ngochandev.authservice.dtos.reqs;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class UserRegisterRequestDto implements Serializable {
    private String username;
    private String fullName;
    private String email;
    private String password;
}

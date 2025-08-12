package dev.edu.ngochandev.common.dtos.res;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntrospectTokenResponseDto {
    private boolean active;
    private Long userId;
    private String username;
    private Set<String> roles;
    private Set<String> permissions;
    private Long exp;
}

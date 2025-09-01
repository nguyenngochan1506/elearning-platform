package dev.edu.ngochandev.common.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateOrUpdateEvent {
    private Long userId;
    private Boolean isActive;
    private String uuid;
    private String name;
    private String thumbnail;
}

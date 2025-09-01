package dev.edu.ngochandev.common.dtos.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UuidsRequestDto {
    private List<String> uuids;
}

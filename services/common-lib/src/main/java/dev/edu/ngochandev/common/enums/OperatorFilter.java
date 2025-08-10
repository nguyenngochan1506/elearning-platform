package dev.edu.ngochandev.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum OperatorFilter {
    @JsonProperty("eq")
    EQUALS("eq"),
    @JsonProperty("contains")
    CONTAINS("contains"),
    @JsonProperty("gt")
    GREATER_THAN("gt"),
    @JsonProperty("lt")
    LESS_THAN("lt"),
    @JsonProperty("between")
    BETWEEN("between"),
    @JsonProperty("in")
    IN("in");

    private final String value;

    OperatorFilter(String value) {
        this.value = value;
    }
}

package dev.edu.ngochandev.authservice.commons.enums;

import lombok.Getter;

@Getter
public enum MailStatus {
    PENDING("pending"),
    SENT("sent"),
    FAILED("failed");

    private final String status;

    MailStatus(String status) {
        this.status = status;
    }
}

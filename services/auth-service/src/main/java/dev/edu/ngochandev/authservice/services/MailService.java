package dev.edu.ngochandev.authservice.services;

import dev.edu.ngochandev.authservice.entities.MailEntity;

import java.util.Map;

public interface MailService {
    boolean sendMail(MailEntity mail, String templateName, Map<String, Object> variables);
}

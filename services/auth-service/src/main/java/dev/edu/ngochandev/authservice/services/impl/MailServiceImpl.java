package dev.edu.ngochandev.authservice.services.impl;

import dev.edu.ngochandev.authservice.commons.enums.MailStatus;
import dev.edu.ngochandev.authservice.entities.MailEntity;
import dev.edu.ngochandev.authservice.repositories.MailRepository;
import dev.edu.ngochandev.authservice.services.MailService;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "MAIL-SERVICE")
public class MailServiceImpl implements MailService {
    private final MailRepository mailRepository;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public boolean sendMail(MailEntity mail, String templateName, Map<String, Object> variables) {
        mail.setFrom(from);
        mail.setStatus(MailStatus.PENDING);

        // create context
        Context context = new Context();
        context.setVariables(variables);

        // render template
        String htmlContent = templateEngine.process("/email/" + templateName, context);
        mail.setContent(htmlContent);

        // send mail
        MailEntity savedMail = mailRepository.save(mail);
        log.info("Sending mail to: {}", savedMail.getTo());

        try {
            MimeMessage mimeMailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, "UTF-8");
            helper.setText(mail.getContent(), true);
            helper.setTo(savedMail.getTo());
            helper.setFrom(savedMail.getFrom());
            helper.setSubject(savedMail.getSubject());
            mailSender.send(mimeMailMessage);

            savedMail.setStatus(MailStatus.SENT);
            log.info("Email sent to: {}", savedMail.getTo());
        } catch (Exception e) {
            savedMail.setStatus(MailStatus.FAILED);
            log.error("Failed to send email to: {}", savedMail.getTo());
            mailRepository.save(savedMail);
            return false;
        }
        mailRepository.save(savedMail);
        return true;
    }
}

package dev.edu.ngochandev.authservice.entities;

import dev.edu.ngochandev.authservice.commons.enums.MailStatus;
import dev.edu.ngochandev.authservice.commons.enums.MailType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_emails")
@Setter
@Getter
public class MailEntity extends BaseEntity {

	@Column(name = "sender_address",nullable = false)
	private String from;

	@Column(name = "recipient_address", nullable = false)
	private String to;

	@Column(name = "subject", nullable = false, length = 512)
	private String subject;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private MailStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private MailType type;
}

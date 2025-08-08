package dev.edu.ngochandev.authservice.commons.enums;

import lombok.Getter;

@Getter
public enum MailType {
	FORGOT_PASSWORD("forgot-password"),
	RESET_PASSWORD("reset-password"),
	ACCOUNT_ACTIVATION("account-activation"),
	EMAIL_VERIFICATION("email-verification");

	private final String type;

	MailType(String type) {
		this.type = type;
	}
}

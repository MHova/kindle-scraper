package com.mhova.kindleScraper;

public class EmailConfiguration {
	public final String user;
	public final String password;
	public final String recipient;
	public final String smtpHost;
	public final int smtpPort;

	public EmailConfiguration(final String user, final String password, final String recipient, final String smtpHost,
			final int smtpPort) {
		super();
		this.user = user;
		this.password = password;
		this.recipient = recipient;
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
	}
}

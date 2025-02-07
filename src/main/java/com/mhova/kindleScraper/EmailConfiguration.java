package com.mhova.kindleScraper;

// TODO: Add a custom Jackson InternetAddress deserializer so that the recipient field can be type InternetAddress and recipient config validation will happen at startup
public record EmailConfiguration(String user, String password, String recipient, String smtpHost, int smtpPort)
		implements NotificationConfiguration {
}

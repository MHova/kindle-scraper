package com.mhova.kindleScraper;

public record EmailConfiguration(String user, String password, String recipient, String smtpHost, int smtpPort) {
}

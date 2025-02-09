package com.mhova.kindleScraper.core;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import com.mhova.kindleScraper.email.EmailSender;

public class EmailNotifier implements PriceDropNotifier {
	private final EmailSender emailSender;

	public EmailNotifier(final EmailSender emailSender) {
		super();
		this.emailSender = emailSender;
	}

	@Override
	public void notify(final Instant previousTimestamp, final double previousPrice, final Instant currentTimestamp,
			final double currentPrice) {
		emailSender.sendEmail("Kindle Price Drop Alert",
				"The Kindle was previously $%.2f at %s, but has dropped to $%.2f at %s.".formatted(previousPrice,
						DateTimeFormatter.ISO_INSTANT.format(previousTimestamp), currentPrice,
						DateTimeFormatter.ISO_INSTANT.format(currentTimestamp)));
	}

	@Override
	public String getType() {
		return "email";
	}
}

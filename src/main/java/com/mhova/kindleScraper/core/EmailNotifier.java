package com.mhova.kindleScraper.core;

import com.mhova.kindleScraper.email.EmailSender;

public class EmailNotifier implements PriceDropNotifier {
	private final EmailSender emailSender;

	public EmailNotifier(final EmailSender emailSender) {
		super();
		this.emailSender = emailSender;
	}

	@Override
	public void notify(double previousPrice, double currentPrice) {
		emailSender.sendEmail("Kindle Price Drop Alert",
				"The Kindle was previously $%.2f, but is now $%.2f.".formatted(previousPrice, currentPrice));
	}

	@Override
	public String getType() {
		return "email";
	}
}

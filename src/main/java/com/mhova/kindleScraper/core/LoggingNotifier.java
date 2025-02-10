package com.mhova.kindleScraper.core;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingNotifier implements PriceDropNotifier {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingNotifier.class);

	@Override
	public void notify(final Instant previousTimestamp, final double previousPrice, final Instant currentTimestamp,
			final double currentPrice) {
		LOGGER.info("Kindle Price Drop Alert! The Kindle was previously $%.2f at %s, but has dropped to $%.2f at %s."
				.formatted(previousPrice, DateTimeFormatter.ISO_INSTANT.format(previousTimestamp), currentPrice,
						DateTimeFormatter.ISO_INSTANT.format(currentTimestamp)));
	}

	@Override
	public String getType() {
		return "logging";
	}
}

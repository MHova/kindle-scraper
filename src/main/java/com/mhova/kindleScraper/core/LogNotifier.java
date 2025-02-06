package com.mhova.kindleScraper.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogNotifier implements PriceDropNotifier {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogNotifier.class);

	@Override
	public void notify(double previousPrice, double currentPrice) {
		LOGGER.info("Kindle Price Drop Alert! The Kindle was previously $%.2f, but is now $%.2f."
				.formatted(previousPrice, currentPrice));
	}
}

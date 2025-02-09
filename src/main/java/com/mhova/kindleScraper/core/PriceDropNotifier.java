package com.mhova.kindleScraper.core;

import java.time.Instant;

public interface PriceDropNotifier {
	public void notify(final Instant previousTimestamp, final double previousPrice, final Instant currentTimestamp,
			final double currentPrice);

	public String getType();
}

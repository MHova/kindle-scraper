package com.mhova.kindleScraper.core;

public interface PriceDropNotifier {
	public void notify(final double previousPrice, final double currentPrice);
	public String getType();
}

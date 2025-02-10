package com.mhova.kindleScraper.db;

import java.time.Instant;

public record PriceCheck(Instant timestamp, double price) {

}

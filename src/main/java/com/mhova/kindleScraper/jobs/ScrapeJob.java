package com.mhova.kindleScraper.jobs;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mhova.kindleScraper.DocumentProvider;
import com.mhova.kindleScraper.core.PriceDropNotifier;
import com.mhova.kindleScraper.db.PricesDAO;

import io.dropwizard.jobs.Job;
import io.dropwizard.jobs.annotations.Every;

@Every
public class ScrapeJob extends Job {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapeJob.class);
	private final PricesDAO dao;
	private final PriceDropNotifier notifier;
	private final DocumentProvider documentProvider;

	public ScrapeJob(final PricesDAO dao, final PriceDropNotifier notifier, final DocumentProvider documentProvider) {
		this.dao = dao;
		this.notifier = notifier;
		this.documentProvider = documentProvider;
	}

	@Override
	public void doJob(final JobExecutionContext context) throws JobExecutionException {
		final Document document;

		try {
			document = documentProvider.getDocument();
		} catch (final IOException e) {
			throw new JobExecutionException(e);
		}

		// @formatter:off
		/*
		 * The pertinent parts of the html look something like this:
		 *
		 * <div ... data-feature-name="corePriceDisplay_desktop" ...>
		 * ...
		 * <span class="a-price-symbol">$</span>
		 * <span class="a-price-whole">94
		 * <span class="a-price-decimal">.</span></span>
		 * <span class="a-price-fraction">99</span>
		 * ...
		 * </div>
		 */
		// @formatter:on
		final Element priceDisplayDiv = document.selectFirst("div[data-feature-name='corePriceDisplay_desktop']");
		final String wholeDollarAndDecimalPoint = priceDisplayDiv.selectFirst("span.a-price-whole").text();
		final String cents = priceDisplayDiv.selectFirst("span.a-price-fraction").text();

		final Double newPrice = Double.parseDouble(wholeDollarAndDecimalPoint + cents);
		LOGGER.info("Kindle price is now " + newPrice);

		// if this is the very first run of the job, then there is no previous price in
		// the DB
		final Optional<Double> maybePreviousPrice = Optional.ofNullable(dao.findLatestPrice());
		dao.insert(Instant.now(), newPrice);

		maybePreviousPrice.ifPresent(previousPrice -> {
			if(newPrice < previousPrice) {
				LOGGER.info("Kindle price dropped from $%.2f to $%.2f! Sending notification.".formatted(previousPrice,
						newPrice));
				notifier.notify(previousPrice, newPrice);
			}
		});
	}
}

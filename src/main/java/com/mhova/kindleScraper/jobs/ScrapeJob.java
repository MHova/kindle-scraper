package com.mhova.kindleScraper.jobs;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.jdbi.v3.core.Jdbi;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mhova.kindleScraper.db.PricesDAO;

import io.dropwizard.jobs.Job;
import io.dropwizard.jobs.annotations.Every;

@Every
public class ScrapeJob extends Job {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapeJob.class);
	private final Jdbi jdbi;

	public ScrapeJob(final Jdbi jdbi) {
		this.jdbi = jdbi;
	}

	@Override
	public void doJob(final JobExecutionContext context) throws JobExecutionException {
		final File html = new File("src/main/resources/kindle.htm");

		Document document = null;

		try {
			document = Jsoup.parse(html);
		} catch (final IOException e) {
			LOGGER.error(e.getMessage());
		}

		if (document != null) {
			final Element priceDisplayDiv = document.selectFirst("div[data-feature-name='corePriceDisplay_desktop']");
			final String wholeDollarAndDecimalPoint = priceDisplayDiv.selectFirst("span.a-price-whole").text();
			final String cents = priceDisplayDiv.selectFirst("span.a-price-fraction").text();

			final Double newPrice = Double.parseDouble(wholeDollarAndDecimalPoint + cents);
			LOGGER.info("Kindle price is now " + newPrice);

			final PricesDAO dao = jdbi.onDemand(PricesDAO.class);
			final Optional<Double> maybePreviousPrice = Optional.ofNullable(dao.findLatestPrice());
			dao.insert(Instant.now(), newPrice);

			if (maybePreviousPrice.isPresent() && newPrice < maybePreviousPrice.get()) {
				LOGGER.info("PRICE DROP!!!!");
			}
		}
	}

}

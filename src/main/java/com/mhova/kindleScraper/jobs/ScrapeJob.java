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
import com.mhova.kindleScraper.db.PriceCheck;
import com.mhova.kindleScraper.db.PriceCheckDAO;

import io.dropwizard.jobs.Job;
import io.dropwizard.jobs.annotations.Every;

@Every
public class ScrapeJob extends Job {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapeJob.class);
	private final PriceCheckDAO dao;
	private final PriceDropNotifier notifier;
	private final DocumentProvider documentProvider;
	private final double minimumPriceDecrease;

	public ScrapeJob(final PriceCheckDAO dao, final PriceDropNotifier notifier, final DocumentProvider documentProvider,
			final double minimumPriceDecrease) {
		this.dao = dao;
		this.notifier = notifier;
		this.documentProvider = documentProvider;
		this.minimumPriceDecrease = minimumPriceDecrease;
	}

	@Override
	public void doJob(final JobExecutionContext _context) throws JobExecutionException {
		final Document document;

		try {
			document = documentProvider.getDocument();
		} catch (final IOException e) {
			// If something goes wrong with retrieving the document (perhaps because the
			// website is temporarily down), stop the current execution of the job, but try
			// again later as per the specified schedule. However...(continued in try/catch
			// below)
			LOGGER.error(e.getMessage());
			throw new JobExecutionException(e);
		}

		Double newPrice;

		try {
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

			newPrice = Double.parseDouble(wholeDollarAndDecimalPoint + cents);
		} catch (final Exception e) {
			// ...if something went wrong while parsing the document, that probably means
			// the structure of the website has changed or we got hit with a captcha. Either
			// way, human intervention is needed to fix the problem. Stop pinging the
			// website altogether by unscheduling this job.
			final JobExecutionException jee = new JobExecutionException(e);
			jee.setUnscheduleAllTriggers(true);
			LOGGER.error("Error while parsing document. Unscheduling ScrapeJob.");
			LOGGER.error(e.getMessage());
			throw jee;
		}

		LOGGER.info("Kindle price is now " + newPrice);

		// if this is the very first run of the job, then there will be no previous
		// price in the DB
		final Optional<PriceCheck> maybePreviousPriceCheck = Optional.ofNullable(dao.findLatestPriceCheck());
		final Instant now = Instant.now();
		dao.insert(new PriceCheck(now, newPrice));

		maybePreviousPriceCheck.ifPresent(previousPriceCheck -> {
			if (newPrice + minimumPriceDecrease <= previousPriceCheck.price()) {
				LOGGER.info("Kindle price dropped from $%.2f to $%.2f! Sending notification via %s."
						.formatted(previousPriceCheck.price(), newPrice, notifier.getType()));
				notifier.notify(previousPriceCheck.timestamp(), previousPriceCheck.price(), now, newPrice);
			}
		});
	}
}

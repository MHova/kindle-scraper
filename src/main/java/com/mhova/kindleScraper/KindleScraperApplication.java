package com.mhova.kindleScraper;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infobip.jackson.InfobipJacksonModule;
import com.mhova.kindleScraper.core.EmailNotifier;
import com.mhova.kindleScraper.core.LoggingNotifier;
import com.mhova.kindleScraper.core.PriceDropNotifier;
import com.mhova.kindleScraper.db.PricesDAO;
import com.mhova.kindleScraper.email.EmailSender;
import com.mhova.kindleScraper.email.Session;
import com.mhova.kindleScraper.email.Transport;
import com.mhova.kindleScraper.jobs.ScrapeJob;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jobs.JobsBundle;

public class KindleScraperApplication extends Application<KindleScraperConfiguration> {
	private static final Logger LOGGER = LoggerFactory.getLogger(KindleScraperApplication.class);

	public static void main(final String[] args) throws Exception {
		new KindleScraperApplication().run(args);
	}

	@Override
	public String getName() {
		return "KindleScraper";
	}

	@Override
	public void initialize(final Bootstrap<KindleScraperConfiguration> bootstrap) {
		bootstrap.getObjectMapper().registerModule(new InfobipJacksonModule());
	}

	@Override
	public void run(final KindleScraperConfiguration configuration, final Environment environment) throws Exception {
		final Jdbi jdbi = new JdbiFactory().build(environment, configuration.getDataSourceFactory(), "h2");

		// @formatter:off
		final PriceDropNotifier notifier =
			switch (configuration.getNotificationConfig()) {
				case EmailConfiguration ec ->
					new EmailNotifier(new EmailSender(ec, new Transport(), new Session()));
				case LoggingConfiguration _lc -> new LoggingNotifier();
		};
		// @formatter:on

		// log this info for sanity checking
		LOGGER.info("Document source: %s".formatted(configuration.getDocumentProvider().getClass().toString()));
		LOGGER.info("Notification medium: %s".formatted(notifier.getClass().toString()));

		final PricesDAO dao = jdbi.onDemand(PricesDAO.class);
		dao.createPricesTable();

		final JobsBundle jobsBundle = new JobsBundle(
				List.of(new ScrapeJob(dao, notifier, configuration.getDocumentProvider())));
		jobsBundle.run(configuration, environment);
	}
}

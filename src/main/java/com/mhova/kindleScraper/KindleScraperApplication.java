package com.mhova.kindleScraper;

import java.util.List;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.mhova.kindleScraper.core.EmailNotifier;
import com.mhova.kindleScraper.core.EmailSender;
import com.mhova.kindleScraper.core.LoggingNotifier;
import com.mhova.kindleScraper.core.PriceDropNotifier;
import com.mhova.kindleScraper.db.PricesDAO;
import com.mhova.kindleScraper.jobs.ScrapeJob;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jobs.JobsBundle;

public class KindleScraperApplication extends Application<KindleScraperConfiguration> {

	public static void main(final String[] args) throws Exception {
		new KindleScraperApplication().run(args);
	}

	@Override
	public String getName() {
		return "KindleScraper";
	}

	@Override
	public void initialize(final Bootstrap<KindleScraperConfiguration> bootstrap) {
		bootstrap.getObjectMapper().registerModule(new ParameterNamesModule());
	}

	@Override
	public void run(final KindleScraperConfiguration configuration, final Environment environment) throws Exception {
		final Jdbi jdbi = new JdbiFactory().build(environment, configuration.getDataSourceFactory(), "h2");

		final PriceDropNotifier notifier =
			switch (configuration.getNotificationConfig()) {
				case EmailConfiguration ec -> new EmailNotifier(new EmailSender(ec));
				case LoggingConfiguration _lc -> new LoggingNotifier();
		};

		final PricesDAO dao = jdbi.onDemand(PricesDAO.class);
		dao.createPricesTable();

		final JobsBundle jobsBundle = new JobsBundle(
			List.of(new ScrapeJob(dao, notifier, configuration.getDocumentProvider())));
		jobsBundle.run(configuration, environment);
	}

}

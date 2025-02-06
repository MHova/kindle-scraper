package com.mhova.kindleScraper;

import java.util.List;

import org.jdbi.v3.core.Jdbi;

import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.mhova.kindleScraper.core.EmailNotifier;
import com.mhova.kindleScraper.core.EmailSender;
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
		final JdbiFactory factory = new JdbiFactory();
		final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "h2");
		final EmailSender emailSender = new EmailSender(configuration.getEmailConfig());
		final PriceDropNotifier notifier = new EmailNotifier(emailSender);

		jdbi.onDemand(PricesDAO.class).createPricesTable();

		final JobsBundle jobsBundle = new JobsBundle(List.of(new ScrapeJob(jdbi, notifier)));
		jobsBundle.run(configuration, environment);
	}

}

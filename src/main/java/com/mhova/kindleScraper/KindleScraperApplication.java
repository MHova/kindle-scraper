package com.mhova.kindleScraper;

import java.util.List;

import com.mhova.kindleScraper.jobs.ScrapeJob;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
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
		bootstrap.addBundle(new JobsBundle(List.of(new ScrapeJob())));
	}

	@Override
	public void run(final KindleScraperConfiguration configuration, final Environment environment) {
		// TODO: implement application
	}

}

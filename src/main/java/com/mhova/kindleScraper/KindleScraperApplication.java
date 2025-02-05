package com.mhova.kindleScraper;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

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
		// TODO: application initialization
	}

	@Override
	public void run(final KindleScraperConfiguration configuration, final Environment environment) {
		// TODO: implement application
	}

}

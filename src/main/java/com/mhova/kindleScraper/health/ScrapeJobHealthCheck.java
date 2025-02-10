package com.mhova.kindleScraper.health;

import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;

import com.codahale.metrics.health.HealthCheck;
import com.mhova.kindleScraper.jobs.ScrapeJob;

import io.dropwizard.jobs.JobsBundle;

public class ScrapeJobHealthCheck extends HealthCheck {
	private final JobsBundle jobsBundle;

	public ScrapeJobHealthCheck(JobsBundle jobsBundle) {
		this.jobsBundle = jobsBundle;
	}

	@Override
	protected Result check() throws Exception {
		final Trigger t = jobsBundle.getScheduler()
				.getTriggersOfJob(JobKey.jobKey(ScrapeJob.class.getCanonicalName(), JobKey.DEFAULT_GROUP)).getFirst();

		final TriggerState triggerState = jobsBundle.getScheduler().getTriggerState(t.getKey());

		switch (triggerState) {
		case TriggerState.NORMAL:
			return Result.healthy();
		default:
			return Result.unhealthy("ScrapeJob is no longer running. Its TriggerState is %s", triggerState);
		}
	}

}

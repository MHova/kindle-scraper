package com.mhova.kindleScraper.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import io.dropwizard.jobs.Job;
import io.dropwizard.jobs.annotations.Every;

@Every
public class ScrapeJob extends Job {

	@Override
	public void doJob(JobExecutionContext context) throws JobExecutionException {
		System.out.println("hello world");
	}

}

package com.mhova.kindleScraper;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.jobs.JobConfiguration;

public class KindleScraperConfiguration extends JobConfiguration {
	@JsonProperty("jobs")
	private Map<String , String> jobs;

	public Map<String, String> getJobs() {
	    return jobs;
	}

	public void setJobs(Map<String, String> jobs) {
	    this.jobs = jobs;
	}
}

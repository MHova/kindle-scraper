package com.mhova.kindleScraper;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jobs.JobConfiguration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class KindleScraperConfiguration extends JobConfiguration {
	@Valid
	@NotNull
	@JsonProperty("jobs")
	private Map<String, String> jobs;

	public Map<String, String> getJobs() {
		return jobs;
	}

	public void setJobs(Map<String, String> jobs) {
		this.jobs = jobs;
	}

	@Valid
	@NotNull
	private DataSourceFactory database = new DataSourceFactory();

	@JsonProperty("database")
	public void setDataSourceFactory(DataSourceFactory factory) {
		this.database = factory;
	}

	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}

	@Valid
	@NotNull
	@JsonProperty("notification")
	private NotificationConfiguration notificationConfig;

	public NotificationConfiguration getNotificationConfig() {
		return notificationConfig;
	}

	public void setNotificationConfig(NotificationConfiguration notificationConfig) {
		this.notificationConfig = notificationConfig;
	}

	@Valid
	@NotNull
	@JsonProperty("document")
	private DocumentProvider documentProvider;

	public DocumentProvider getDocumentProvider() {
		return documentProvider;
	}

	public void setDocumentProvider(DocumentProvider documentProvider) {
		this.documentProvider = documentProvider;
	}

	@Valid
	@NotNull
	@JsonProperty("minimumPriceDecrease")
	private double minimumPriceDecrease;

	public double getMinimumPriceDecrease() {
		return minimumPriceDecrease;
	}

	public void setMinimumPriceDecrease(double minimumPriceDecrease) {
		this.minimumPriceDecrease = minimumPriceDecrease;
	}
}

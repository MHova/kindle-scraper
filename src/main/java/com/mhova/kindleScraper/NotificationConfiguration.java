package com.mhova.kindleScraper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
	@JsonSubTypes.Type(value = EmailConfiguration.class, name = "email"),
	@JsonSubTypes.Type(value = LoggingConfiguration.class, name = "logging") })
public sealed interface NotificationConfiguration permits EmailConfiguration, LoggingConfiguration {
}

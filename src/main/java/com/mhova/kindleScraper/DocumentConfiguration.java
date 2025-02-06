package com.mhova.kindleScraper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
	@JsonSubTypes.Type(value = FileDocumentConfiguration.class, name = "file"),
	@JsonSubTypes.Type(value = WebDocumentConfiguration.class, name = "web")})
public sealed interface DocumentConfiguration permits FileDocumentConfiguration, WebDocumentConfiguration {

}

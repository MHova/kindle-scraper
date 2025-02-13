package com.mhova.kindleScraper;

import java.io.IOException;

import org.jsoup.nodes.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
	@JsonSubTypes.Type(value = FileDocumentProvider.class, name = "file"),
	@JsonSubTypes.Type(value = WebDocumentProvider.class, name = "web")})
public sealed interface DocumentProvider permits FileDocumentProvider, WebDocumentProvider {
	Document getDocument() throws IOException;
}

package com.mhova.kindleScraper;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class WebDocumentProvider implements DocumentProvider {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36";
	private final URL url;

	@JsonCreator
	public WebDocumentProvider(@JsonProperty("url") final URL url) {
		super();
		this.url = url;
	}

	@Override
	public Document getDocument() throws IOException {
		return Jsoup.connect(url.toExternalForm()).userAgent(USER_AGENT).get();
	}
}

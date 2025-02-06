package com.mhova.kindleScraper;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class FileDocumentProvider implements DocumentProvider {
	private final String fileLocation;

	@JsonCreator
	public FileDocumentProvider(@JsonProperty("fileLocation") final String fileLocation) {
		super();
		this.fileLocation = fileLocation;
	}

	@Override
	public Document getDocument() throws IOException {
		// NB: purposefully rereading the document every time to allow one to modify the
		// file on the fly while the server is running
		return Jsoup.parse(new File(fileLocation));
	}
}

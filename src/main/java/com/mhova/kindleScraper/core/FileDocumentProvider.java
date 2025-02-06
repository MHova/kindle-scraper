package com.mhova.kindleScraper.core;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FileDocumentProvider implements DocumentProvider {
	private final String fileLocation;

	public FileDocumentProvider(final String fileLocation) {
		super();
		this.fileLocation = fileLocation;
	}

	@Override
	public Document getDocument() throws IOException {
		return Jsoup.parse(new File(fileLocation));
	}
}

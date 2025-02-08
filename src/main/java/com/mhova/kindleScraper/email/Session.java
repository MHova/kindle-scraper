package com.mhova.kindleScraper.email;

import java.util.Properties;

import javax.mail.Authenticator;

public class Session {
	public javax.mail.Session getInstance(final Properties properties, final Authenticator authenticator) {
		return javax.mail.Session.getInstance(properties, authenticator);
	}
}

package com.mhova.kindleScraper.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;

public class SessionProxy {
	public Session getInstance(final Properties properties, final Authenticator authenticator) {
		return Session.getInstance(properties, authenticator);
	}
}

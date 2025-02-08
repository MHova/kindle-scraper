package com.mhova.kindleScraper.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class Transport {
	public void send(final MimeMessage msg) throws MessagingException {
		javax.mail.Transport.send(msg);
	}
}

package com.mhova.kindleScraper.email;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class TransportProxy {
	public void send(final MimeMessage msg) throws MessagingException {
		Transport.send(msg);
	}
}

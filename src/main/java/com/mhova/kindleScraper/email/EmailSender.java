package com.mhova.kindleScraper.email;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mhova.kindleScraper.EmailConfiguration;

public class EmailSender {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

	private final EmailConfiguration emailConfig;
	private final TransportProxy transportProxy;
	private final SessionProxy sessionProxy;

	public EmailSender(final EmailConfiguration emailConfig, final TransportProxy transportProxy,
			final SessionProxy sessionProxy) {
		this.emailConfig = emailConfig;
		this.transportProxy = transportProxy;
		this.sessionProxy = sessionProxy;
	}

	public void sendEmail(final String subject, final String body) {
		final Properties props = new Properties();
		props.put("mail.smtp.host", emailConfig.smtpHost());
		props.put("mail.smtp.port", emailConfig.smtpPort());
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		final Session session = sessionProxy.getInstance(props, emailConfig.user(), emailConfig.password());

		try {
			final MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress(emailConfig.user(), "ECFX"));
			msg.setReplyTo(InternetAddress.parse(emailConfig.user(), false));
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailConfig.recipient(), false));
			transportProxy.send(msg);
		} catch (final MessagingException e) {
			LOGGER.error(e.getMessage());
		} catch (final UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage());
		}
	}
}

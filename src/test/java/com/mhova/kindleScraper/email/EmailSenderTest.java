package com.mhova.kindleScraper.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mhova.kindleScraper.EmailConfiguration;

@ExtendWith(MockitoExtension.class)
class EmailSenderTest {
	private EmailSender emailSender;
	private EmailConfiguration config;

	@Mock
	private TransportProxy transportProxy;

	@Mock
	private SessionProxy sessionProxy;

	@Captor
	private ArgumentCaptor<MimeMessage> mimeMessageCaptor;

	@BeforeEach
	void setup() {
		this.config = new EmailConfiguration("theUser", "thePassword", "theRecipient", "theHost", 1337);
		this.emailSender = new EmailSender(config, transportProxy, sessionProxy);
	}

	@Test
	void correctlyConstructSession() {
		final Properties props = new Properties();
		props.put("mail.smtp.host", "theHost");
		props.put("mail.smtp.port", 1337);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		emailSender.sendEmail("Hello", "World");

		verify(sessionProxy).getInstance(props, "theUser", "thePassword");
	}

	@Test
	void correctlyConstructMessage() throws MessagingException, IOException {
		emailSender.sendEmail("Hello", "World");
		verify(transportProxy).send(mimeMessageCaptor.capture());

		final MimeMessage msg = mimeMessageCaptor.getValue();

		assertEquals("Hello", msg.getSubject());
		assertEquals("World", msg.getContent());

		final Address[] recipients = msg.getRecipients(RecipientType.TO);
		assertEquals(1, recipients.length);
		assertEquals("theRecipient", recipients[0].toString());

		final Address[] replyTo = msg.getReplyTo();
		assertEquals(1, replyTo.length);
		assertEquals("theUser", replyTo[0].toString());

		final Address[] from = msg.getFrom();
		assertEquals(1, from.length);
		assertEquals("ECFX <theUser>", from[0].toString());
	}
}

package com.mhova.kindleScraper.jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionException;

import com.mhova.kindleScraper.DocumentProvider;
import com.mhova.kindleScraper.FileDocumentProvider;
import com.mhova.kindleScraper.core.PriceCheck;
import com.mhova.kindleScraper.core.PriceDropNotifier;
import com.mhova.kindleScraper.db.PriceCheckDAO;

@ExtendWith(MockitoExtension.class)
class ScrapeJobTest {
	private ScrapeJob scrapeJob;

	@Mock
	private PriceCheckDAO dao;

	@Mock
	private PriceDropNotifier notifier;

	@Captor
	private ArgumentCaptor<PriceCheck> priceCheckCaptor;

	private final DocumentProvider documentProvider = new FileDocumentProvider("src/test/resources/kindle.htm");

	@BeforeEach
	void setup() {
		this.scrapeJob = new ScrapeJob(dao, notifier, documentProvider, 0.01);
	}

	@Test
	void writesCorrectPriceToDB() throws JobExecutionException {
		scrapeJob.doJob(null);

		verify(dao).insert(priceCheckCaptor.capture());
		assertEquals(94.99, priceCheckCaptor.getValue().price());
	}

	@Test
	void doesNotNotifyIfNoPreviousPrice() throws JobExecutionException {
		when(dao.findLatestPriceCheck()).thenReturn(null);

		scrapeJob.doJob(null);

		verify(notifier, never()).notify(any(Instant.class), anyDouble(), any(Instant.class), anyDouble());
	}

	@Test
	void notifiesWhenPriceDrops() throws JobExecutionException {
		when(dao.findLatestPriceCheck()).thenReturn(new PriceCheck(Instant.now(), 95.0));

		scrapeJob.doJob(null);

		verify(notifier).notify(any(Instant.class), eq(95.0), any(Instant.class), eq(94.99));
	}

	@Test
	void doesNotNotifyIfPreviousPriceWasTheSame() throws JobExecutionException {
		when(dao.findLatestPriceCheck()).thenReturn(new PriceCheck(Instant.now(), 94.99));

		scrapeJob.doJob(null);

		verify(notifier, never()).notify(any(Instant.class), anyDouble(), any(Instant.class), anyDouble());
	}

	@Test
	void doesNotNotifyIfPreviousPriceIsWithinDelta() throws JobExecutionException {
		scrapeJob = new ScrapeJob(dao, notifier, documentProvider, 0.02);

		when(dao.findLatestPriceCheck()).thenReturn(new PriceCheck(Instant.now(), 95.0));

		scrapeJob.doJob(null);

		verify(notifier, never()).notify(any(Instant.class), anyDouble(), any(Instant.class), anyDouble());
	}

	@Test
	void notifiesWhenPriceDropsMoreThanDelta() throws JobExecutionException {
		when(dao.findLatestPriceCheck()).thenReturn(new PriceCheck(Instant.now(), 95.01));

		scrapeJob.doJob(null);

		verify(notifier).notify(any(Instant.class), eq(95.01), any(Instant.class), eq(94.99));
	}

	@Test
	void doesNotNotifyIfPreviousPriceWasLower() throws JobExecutionException {
		when(dao.findLatestPriceCheck()).thenReturn(new PriceCheck(Instant.now(), 94.98));

		scrapeJob.doJob(null);

		verify(notifier, never()).notify(any(Instant.class), anyDouble(), any(Instant.class), anyDouble());
	}
}

package com.mhova.kindleScraper.jobs;

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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionException;

import com.mhova.kindleScraper.DocumentProvider;
import com.mhova.kindleScraper.FileDocumentProvider;
import com.mhova.kindleScraper.core.PriceDropNotifier;
import com.mhova.kindleScraper.db.PricesDAO;

@ExtendWith(MockitoExtension.class)
class ScrapeJobTest {
	private ScrapeJob scrapeJob;

	@Mock
	private PricesDAO dao;

	@Mock
	private PriceDropNotifier notifier;

	private final DocumentProvider documentProvider = new FileDocumentProvider("src/test/resources/kindle.htm");

	@BeforeEach
	void setup() {
		this.scrapeJob = new ScrapeJob(dao, notifier, documentProvider);
	}

	@Test
	void writesCorrectPriceToDB() throws JobExecutionException {
		scrapeJob.doJob(null);

		verify(dao).insert(any(Instant.class), eq(94.99));
	}

	@Test
	void doesNotNotifyIfNoPreviousPrice() throws JobExecutionException {
		when(dao.findLatestPrice()).thenReturn(null);

		scrapeJob.doJob(null);

		verify(notifier, never()).notify(anyDouble(), anyDouble());
	}

	@Test
	void notifiesWhenPriceDrops() throws JobExecutionException {
		when(dao.findLatestPrice()).thenReturn(95.0);

		scrapeJob.doJob(null);

		verify(notifier).notify(95, 94.99);
	}

	@Test
	void doesNotNotifyIfPreviousPriceWasTheSame() throws JobExecutionException {
		when(dao.findLatestPrice()).thenReturn(94.99);

		scrapeJob.doJob(null);

		verify(notifier, never()).notify(anyDouble(), anyDouble());
	}
}

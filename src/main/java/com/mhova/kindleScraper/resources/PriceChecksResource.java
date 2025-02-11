package com.mhova.kindleScraper.resources;

import java.util.List;
import java.util.Optional;

import com.mhova.kindleScraper.core.PriceCheck;
import com.mhova.kindleScraper.db.PriceCheckDAO;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/pricechecks")
public class PriceChecksResource {
	private final PriceCheckDAO dao;

	public PriceChecksResource(PriceCheckDAO dao) {
		this.dao = dao;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<PriceCheck> getPriceChecks(@QueryParam("limit") Optional<Integer> maybeLimit) {
		final int limit = maybeLimit.orElse(20);
		// the price checks will come back from the DB in reverse chronological order
		return dao.getPriceChecks(limit).reversed();
	}
}

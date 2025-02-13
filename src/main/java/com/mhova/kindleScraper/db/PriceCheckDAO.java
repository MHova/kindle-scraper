package com.mhova.kindleScraper.db;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.mhova.kindleScraper.core.PriceCheck;

@RegisterConstructorMapper(PriceCheck.class)
public interface PriceCheckDAO {
	// row id is DB-generated and monotonically increasing
	@SqlUpdate("create table price_checks (id int GENERATED ALWAYS AS IDENTITY, timestamp timestamp with time zone, price numeric(10,2))")
	void createPriceChecksTable();

	@SqlUpdate("insert into price_checks (timestamp, price) values (:timestamp, :price)")
	void insert(@BindMethods PriceCheck priceCheck);

	@SqlQuery("select timestamp, price from price_checks order by id desc")
	PriceCheck findLatestPriceCheck();

	@SqlQuery("select timestamp, price from price_checks order by id desc limit :limit")
	List<PriceCheck> getPriceChecks(@Bind("limit") final int limit);
}
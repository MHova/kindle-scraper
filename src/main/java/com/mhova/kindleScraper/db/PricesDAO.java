package com.mhova.kindleScraper.db;

import java.time.Instant;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface PricesDAO {
	@SqlUpdate("create table prices (id int GENERATED ALWAYS AS IDENTITY, timestamp timestamp with time zone, price numeric(10,2))")
	void createPricesTable();

	@SqlUpdate("insert into prices (timestamp, price) values (:timestamp, :price)")
	void insert(@Bind("timestamp") Instant timestamp, @Bind("price") Double price);

	@SqlQuery("select price from prices order by id desc limit 1")
	Double findLatestPrice();
}
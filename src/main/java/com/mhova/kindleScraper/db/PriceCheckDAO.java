package com.mhova.kindleScraper.db;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface PriceCheckDAO {
	@SqlUpdate("create table price_checks (id int GENERATED ALWAYS AS IDENTITY, timestamp timestamp with time zone, price numeric(10,2))")
	void createPriceChecksTable();

	@SqlUpdate("insert into price_checks (timestamp, price) values (:timestamp, :price)")
	void insert(@BindMethods PriceCheck priceCheck);

	@RegisterConstructorMapper(PriceCheck.class)
	@SqlQuery("select timestamp, price from price_checks order by id desc")
	PriceCheck findLatestPriceCheck();
}
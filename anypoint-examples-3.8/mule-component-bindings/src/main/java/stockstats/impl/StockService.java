/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package stockstats.impl;

import java.util.Date;

import stockstats.StockStats;

/**
 * Defines an interface for classes that can fetch historical stock stats.
 */
public interface StockService {

	StockStats getHistoricalPrices(String stock, Date date) throws InvalidStockSymbol;
	
}

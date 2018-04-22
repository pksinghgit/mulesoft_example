/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package stockstats.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import stockstats.StockStats;
import stockstats.StockStatsResource;


public class StockStatsResourceImpl implements StockStatsResource {
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private StockService stockService;
	private TwitterService twitterService;
	private SentimentService sentimentService;

	@Override
	public StockStats search(String stock, String dateStr) {
		try {
			// Validation
			if (StringUtils.isBlank(stock)) {
				throw createWebApplicationException(Response.Status.BAD_REQUEST, "stock is required");
			}
			if (StringUtils.isBlank(dateStr)) {
				throw createWebApplicationException(Response.Status.BAD_REQUEST, "date is required");
			}
			if (dateStr.length() != DATE_FORMAT.length()) {
				throw createWebApplicationException(Response.Status.BAD_REQUEST, "date format must be YYYY-MM-DD");
			}
			
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			Date date = null;
			try {
				date = df.parse(dateStr);
			} catch (ParseException e) {
				throw createWebApplicationException(Response.Status.BAD_REQUEST, "date format must be YYYY-MM-DD");
			}
			
			// Fetch the stock price
			StockStats stockStats = null;
			try {
				stockStats = stockService.getHistoricalPrices(stock, date);
			} catch (InvalidStockSymbol e) {
				throw createWebApplicationException(Response.Status.BAD_REQUEST, "Invalid stock symbol " + stock);
			}
			
			// Fetch tweets
			List<Tweet> tweets = new ArrayList<Tweet>();
			int maxResults = 1000;
			int pageSize = 100;
			int page = 1;
			while (true) {
				List<Tweet> results = twitterService.search(stock, date, pageSize, page);
				tweets.addAll(results);
				if (results.size() == 0) {
					break;
				}
				if (tweets.size() > maxResults) {
					stockStats.getSocialStats().setMoreTweets(true);
					break;
				}
				page++;
			}
			
			// Make sure that we have no more that 1000 tweets
			while (tweets.size() > maxResults) {
				tweets.remove(maxResults);
			}
			
			// Classify the tweets
			sentimentService.classify(tweets);
			
			for (Tweet tweet: tweets) {
				if (stockStats != null) { // We won't have stats for weekend days
					stockStats.getSocialStats().addTweet(tweet);
				}
			}
			
			return stockStats;
		} catch (WebApplicationException e) {
			throw e;
		} catch (Throwable e) {
			e.printStackTrace();
			throw createWebApplicationException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	static WebApplicationException createWebApplicationException(Response.Status status, String message) {
		return new WebApplicationException(Response.status(status).entity(message).build());
	}

	public StockService getStockService() {
		return stockService;
	}

	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

	public TwitterService getTwitterService() {
		return twitterService;
	}

	public void setTwitterService(TwitterService twitterService) {
		this.twitterService = twitterService;
	}

	public SentimentService getSentimentService() {
		return sentimentService;
	}

	public void setSentimentService(SentimentService sentimentService) {
		this.sentimentService = sentimentService;
	}

}

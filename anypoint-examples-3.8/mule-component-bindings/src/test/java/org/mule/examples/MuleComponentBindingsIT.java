/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.NullPayload;

import stockstats.StockStats;
import stockstats.impl.SentimentService;
import stockstats.impl.StockService;
import stockstats.impl.StockStatsResourceImpl;
import stockstats.impl.Tweet;
import stockstats.impl.TwitterService;

public class MuleComponentBindingsIT extends FunctionalTestCase
{
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(MuleComponentBindingsIT.class); 
	
	private static final String BEAN_NAME = "stackStatsResource";
	private StockService stockService = Mockito.mock(StockService.class);
	private SentimentService sentimentService = Mockito.mock(SentimentService.class);
	private TwitterService twitterService = Mockito.mock(TwitterService.class);
	
	private final String STOCK = "AAPL";
	private final String DATE = "2012-10-26";	
	private StockStatsResourceImpl stockStatResource;
	
    private static String MESSAGE = "?stock=AAPL&date=2012-10-26";
    private static String REPLY;

    @Override
    protected String getConfigResources()
    {
        return "mule-component-bindings.xml";
    }

    @BeforeClass
	public static void init() {
    	final Properties props = new Properties();
    	try {
    	props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	} 
    	
    	try {
			REPLY = FileUtils.readFileToString(new File("./src/test/resources/reply.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}  
    	
    	System.setProperty("twitter.apiKey", props.getProperty("twitter.apiKey"));
    	System.setProperty("twitter.apiSecret", props.getProperty("twitter.apiSecret"));
    	System.setProperty("twitter.accessKey", props.getProperty("twitter.accessKey"));
    	System.setProperty("twitter.accessSecret", props.getProperty("twitter.accessSecret"));
    	System.setProperty("stockstats.stocklyticsApiKey", props.getProperty("stockstats.stocklyticsApiKey"));
    	System.setProperty("stockstats.sentiment140ApiKey", props.getProperty("stockstats.sentiment140ApiKey"));
    }
    
    @Before 
    public void setDependencies(){
    	stockStatResource = muleContext.getRegistry().get(BEAN_NAME);
    	stockStatResource.setStockService(stockService);
    	stockStatResource.setSentimentService(sentimentService);    	
    	stockStatResource.setTwitterService(twitterService);
    }
    
    @Test
    public void testBinding() throws Exception
    {    	    	   
    	StockStats stockStats = new StockStats();
    	stockStats.setClosingDate(DATE);
    	stockStats.getFinancialStats().setClose(1);
    	stockStats.getFinancialStats().setLow(2);
    	stockStats.getSocialStats().addTweet(new Tweet());
    	
    	Mockito.when(stockService.getHistoricalPrices(STOCK, new SimpleDateFormat("yyyy-MM-dd").parse(DATE))).thenReturn(stockStats);
   		
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        MuleMessage result = client.send("http://localhost:8180/api/stockStats" + MESSAGE, "", props);
        Thread.sleep(5000);
        assertNotNull(result);
        assertFalse(result.getPayload() instanceof NullPayload);
        assertTrue(result.getPayloadAsString().contains(REPLY));
    }

}

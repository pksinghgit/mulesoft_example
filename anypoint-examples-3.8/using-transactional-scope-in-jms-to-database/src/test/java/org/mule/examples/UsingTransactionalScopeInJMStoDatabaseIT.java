/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.module.client.MuleClient;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.tck.junit4.FunctionalTestCase;

public class UsingTransactionalScopeInJMStoDatabaseIT extends FunctionalTestCase
{	
	private static String MESSAGE;
	
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final String PATH_TO_SQL_SCRIPT = "src/main/resources/order.sql";
	private static final String DATABASE_NAME = "company"+System.currentTimeMillis();
	private static final MySQLDbCreator DBCREATOR = new MySQLDbCreator(DATABASE_NAME, PATH_TO_SQL_SCRIPT, PATH_TO_TEST_PROPERTIES);

	@Override
    protected String getConfigResources()
    {
        return "transactions.xml,testflows/test-flows.xml";
    }

    @Test
    public void sendJMStoTransactionScopeTest() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        client.send("jms://in", MESSAGE, null);
        SubflowInterceptingChainLifecycleWrapper subflow = getSubFlow("selectOrders");
        subflow.initialise();
        MuleEvent response = subflow.process(getTestEvent(null, MessageExchangePattern.REQUEST_RESPONSE));
        assertEquals("[{count(*)=0}]", response.getMessage().getPayloadAsString());
    }

    @BeforeClass
    public static void prepareTest() throws Exception {
    	try {
			MESSAGE = FileUtils.readFileToString(new File("./src/test/resources/message.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		DBCREATOR.setUpDatabase();
		System.setProperty("jdbc.url", DBCREATOR.getDatabaseUrlWithName());

    }
    
    @AfterClass
    public static void tearDown(){    	
    	DBCREATOR.tearDownDataBase();
    }
    
}

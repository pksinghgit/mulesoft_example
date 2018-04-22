/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples.integration;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mulesoft.module.batch.BatchTestHelper;



public class SalesforceDataSynchronizationUsingWatermarkingAndBatchProcessingIT extends AbstractTemplateTestCase {
	protected static final int TIMEOUT = 60;
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(SalesforceDataSynchronizationUsingWatermarkingAndBatchProcessingIT.class); 
	
	private BatchTestHelper helper;
		
	@BeforeClass
	public static void init() {
		final Properties props = new Properties();
    	try {
    	props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	}

		// Salesforce dropped TLS 1.0 as of 2017-09-23 and Java 7 uses TLS 1.0 by default
		System.setProperty("https.protocols", "TLSv1.1,TLSv1.2");    	
    	System.setProperty("sfdc.user", props.getProperty("sfdc.user"));
		System.setProperty("sfdc.password", props.getProperty("sfdc.password"));
		System.setProperty("sfdc.securityToken", props.getProperty("sfdc.securityToken"));	
				
	}

	@Before
	public void setUp() throws Exception {
		
		stopFlowSchedulers(POLL_FLOW_NAME);
		registerListeners();
		helper = new BatchTestHelper(muleContext);

	}

	@After
	public void tearDown() throws Exception {
		stopFlowSchedulers(POLL_FLOW_NAME);				
	}

	@Test
	public void testMainFlow() throws Exception {
		// Run poll and wait for it to run
		runSchedulersOnce(POLL_FLOW_NAME);
		waitForPollToRun();

		// Wait for the batch job executed by the poll flow to finish
		helper.awaitJobTermination(TIMEOUT_SEC * 1000, 500);
		helper.assertJobWasSuccessful();

	}
	
}

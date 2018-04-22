/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples.integration;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.examples.builders.SfdcObjectBuilder;
import org.mule.examples.db.MySQLDbCreator;
import org.mule.modules.salesforce.bulk.EnrichedUpsertResult;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mulesoft.module.batch.BatchTestHelper;


/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Mule Template that make calls to external systems.
 */
public class SalesforceToMySQLDBUsingBatchProcessingIT extends AbstractTemplateTestCase {
	protected static final int TIMEOUT = 60;
	private static final Logger log = LogManager.getLogger(SalesforceToMySQLDBUsingBatchProcessingIT.class);
	private static final String CONTACT_NAME = "Contact Test Name";
	
	private BatchTestHelper helper;
	private Map<String, Object> contact;
	private SubflowInterceptingChainLifecycleWrapper selectContactFromDBFlow;
	private SubflowInterceptingChainLifecycleWrapper deleteContactFromSalesforce;
	
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final String PATH_TO_SQL_SCRIPT = "src/main/resources/contact.sql";
	private static final String DATABASE_NAME = "company";
	private static final MySQLDbCreator DBCREATOR = new MySQLDbCreator(DATABASE_NAME, PATH_TO_SQL_SCRIPT, PATH_TO_TEST_PROPERTIES);

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
		System.setProperty("mysql.host", props.getProperty("database.host"));
		System.setProperty("mysql.user", props.getProperty("database.user"));
		System.setProperty("mysql.password", props.getProperty("database.password"));
		
		DBCREATOR.setUpDatabase();
		System.setProperty("database.url", DBCREATOR.getDatabaseUrlWithName());
	}

	@Before
	public void setUp() throws Exception {
		stopFlowSchedulers(POLL_FLOW_NAME);
		registerListeners();
		helper = new BatchTestHelper(muleContext);

		selectContactFromDBFlow = getSubFlow("selectContactFromDB");
		
		deleteContactFromSalesforce = getSubFlow("deleteContactFromSalesforce");
		deleteContactFromSalesforce.initialise();

		// prepare test data
		contact = createSalesforceContact();
		insertSalesforceContact(contact);
	}

	@After
	public void tearDown() throws Exception {
		stopFlowSchedulers(POLL_FLOW_NAME);
		// delete previously created Contact from DB by matching ID
		final Map<String, Object> contact = new HashMap<String, Object>();
		contact.put("LastName", this.contact.get("LastName"));
		contact.put("Id", this.contact.get("Id"));

		deleteContactFromDB(contact);
		deleteContactFromSalesforce(contact);
		
		DBCREATOR.tearDownDataBase();
	}

	@Test
	public void testMainFlow() throws Exception {
		// Run poll and wait for it to run
		runSchedulersOnce(POLL_FLOW_NAME);
		waitForPollToRun();

		// Wait for the batch job executed by the poll flow to finish
		helper.awaitJobTermination(TIMEOUT_SEC * 1000, 500);
		helper.assertJobWasSuccessful();

		// wait for 
		Thread.sleep(5000);
		// Execute selectContactFromDB subflow
		final MuleEvent event = selectContactFromDBFlow.process(getTestEvent(contact, MessageExchangePattern.REQUEST_RESPONSE));
		final List<Map<String, Object>> payload = (List<Map<String, Object>>) event.getMessage().getPayload();

		// print result
		for (Map<String, Object> acc : payload){
			log.info("selectContactFromDB response: " + acc);
		}

		// Contact previously created in Salesforce should be present in database
		Assert.assertEquals("The Contact should have been sync", 1, payload.size());
		Assert.assertEquals("The Contact name should match", contact.get("LastName"), payload.get(0).get("last_name"));		
	}

	@SuppressWarnings("unchecked")
	private void insertSalesforceContact(final Map<String, Object> Contact) throws Exception {
		final SubflowInterceptingChainLifecycleWrapper flow = getSubFlow("insertSalesforceContactSubFlow");
		flow.initialise();

		final MuleEvent event = flow.process(getTestEvent(Contact, MessageExchangePattern.REQUEST_RESPONSE));
		final List<EnrichedUpsertResult> result = (List<EnrichedUpsertResult>) event.getMessage().getPayload();

		// store Id into our Contact
		for (EnrichedUpsertResult item : result) {
			log.info("response from insertSalesforceContactSubFlow: " + item);
			Contact.put("Id", item.getId());
			Contact.put("LastModifiedDate", item.getPayload().getField("LastModifiedDate"));
		}
	}

	private void deleteContactFromSalesforce(final Map<String, Object> acc) throws Exception {

		List<Object> idList = new ArrayList<Object>();
		idList.add(acc.get("Id"));

		deleteContactFromSalesforce.process(getTestEvent(idList, MessageExchangePattern.REQUEST_RESPONSE));
	}

	private void deleteContactFromDB(final Map<String, Object> contact) throws Exception {
		final SubflowInterceptingChainLifecycleWrapper flow = getSubFlow("deleteContactFromDB");
		flow.initialise();

		final MuleEvent event = flow.process(getTestEvent(contact, MessageExchangePattern.REQUEST_RESPONSE));
		final Object result = event.getMessage().getPayload();
		log.info("deleteContactFromDB result: " + result);
	}

	private Map<String, Object> createSalesforceContact() {
		final SfdcObjectBuilder builder = new SfdcObjectBuilder();
		final Map<String, Object> contact = builder
				.with("LastName", CONTACT_NAME + System.currentTimeMillis())
				.with("Email", "Contact" + System.currentTimeMillis() + "@test.com")
				.build();
		log.info("SFDC contact: " + contact);
		return contact;
	}
}

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
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.module.client.MuleClient;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.util.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImportContactsToSfdcIT extends FunctionalTestCase
{

	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(ImportContactsToSfdcIT.class); 
	
	private final String EMAIL1 = "john.doe@texasComp.com";
	private final String EMAIL2 = "jane.doe@texasComp.com";
	private static List<String> contactIds = new ArrayList<String>();
	private static final String MAPPINGS_FOLDER_PATH = "./mappings";

	 
    @Override
    protected String getConfigResources()
    {
        return "contacts-to-SFDC.xml,testflows/test-flows.xml";
    }

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
        
	@Override
	protected Properties getStartUpProperties() {
		Properties properties = new Properties(super.getStartUpProperties());

		String pathToResource = MAPPINGS_FOLDER_PATH;
		File graphFile = new File(pathToResource);

		properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY,
				graphFile.getAbsolutePath());

		return properties;
	}
    
    @SuppressWarnings("unchecked")
	@Test
    public void testImport() throws Exception
    {
    	MuleClient client = new MuleClient(muleContext);
        String fileInputPath = "file://./src/main/resources/input";
        Map<String, Object> outboundProperties = new HashMap<String, Object>();
        outboundProperties.put("outputPattern", "contacts.csv");
        MuleMessage message = new DefaultMuleMessage(
        		IOUtils.getResourceAsString("contacts.csv", this.getClass()),
        		outboundProperties,
        		muleContext);
        client.dispatch(fileInputPath, message);        
        
        Thread.sleep(8000);
        SubflowInterceptingChainLifecycleWrapper select = getSubFlow("selectContactFromSalesforce");        										
        select.initialise();
        
        MuleEvent response = select.process(getTestEvent(EMAIL1, MessageExchangePattern.REQUEST_RESPONSE));        
        Map<String, Object> contact = (Map<String, Object>)response.getMessage().getPayload();        
        assertEquals("John", contact.get("FirstName"));
        contactIds.add(contact.get("Id").toString());
        response = select.process(getTestEvent(EMAIL2, MessageExchangePattern.REQUEST_RESPONSE));        
        contact = (Map<String, Object>)response.getMessage().getPayload();        
        assertEquals("Doe", contact.get("LastName"));
        contactIds.add(contact.get("Id").toString());
    }

    @After
    public void tearDown() {
    	SubflowInterceptingChainLifecycleWrapper delete = getSubFlow("deleteContactFromSalesforce");
        try {
			delete.initialise();
			delete.process(getTestEvent(contactIds, MessageExchangePattern.REQUEST_RESPONSE));
		} catch (Exception e) {
			e.printStackTrace();
		}
        		          
    }
}

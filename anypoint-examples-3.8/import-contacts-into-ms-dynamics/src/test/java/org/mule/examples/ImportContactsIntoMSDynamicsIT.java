/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class ImportContactsIntoMSDynamicsIT extends FunctionalTestCase
{

	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger LOG = LogManager.getLogger(ImportContactsIntoMSDynamicsIT.class); 
	private static final String MAPPINGS_FOLDER_PATH = "./mappings";
	
	private final String EMAIL1 = "john.doe@texasComp.com";
	private final String EMAIL2 = "jane.doe@texasComp.com";
	private List<String> contactIds = new ArrayList<String>();

	 
    @Override
    protected String getConfigResources()
    {
        return "import-contacts-into-ms-dynamics.xml,testflows/test-flows.xml";
    }

    @BeforeClass
	public static void init() {
    	final Properties props = new Properties();
    	try {
    	props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		LOG.error("Error occured while reading mule.test.properties", e);
    	}    	
    	System.setProperty("crm.username", props.getProperty("crm.username"));
		System.setProperty("crm.password", props.getProperty("crm.password"));
		System.setProperty("crm.service-url", props.getProperty("crm.service-url"));
		System.setProperty("crm.auth-type", props.getProperty("crm.auth-type"));
		
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
        MuleMessage testMessage = new DefaultMuleMessage(
        	IOUtils.getResourceAsString("contacts.csv", this.getClass()),
        	outboundProperties,
        	muleContext);
        client.dispatch(fileInputPath, testMessage);         
                 	
        Thread.sleep(40000);
        SubflowInterceptingChainLifecycleWrapper select = getSubFlow("selectContactFromDynamics");
        select.initialise();
        
        MuleEvent response = select.process(getTestEvent(EMAIL1, MessageExchangePattern.REQUEST_RESPONSE));
        assertTrue(((List<Map<String, Object>>)response.getMessage().getPayload()).size() > 0);
        Map<String, Object> contact = ((List<Map<String, Object>>)response.getMessage().getPayload()).get(0);
        assertEquals("John", contact.get("firstname"));
        contactIds.add(contact.get("contactid").toString());
        
        response = select.process(getTestEvent(EMAIL2, MessageExchangePattern.REQUEST_RESPONSE));
        assertTrue(((List<Map<String, Object>>)response.getMessage().getPayload()).size() > 0);
        contact = ((List<Map<String, Object>>)response.getMessage().getPayload()).get(0);
        assertEquals("Doe", contact.get("lastname"));
        contactIds.add(contact.get("contactid").toString());
    }

    @After
    public void tearDown() {
    	SubflowInterceptingChainLifecycleWrapper delete = getSubFlow("deleteContactFromDynamics");
        try {
			delete.initialise();
			for (String id : contactIds)
			delete.process(getTestEvent(id, MessageExchangePattern.REQUEST_RESPONSE));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

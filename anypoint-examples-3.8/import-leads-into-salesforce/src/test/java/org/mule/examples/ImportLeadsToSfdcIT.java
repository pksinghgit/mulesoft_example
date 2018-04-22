/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertEquals;

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
import org.mule.module.client.MuleClient;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.util.IOUtils;

public class ImportLeadsToSfdcIT extends FunctionalTestCase
{

	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(ImportLeadsToSfdcIT.class); 
	
	private static final String EMAIL1 = "faucibus@egetmetus.org";
	private static final String EMAIL2 = "sem.egestas@mollis.org";
	
	private static List<String> leadIds = new ArrayList<String>();
	 
    @Override
    protected String getConfigResources()
    {
        return "import-leads-into-salesforce.xml,testflows/test-flows.xml";
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
        
    
    @Test
    public void testImport() throws Exception
    {
    	
        MuleClient client = new MuleClient(muleContext);
        String fileInputPath = "file://./src/main/resources/input";
        Map<String, Object> outboundProperties = new HashMap<String, Object>();
        outboundProperties.put("outputPattern", "leads.csv");
        MuleMessage message = new DefaultMuleMessage(
        		IOUtils.getResourceAsString("leads.csv", this.getClass()),
        		outboundProperties,
        		muleContext);
        client.dispatch(fileInputPath, message);

        Thread.sleep(8000);
        SubflowInterceptingChainLifecycleWrapper select = getSubFlow("selectLeadFromSalesforce");        										
        select.initialise();
        
        MuleEvent response = select.process(getTestEvent(EMAIL1, MessageExchangePattern.REQUEST_RESPONSE));        
        Map<String, Object> lead = (Map<String, Object>)response.getMessage().getPayload();        
        assertEquals("Ishmael", lead.get("FirstName"));
        leadIds.add(lead.get("Id").toString());
        response = select.process(getTestEvent(EMAIL2, MessageExchangePattern.REQUEST_RESPONSE));        
        lead = (Map<String, Object>)response.getMessage().getPayload();        
        assertEquals("Burks", lead.get("LastName"));
        leadIds.add(lead.get("Id").toString());
                										
    }

    @After
    public void tearDown() {
    	SubflowInterceptingChainLifecycleWrapper delete = getSubFlow("deleteLeadFromSalesforce");
        try {
			delete.initialise();
			delete.process(getTestEvent(leadIds, MessageExchangePattern.REQUEST_RESPONSE));
		} catch (Exception e) {
			e.printStackTrace();
		}
        		          
    }
}

/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

public class GetCustomersListFromNetsuiteIT extends FunctionalTestCase
{

	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger LOGGER = LogManager.getLogger(GetCustomersListFromNetsuiteIT.class); 	
	private static final String LAST_NAME_VALUE = "a";
	private static final CharSequence TEST_HTML = "<h1>Netsuite Customer List</h1>";
	
	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	 
    @Override
    protected String getConfigResources()
    {
        return "get-customer-list-from-netsuite.xml";
    }

    @BeforeClass
	public static void init() {
    	final Properties props = new Properties();
    	try {
    	props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		LOGGER.error("Error occured while reading mule.test.properties", e);
    	}    	
    	System.setProperty("netsuite.email", props.getProperty("netsuite.email"));
		System.setProperty("netsuite.password", props.getProperty("netsuite.password"));
		System.setProperty("netsuite.account", props.getProperty("netsuite.account"));	
		System.setProperty("netsuite.roleId", props.getProperty("netsuite.roleId"));	
	}
          
    @Test
    public void testRetrieve() throws Exception
    {
    	MuleClient client = new MuleClient(muleContext);
    	Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        MuleMessage response = client.send("http://localhost:" + port.getNumber() + "/customers?lastName=" + LAST_NAME_VALUE, "", props, 60000);
        assertTrue(response.getPayloadAsString().contains(TEST_HTML));
    }
}
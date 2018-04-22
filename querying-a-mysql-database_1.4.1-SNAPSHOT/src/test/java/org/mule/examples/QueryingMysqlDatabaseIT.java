/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.transport.NullPayload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QueryingMysqlDatabaseIT extends FunctionalTestCase
{
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(QueryingMysqlDatabaseIT.class); 
	
	private static MySQLDbCreator DB = null;
	
	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	
    @Override
    protected String getConfigResources()
    {
        return "database-to-json.xml";
    }
    
    
    @BeforeClass
	public static void init() {
    	final Properties props = new Properties();
    	try {
    	props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	}
    	System.setProperty("jdbc.url", props.getProperty("jdbc.url"));
    	DB = new MySQLDbCreator("company", "./src/test/resources/mule.test.properties");
    	DB.setUpDatabase();
    }
    
    @Test
    public void httpGetToFlowUrlSentMessage() throws Exception
    {
    	Thread.sleep(3000);
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        MuleMessage result = client.send("http://0.0.0.0:" + port.getNumber() + "/?" + MESSAGE, "", props);
        assertNotNull(result);
        assertFalse(result.getPayload() instanceof NullPayload);
        assertEquals(REPLY, result.getPayloadAsString());
        
    }
        
    @AfterClass
    public static void tearDown(){
    	DB.tearDownDataBase();
    }
    
    private String MESSAGE = "lastname=Puckett";
    private String REPLY = "[{\"first_name\":\"Chava\"},{\"first_name\":\"Quentin\"}]";
}

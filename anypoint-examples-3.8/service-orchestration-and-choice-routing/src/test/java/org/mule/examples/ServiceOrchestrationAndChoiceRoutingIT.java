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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.NullPayload;

public class ServiceOrchestrationAndChoiceRoutingIT extends FunctionalTestCase
{

	private static final String MAPPINGS_FOLDER_PATH = "./mappings";
	private static String MESSAGE;
	private static String REPLY;
	
    @Override
    protected String getConfigResources()
    {
        return "fulfillment.xml,mule-config.xml";
    }
            
    @BeforeClass
    public static void clean(){
    	deleteDB();
    	try {
			MESSAGE = FileUtils.readFileToString(new File("./src/test/resources/message.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
			REPLY = FileUtils.readFileToString(new File("./src/test/resources/reply.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		} 

    }
    
    private static void deleteDB(){
    	try {
    		FileUtils.deleteDirectory(new File("./muleEmbeddedDB"));
			FileUtils.deleteDirectory(new File("./ObjectStore"));			
		} catch (IOException e) {
			e.printStackTrace();
		} 
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
    
    @Test 
    public void orderTest() throws Exception
    {    	
    	MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "POST");
        MuleMessage res = client.send("http://localhost:1080/orders", MESSAGE, props);
        assertEquals("200", res.getInboundProperty("http.status"));     
        assertEquals(REPLY, res.getPayloadAsString());        
    }
    
    @Test
    public void populateDatabase() throws Exception
    {    	
    	MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        MuleMessage result = client.send("http://localhost:8091/populate", "", props);
        assertNotNull(result);
        assertFalse(result.getPayload() instanceof NullPayload);
        assertTrue(result.getPayloadAsString().contains("db populated"));        
    }
          
    
}

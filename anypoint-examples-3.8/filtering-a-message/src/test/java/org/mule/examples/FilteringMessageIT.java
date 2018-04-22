/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

public class FilteringMessageIT extends FunctionalTestCase
{

    private static String MESSAGE;
    private static String MESSAGE1;

    @Rule
	public DynamicPort port = new DynamicPort("http.port");
    
    @Override
    protected String getConfigResources()
    {
        return "filtering.xml";
    }

    @Before
    public void init(){
    	try {
			MESSAGE = FileUtils.readFileToString(new File("./src/test/resources/message.json"));
			MESSAGE1 = FileUtils.readFileToString(new File("./src/test/resources/message1.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}    	
    }
    
    @Test
    public void testFiltering() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "POST");
        
        MuleMessage result = client.send("http://localhost:" + port.getNumber() + "/", MESSAGE, props);
        assertNotNull(result);
        assertEquals("the discount was granted.", result.getPayloadAsString());
        
        result = client.send("http://localhost:" + port.getNumber() + "/", MESSAGE1, props);
        assertNotNull(result);
        assertTrue(result.getPayloadAsString().isEmpty());       
    }

}

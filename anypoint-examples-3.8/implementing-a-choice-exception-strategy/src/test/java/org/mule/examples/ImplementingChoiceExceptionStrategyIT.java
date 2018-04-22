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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.transport.NullPayload;

public class ImplementingChoiceExceptionStrategyIT extends FunctionalTestCase
{
	private static String MESSAGE;
	private static String MESSAGE_WRONG;
	private static final String REPLY = "Input data validation passed.";
	private static final String[] REPLY_WRONG = {"Missing input data:", "item price per unit=1", "membership=free", "item name=aa", "item units=10"};
    
	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	
	@Override
    protected String getConfigResources()
    {
        return "choice-error-handling.xml";
    }

	@Before
    public void init(){
    	try {
			MESSAGE = FileUtils.readFileToString(new File("./src/test/resources/message.json"));
			MESSAGE_WRONG = FileUtils.readFileToString(new File("./src/test/resources/message_wrong.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}    	
    }
	
    @Test
    public void inputValidationTest() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "POST");
        MuleMessage result = client.send("http://localhost:" + port.getNumber() + "/", MESSAGE, props);
        assertNotNull(result);
        assertFalse(result.getPayload() instanceof NullPayload);
        assertEquals(REPLY, result.getPayloadAsString());
        
        result = client.send("http://localhost:" + port.getNumber() + "/", MESSAGE_WRONG, props);
        assertNotNull(result);
        assertEquals("400", result.getInboundProperty("http.status"));
        for (String item : REPLY_WRONG) {
        	assertTrue(StringUtils.contains(result.getPayloadAsString(), item));
        }
        
    }

}

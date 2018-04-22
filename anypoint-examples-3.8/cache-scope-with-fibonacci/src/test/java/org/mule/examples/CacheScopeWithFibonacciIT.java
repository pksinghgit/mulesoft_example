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

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.transport.NullPayload;

public class CacheScopeWithFibonacciIT extends FunctionalTestCase
{

    private static String MESSAGE = "10";
    private static String MESSAGE1 = "5";

    @Rule
	public DynamicPort port = new DynamicPort("http.port");
    
    @Override
    protected String getConfigResources()
    {
        return "cache-scope.xml";
    }

    @Test
    public void httpGetToFlowUrlSentMessage() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        
        MuleMessage result = client.send("http://localhost:" + port.getNumber() + "/fibonacci?n=" + MESSAGE, "", props);
        assertNotNull(result);
        assertFalse(result.getPayload() instanceof NullPayload);
        assertEquals("Fibonacci(10) = 55\nCOST: 11", result.getPayloadAsString());
        
        result = client.send("http://localhost:" + port.getNumber() + "/fibonacci?n=" + MESSAGE1, "", props);
        assertNotNull(result);
        assertFalse(result.getPayload() instanceof NullPayload);
        assertEquals("Fibonacci(5) = 5\nCOST: 0", result.getPayloadAsString());
    }

}

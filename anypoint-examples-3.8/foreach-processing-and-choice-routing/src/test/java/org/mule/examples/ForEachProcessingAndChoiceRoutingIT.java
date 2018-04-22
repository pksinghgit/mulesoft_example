/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.NullPayload;

public class ForEachProcessingAndChoiceRoutingIT extends FunctionalTestCase
{
	
    @Override
    protected String getConfigResources()
    {
        return "loanbroker-simple.xml";
    }

    @Test
    public void httpGetToFlowUrlSentMessage() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        MuleMessage result = client.send("http://0.0.0.0:11081/?" + MESSAGE, "", props);
        assertNotNull(result);
        assertFalse(result.getPayload() instanceof NullPayload);
        assertTrue(result.getPayloadAsString().contains("Bank"));
        assertTrue(result.getPayloadAsString().contains("rate"));
                
    }
        
    private String MESSAGE = "name=Muley&amount=20000&term=48&ssn=1234";
    
}

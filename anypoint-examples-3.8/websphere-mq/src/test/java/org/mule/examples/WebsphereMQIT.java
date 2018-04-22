/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;


import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class WebsphereMQIT extends FunctionalMunitSuite
{
	private String MESSAGE = "test";
		
    @Override
    protected String getConfigResources()
    {
        return "websphere-mq.xml";
    }
        
    @BeforeClass
    public static void init(){
    	System.setProperty("wmq.host", "");
    	System.setProperty("wmq.password", "");
    	System.setProperty("wmq.port", "1414");
    	System.setProperty("wmq.channel", "");
    	System.setProperty("wmq.username", "");
    	System.setProperty("wmq.queue", "");    	    	
    }
    
    @Before
    public void setUp(){    	
    	whenMessageProcessor("outbound-endpoint").ofNamespace("wmq").thenReturn(new DefaultMuleMessage(MESSAGE, muleContext));
    	whenMessageProcessor("outbound-endpoint").ofNamespace("ajax").thenReturn(new DefaultMuleMessage(MESSAGE, muleContext));
    }
    
    @Test
    public void testWMQ() throws Exception
    {    	
    	MuleEvent res = runFlow("Input", testEvent(""));    	
    	Assert.assertEquals(MESSAGE, res.getMessage().getPayloadAsString());
    	res = runFlow("MessageProcessor", testEvent(""));
    	Assert.assertEquals(MESSAGE, res.getMessage().getPayloadAsString());
    	res = runFlow("Output", testEvent(""));
    	Assert.assertEquals(MESSAGE, res.getMessage().getPayloadAsString());  
    }

}

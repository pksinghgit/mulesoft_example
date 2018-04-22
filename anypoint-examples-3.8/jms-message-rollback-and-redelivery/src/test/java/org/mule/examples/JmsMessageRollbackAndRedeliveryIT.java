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
import org.apache.activemq.broker.BrokerService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

public class JmsMessageRollbackAndRedeliveryIT extends FunctionalTestCase {
	private static final String MESSAGE = "Message123";
	private static BrokerService broker = null;
	
	@Override
	protected String getConfigResources() {
		return "jms-redelivery.xml";
	}
	
	@BeforeClass
	public static void init() throws Exception {
		broker = new BrokerService();
		broker.setPersistent(false);
		broker.addConnector("tcp://localhost:61616");
		broker.start();
	}
	
	@Test
	public void redeliverJMSMessageTest() throws Exception {
		MuleClient client = new MuleClient(muleContext);
		client.dispatch("jms://queue:in", MESSAGE, null);
		MuleMessage response = client.request("jms://topic:topic1", 30000);
		assertNotNull(response);
		assertEquals(response.getPayloadAsString(), MESSAGE);
		broker.stop();
	}
}
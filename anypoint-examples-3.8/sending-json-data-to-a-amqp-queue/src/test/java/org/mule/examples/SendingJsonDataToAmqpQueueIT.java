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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;

import java.util.concurrent.TimeoutException;

public class SendingJsonDataToAmqpQueueIT extends FunctionalTestCase {

	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(SendingJsonDataToAmqpQueueIT.class); 
	
	private static final String EXCHNAGE_NAME = "sales_exchange";
	private static final String QUEUE_NAME = "sales_queue";
	private static String HOST;
	
	private static final String REPLY_OK = "test";
	 
	private Channel channel;
	private Connection connection;
	protected String messagebody;
	
	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	
	@Override
    protected String getConfigResources()
    {
        return "json-to-rabbitmq.xml";
    }

    @Test	    
    public void testFlow() throws Exception
    {	    		    
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "POST");
        client.dispatch("http://localhost:" + port.getNumber() + "/", REPLY_OK, props);
        recieveMessage();
        Thread.sleep(5000);
        assertNotNull(messagebody);
        assertEquals(REPLY_OK, messagebody);	        	        
    }
    
    @Before
    public void setUp() throws IOException, TimeoutException{
    	connectToChannel();
    	channel.exchangeDeclare(EXCHNAGE_NAME, "topic");
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHNAGE_NAME, "#");	        
    }
    
    @After
    public void tearDown() throws IOException, TimeoutException{
    	channel.exchangeDelete(EXCHNAGE_NAME);
    	channel.queueDelete(QUEUE_NAME);
    	channel.close();
    	connection.close();
    }
    
    private void connectToChannel() throws IOException, TimeoutException{
    	ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        connection = factory.newConnection();
        channel = connection.createChannel();	        
    }
    
    private void recieveMessage() throws IOException{	    	
        channel.basicConsume(QUEUE_NAME, false, "myConsumerTag",
        	     new DefaultConsumer(channel) {
        	         @Override
        	         public void handleDelivery(String consumerTag,
        	        		 com.rabbitmq.client.Envelope envelope,
        	                                    AMQP.BasicProperties properties,
        	                                    byte[] body)
        	             throws IOException
        	         {
        	        	 messagebody = new String(body);
        	         }
        	     });	        	       
    }
    
    @BeforeClass
    public static void init(){
    	final Properties props = new Properties();
    	try {
    		props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	} 
    	
    	try {
			FileUtils.deleteDirectory(new File("./work/derbystore"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	HOST = props.getProperty("amqp.host");
    	
    	BrokerOptions configuration = new BrokerOptions();
    	configuration.setInitialConfigurationLocation("./src/test/resources/config.json");
		Broker broker = new Broker();
		try {
			broker.startup(configuration);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
    }
}

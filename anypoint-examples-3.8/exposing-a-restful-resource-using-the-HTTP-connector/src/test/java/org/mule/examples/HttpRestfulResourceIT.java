/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class HttpRestfulResourceIT extends FunctionalTestCase
{

    private static final String REPLY = "{ \"status\": \"success\", \"statusDescription\": \"person created successfully\"}";
	private static String PERSON_JSON;
    
	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	
    @BeforeClass
    public static void init(){
    	try {
			PERSON_JSON = IOUtils.toString(new FileInputStream("./src/test/resources/person.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    protected String getConfigResources()
    {
        return "http-restful-resource.xml";
    }

    @Before
    public void createPerson() throws Exception
    {
    	MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "POST");
        client.send("http://0.0.0.0:" + port.getNumber() + "/person", PERSON_JSON, props);		       
    }

    @Test
	public void getPerson() throws Exception {
    	MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        MuleMessage result = client.send("http://0.0.0.0:" + port.getNumber() + "/person/1", "", props);
        assertEquals(result.getPayloadAsString(), PERSON_JSON);       
	}
    
    @Test
	public void postPerson() throws Exception {
    	MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "POST");
        MuleMessage result = client.send("http://0.0.0.0:" + port.getNumber() + "/person", PERSON_JSON, props);
        assertEquals(result.getPayloadAsString(), REPLY);       
	}
    
    @Test
	public void getPersons() throws Exception {
    	MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        MuleMessage result = client.send("http://0.0.0.0:" + port.getNumber() + "/person", "", props);
        assertTrue(result.getPayloadAsString().contains(PERSON_JSON));       
	}
}


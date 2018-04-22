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
import org.mule.transport.NullPayload;

public class XMLWithSoapIT extends FunctionalTestCase
{
	private String MESSAGE;
	private String REPLY1 = "<runningTotal>500</runningTotal>";
	private String REPLY2 = "<status>ADMITTED</status>";
	
	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	
	 @Override
	 protected String getConfigResources()
	 {
	  return "Hospital_Admissions_SOA.xml,mocks.xml";
	 }

	 @Before
	 public void setUp(){
		 try {
			 MESSAGE = FileUtils.readFileToString(new File("./src/test/resources/message.xml"));			 
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	 
	 @Test
	 public void testXMLwithSoap() throws Exception
	 {
		  MuleClient client = new MuleClient(muleContext);
		  Map<String, Object> props = new HashMap<String, Object>();
		  props.put("http.method", "POST");
		  MuleMessage result = client.send("http://localhost:" + port.getNumber() + "/AdmissionService", MESSAGE, props);
		  assertNotNull(result);
		  assertFalse(result.getPayload() instanceof NullPayload);	  
		  assertTrue(result.getPayloadAsString().contains(REPLY1));
		  assertTrue(result.getPayloadAsString().contains(REPLY2));
	 }

}

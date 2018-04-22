/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

public class SalesforceIdRetrievalIT extends FunctionalTestCase {

	private static final String TEST_DIR = "./src/test/resources";
	private static List<String> REPLY = new ArrayList<String>();
	private static final String OPTION_REGEX = "<option\\svalue=\\\".*\\\">.*<\\/option>";
	
	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	
	@BeforeClass
	public static void init() throws IOException{
		REPLY = FileUtils.readLines(new File(TEST_DIR + "/reply.txt"));
		
		Properties props = new Properties();
		props.load(new FileInputStream(TEST_DIR + "/mule.test.properties"));

		// Salesforce dropped TLS 1.0 as of 2017-09-23 and Java 7 uses TLS 1.0 by default
		System.setProperty("https.protocols", "TLSv1.1,TLSv1.2");
		System.setProperty("sfdc.username", props.getProperty("sfdc.username"));
    	System.setProperty("sfdc.securityToken", props.getProperty("sfdc.securityToken"));
    	System.setProperty("sfdc.password", props.getProperty("sfdc.password"));

	}
	
	@Override
    protected String getConfigResources()
    {
        return "salesforce-id-retrieval.xml";
    }
	
	@Test
	public void testDisplayData() throws Exception {
		MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        MuleMessage result = client.send("http://localhost:" + port.getNumber(), "", props);

        Pattern p = Pattern.compile(OPTION_REGEX);
        Matcher m = p.matcher(result.getPayloadAsString());
        assertTrue("Payload should contain at least one option tag", m.find());
	}
	
	@Test
	public void testGetData() throws Exception {
		MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "POST");
        Map<String, String> params = new HashMap<String, String>();
        params.put("object", "user");
        params.put("field", "email");
        params.put("searchKey", "name");
        params.put("searchValue", "mule");
        MuleMessage result = client.send("http://localhost:" + port.getNumber() + "/", params, props);
        for (String line : REPLY){
        	assertTrue(result.getPayloadAsString().contains(line));
        }
	}
}

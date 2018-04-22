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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.transport.NullPayload;

public class MuleExpressionLanguageBasicsIT extends FunctionalTestCase
{
	private static MuleClient CLIENT;
	private static final String MESSAGE = "username=Mule&age=1";
	private static String MESSAGE_XML;
	private static String MESSAGE_JSON;
	private static final String REPLY = "Hello Mule";
	private static final String REPLY_2 = "No username provided";
	private static final String DIR = "Path_of_your_choice";
	private static final String REPLY_1 = "Mule, 1, false";
	
	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	
    @Override
    protected String getConfigResources()
    {
        return "greeting.xml";
    }

    @Before
    public void init() throws MuleException {
    	try {
    		CLIENT = new MuleClient(muleContext);
			MESSAGE_JSON = FileUtils.readFileToString(new File("./src/test/resources/message.json"));
			MESSAGE_XML = FileUtils.readFileToString(new File("./src/test/resources/message.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}    	
    }
    
    @Test
    public void melFlow1Test() throws Exception
    {
        testResponse(CLIENT, "greet1?" + MESSAGE, REPLY, StringUtils.EMPTY);                
    }
    
    @Test
    public void melFlow2Test() throws Exception
    {  
    	testResponse(CLIENT, "greet2?" + MESSAGE, REPLY, StringUtils.EMPTY);   
        testResponse(CLIENT, "greet2", REPLY_2, StringUtils.EMPTY);            
    }
    
    @Test
    public void melFlow3Test() throws Exception
    {
    	testFileAndClean(CLIENT, "greet3?" + MESSAGE, REPLY_1, StringUtils.EMPTY);          
    }
    
    @Test
    public void melFlow4Test() throws Exception
    {    
    	testFileAndClean(CLIENT, "greet4?" + MESSAGE, "Mule,1,false", StringUtils.EMPTY);
    }
    
    @Test
    public void melFlow5Test() throws Exception
    {
        testFileAndClean(CLIENT, "greet5", REPLY_1, MESSAGE_XML);
    }
    
    @Test
    public void melFlow6Test() throws Exception
    {
        testFileAndClean(CLIENT, "greet6", REPLY_1, MESSAGE_JSON);
    }
    
    private void testFileAndClean(MuleClient client, String param, String reply, String body) throws Exception{
    	testResponse(client, param, REPLY, body);
        assertTrue(new File(DIR).list().length == 1);
        assertEquals(StringUtils.trim(reply), StringUtils.trim(FileUtils.readFileToString(new File(DIR).listFiles()[0])));
        FileUtils.deleteDirectory(new File(DIR));
    }
    
    private void testResponse(MuleClient client, String param, String reply, String body) throws Exception{
    	Map<String, Object> props = new HashMap<String, Object>();
    	props.put("http.method", "POST");
        MuleMessage result = client.send("http://localhost:" + port.getNumber() + "/" + param, body, props);
        assertNotNull(result);
        assertFalse(result.getPayload() instanceof NullPayload);
        assertEquals(reply, result.getPayloadAsString());
    }
    
    @AfterClass
    public static void tearDown() throws IOException {
    	FileUtils.deleteDirectory(new File(DIR));
    }
    
    private static final String MAPPINGS_FOLDER_PATH = "./mappings";

	@Override
	protected Properties getStartUpProperties() {
		Properties properties = new Properties(super.getStartUpProperties());

		String pathToResource = MAPPINGS_FOLDER_PATH;
		File graphFile = new File(pathToResource);

		properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY,
				graphFile.getAbsolutePath());

		return properties;
	}

}

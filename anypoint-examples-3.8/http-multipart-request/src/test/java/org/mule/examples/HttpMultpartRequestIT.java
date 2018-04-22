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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

public class HttpMultpartRequestIT extends FunctionalTestCase
{

    private static final String TMP_FILE = "/tmp/file";
	private static final String TEST_FILE_PATH = "./src/test/resources/test.txt";
	private static String HTML;

	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	
    @Override
    protected String getConfigResources()
    {
        return "http-multipart-request.xml";
    }

    @BeforeClass
    public static void init(){
    	try {
			HTML = IOUtils.toString(new FileInputStream("./src/main/resources/uploadFile.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }       
        
    @Test
	public void testPageRender() throws Exception {        	
    	MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        MuleMessage result = client.send("http://localhost:" + port.getNumber() + "/uploadFile", "", props);
        assertTrue(result.getPayloadAsString().contains(HTML));        
	} 
    
    @Test
	public void testFileUpload() throws Exception {        	
    	
    	AttachmentRequest request = new AttachmentRequest();
    	File testFile = new File(TEST_FILE_PATH);
    	request.executeMultiPartRequest("http://localhost:" + port.getNumber() + "/uploadFile", testFile, "file", "");    	
    	assertTrue(new File(TMP_FILE).exists());
	}
    
    @After
    public void tearDown(){
    	new File(TMP_FILE).delete();
    }
    
}


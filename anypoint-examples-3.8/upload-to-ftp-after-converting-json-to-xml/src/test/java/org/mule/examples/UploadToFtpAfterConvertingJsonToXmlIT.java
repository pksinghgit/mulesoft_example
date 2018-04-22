/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

public class UploadToFtpAfterConvertingJsonToXmlIT extends FunctionalTestCase
{
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(UploadToFtpAfterConvertingJsonToXmlIT.class); 
	private static final String EMPTY_STRING = "";
	
	private static String FTP_PASSWORD;
	private static String FTP_USER;
	private static String FTP_HOST;
	private static String FTP_PORT;
	private static String FTP_HOME;
	private static String FTP_PATH;
	
	private static String MESSAGE = "";
	
	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	
    @Override
    protected String getConfigResources()
    {
        return "upload-to-ftp.xml";
    }

    @BeforeClass
    public static void init() throws IOException{
    	final Properties props = new Properties();
    	try {
    	props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	}
    	try {
			MESSAGE = FileUtils.readFileToString(new File("./src/test/resources/message.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}    	    
    	FTP_USER = props.getProperty("ftp.user");
    	FTP_PASSWORD = props.getProperty("ftp.password");
    	FTP_PORT = props.getProperty("ftp.port");
    	FTP_HOME = props.getProperty("ftp.home");		
    	FTP_PATH = props.getProperty("ftp.path");
    	FTP_HOST = props.getProperty("ftp.host");
    	
    	System.setProperty("ftp.user", FTP_USER);
    	System.setProperty("ftp.password", FTP_PASSWORD);
    	System.setProperty("ftp.port", FTP_PORT);
    	System.setProperty("ftp.path", FTP_PATH);
    	System.setProperty("ftp.host", FTP_HOST);
		File dataDirectory = new File(FTP_HOME);
		if (dataDirectory.exists()) {
		    FileUtils.deleteDirectory(dataDirectory);
		}
		dataDirectory.mkdirs();    			
    }    
    

    @Test
    public void testDataWeave() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "POST");
        props.put("Content-Type", "application/json");
        try {
        	MuleMessage result = client.send("http://0.0.0.0:" + port.getNumber() + "/", MESSAGE, props);
            //after upload, the payload should contain empty string
            assertEquals(EMPTY_STRING, result.getPayloadAsString());  
        } catch (Exception ex){
        	fail("The FTP upload flow was not successful.");
        }      
    }


	@Override
	protected Properties getStartUpProperties() {
		return new Properties(super.getStartUpProperties());
	}	    
}

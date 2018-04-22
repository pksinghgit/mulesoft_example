/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.util.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.mail.imap.IMAPFolder;

public class SendingACsvFileThroughEmailUsingSmtpIT extends FunctionalTestCase
{
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(SendingACsvFileThroughEmailUsingSmtpIT.class); 
	
	private static String USER;
	private static String PASSWORD;
	private static String IMAP_HOST;
	private static String USER_ENC;	
	private static String TEST_EMAIL_BODY;
			
    	
    @Override
    protected String getConfigResources()
    {
        return "csv-to-smtp.xml";
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
	
    @BeforeClass
	public static void init() {
    	final Properties props = new Properties();
    	try {
    		props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	} 
    	
    	try {
			TEST_EMAIL_BODY = FileUtils.readFileToString(new File("./src/main/resources/email.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	PASSWORD = props.getProperty("smtp.password");    	
    	USER = props.getProperty("smtp.user");
    	USER_ENC = props.getProperty("smtp.user_enc");
    	IMAP_HOST = props.getProperty("imap.host");
    	System.setProperty("smtp.host", props.getProperty("smtp.host"));
    	System.setProperty("smtp.user", USER_ENC);
    	System.setProperty("smtp.password", PASSWORD);
    	System.setProperty("mail.to", USER);
    	System.setProperty("mail.from", USER);
    	System.setProperty("mail.subject", "Mule flow completed!");
    }

    @After
    public void tearDown() throws MessagingException, IOException{
    	getSentEmail();
    }
    
    @Test
    public void sendCSVFile() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        String fileInputPath = "file://./src/main/resources/input";
        Map<String, Object> outboundProperties = new HashMap<String, Object>();
        outboundProperties.put("outputPattern", "input.csv");
        MuleMessage testMessage = new DefaultMuleMessage(
        		IOUtils.getResourceAsString("input.csv", this.getClass()),
        		outboundProperties,
        		muleContext);
        client.dispatch(fileInputPath, testMessage);                
        
        Thread.sleep(12000);
        String message = null;
        try{
        	message = getSentEmail();        	        	        
        }
        catch (Exception e){
        	e.printStackTrace();
        }
        assertEquals(TEST_EMAIL_BODY, message.trim());
    }

    private String getSentEmail() throws MessagingException, IOException{
    	IMAPFolder folder = null;
        Store store = null;
        try 
        {
          Properties props = System.getProperties();
          props.setProperty("mail.store.protocol", "imaps");

          Session session = Session.getDefaultInstance(props, null);

          store = session.getStore("imaps");
          store.connect(IMAP_HOST, USER, PASSWORD);

          folder = (IMAPFolder) store.getFolder("inbox");


          if(!folder.isOpen())
        	  folder.open(Folder.READ_WRITE);
          Message[] messages = folder.getMessages();
          if (messages.length > 0){
        	  messages[messages.length - 1].setFlag(Flags.Flag.DELETED, true);
        	  return messages[messages.length - 1].getContent().toString();
          }
          else
        	  return null;
        }
        finally 
        {
          if (folder != null && folder.isOpen()) { folder.close(true); }
          if (store != null) { store.close(); }
        }
    }
}

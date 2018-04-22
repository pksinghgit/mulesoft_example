/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.config.MuleProperties;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

import com.sun.mail.imap.IMAPFolder;

public class QueryingDbAndAttachingResultsToAnEmailIT extends FunctionalTestCase
{
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(QueryingDbAndAttachingResultsToAnEmailIT.class); 
	
	private static String MESSAGE;
	private static final String REPLY_1 = "Chava,Puckett,F,1985-09-02,2008-10-12";
	private static final String REPLY_2 = "Mona,Sosa,M,1950-09-26,2007-11-27";
	private static String USER ;
	private static String PASSWORD;
	private static String HOST;
	
	private static final String MAPPINGS_FOLDER_PATH = "./mappings";
	private static MySQLDbCreator DB = null;

	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	
	@Override
    protected String getConfigResources()
    {
        return "attachments.xml";
    }

    @Test
    public void attachmentsTest() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "POST");
        
        client.dispatch("http://localhost:" + port.getNumber() + "/", MESSAGE, props);
        Thread.sleep(15000);
        String attachmentContent = getAttachmentContent();
        assertNotNull(attachmentContent);
        assertTrue(attachmentContent.contains(REPLY_1));
        assertTrue(attachmentContent.contains(REPLY_2));
                       
    }

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
    public static void prepareTest() throws Exception {
    	final Properties props = new Properties();
    	try {
    		props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	}
    	
    	try {
			MESSAGE = FileUtils.readFileToString(new File("./src/test/resources/message.xml"));			
		} catch (IOException e) {
			e.printStackTrace();
		} 
    	
    	System.setProperty("smtp.host", props.getProperty("smtp.host"));
       	System.setProperty("smtp.password", props.getProperty("smtp.password"));
       	System.setProperty("smtp.user", props.getProperty("smtp.user_enc"));
       	System.setProperty("smtp.port", props.getProperty("smtp.port"));
    	System.setProperty("mail.to", props.getProperty("smtp.user"));
    	System.setProperty("mail.from", props.getProperty("smtp.user"));
    	System.setProperty("jdbc.url", props.getProperty("jdbc.url"));
    	System.setProperty("mail.subject", "Mule flow completed!");
    	HOST = props.getProperty("smtp.host");
    	USER = props.getProperty("smtp.user");
    	PASSWORD = props.getProperty("smtp.password");
    	DB = new MySQLDbCreator("company", "./src/test/resources/mule.test.properties");
    	DB.setUpDatabase();

    }
    
    @AfterClass
    public static void tearDown(){
    	DB.tearDownDataBase();
    }
    
    private static String getAttachmentContent() throws IOException, javax.mail.MessagingException{
    	IMAPFolder folder = null;
        Store store = null;
        try 
        {
          Properties props = System.getProperties();
          props.setProperty("mail.store.protocol", "imaps");

          Session session = Session.getDefaultInstance(props, null);

          store = session.getStore("imaps");
          store.connect(HOST, USER, PASSWORD);

          folder = (IMAPFolder) store.getFolder("inbox");


          if(!folder.isOpen())
        	  folder.open(Folder.READ_WRITE);
          Message[] messages = folder.getMessages();
          if (messages.length > 0){
        	  messages[messages.length - 1].setFlag(Flags.Flag.DELETED, true);
        	  return getAttachment(messages[messages.length - 1]);
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
    
    private static String getAttachment(Message message) throws IOException, MessagingException{    	
	    Multipart multipart = (Multipart) message.getContent();

	    for (int i = 0; i < multipart.getCount(); i++) {
		    BodyPart bodyPart = multipart.getBodyPart(i);
		    if(!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
		               !StringUtils.isNotBlank(bodyPart.getFileName())) {
		      return bodyPart.getContent().toString();
		    } 
	    }
    	return null;    
    }

}

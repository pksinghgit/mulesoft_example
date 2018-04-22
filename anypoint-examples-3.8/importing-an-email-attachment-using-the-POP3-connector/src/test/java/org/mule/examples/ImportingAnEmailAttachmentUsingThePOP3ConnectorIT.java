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
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.api.config.MuleProperties;
import org.mule.tck.junit4.FunctionalTestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.mail.imap.IMAPFolder;

public class ImportingAnEmailAttachmentUsingThePOP3ConnectorIT extends FunctionalTestCase
{
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(ImportingAnEmailAttachmentUsingThePOP3ConnectorIT.class); 
	private static final String MAPPINGS_FOLDER_PATH = "./mappings";	
	private static String TEST_DIR_PATH = "./src/test/resources/output";
		
	private static String USER;
	private static String PASSWORD;
	private static String SMTP_HOST;
	private static String SMTP_PORT;
	private static String IMAP_HOST;
	private static String POP3_USER;
	private static String POP3_PASSWORD;
	
    @Override
    protected String getConfigResources()
    {
        return "pop-to-xml.xml";
    }

    @BeforeClass
    public static void prepareTest() throws Exception {
    	final Properties props = new Properties();
    	try {
    	props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	} 
    	
    	PASSWORD = props.getProperty("smtp.password");
    	SMTP_HOST = props.getProperty("smtp.host");
    	SMTP_PORT = props.getProperty("smtp.port");
    	USER = props.getProperty("smtp.user");
    	IMAP_HOST = props.getProperty("imap.host");
    	POP3_USER = props.getProperty("pop3.user");
    	POP3_PASSWORD = props.getProperty("pop3.password");
		
      	System.setProperty("pop3.host", props.getProperty("pop3.host"));
       	System.setProperty("pop3.password", props.getProperty("pop3.password"));
       	System.setProperty("pop3.user", props.getProperty("pop3.user_enc"));
    	       	
    	File testDir = new File(TEST_DIR_PATH);
    	if (!testDir.exists()){
    		testDir.mkdirs();
    	}
    	
		String to = POP3_USER;
        String from = USER;        
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", SMTP_HOST);
        properties.setProperty("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.starttls.enable", "true");
        
        // Log into Account A (sender)
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USER, PASSWORD);
                    }
                });

        // Send email with attachment to Account B (receiver)
        try{
           MimeMessage message = new MimeMessage(session);
           message.setFrom(new InternetAddress(from));
           message.addRecipient(Message.RecipientType.TO,
                                    new InternetAddress(to));
           message.setSubject("This is the POP Subject Line!");
           MimeBodyPart attachmentPart = new MimeBodyPart();
		   FileDataSource fileDataSource = new FileDataSource("./src/main/resources/input.csv") {
		     @Override
		     public String getContentType() {
		         return "application/octet-stream";
		     }
		   };
		   
		   attachmentPart.setDataHandler(new DataHandler(fileDataSource));
		   attachmentPart.setFileName("report");
	       Multipart multipart = new MimeMultipart();
	       multipart.addBodyPart(attachmentPart);
	 
	       message.setContent(multipart);
           
           Transport.send(message);
           log.info("Sent message successfully....");
        }catch (MessagingException mex) {
           mex.printStackTrace();
        }        
    }
    
    @Test
    public void recievePOPMessage() throws Exception
    {       
       Thread.sleep(10000);	
       assertTrue(new File(TEST_DIR_PATH).listFiles().length > 0);       
    }
   
    @AfterClass
    public static void tearDown(){
    	File testDir = new File(TEST_DIR_PATH);
    	
    	for (File file : testDir.listFiles()){
    		file.delete();
    	}    
    	try {
			deleteSentEmail();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
	private static IMAPFolder openFolder(Store store) throws MessagingException{
	    	
	        Properties props = System.getProperties();
	        props.setProperty("mail.store.protocol", "imaps");
	
	        Session session = Session.getDefaultInstance(props, null);
	
	        store = session.getStore("imaps");
	        store.connect(IMAP_HOST, POP3_USER, POP3_PASSWORD);
	        
	        IMAPFolder folder = (IMAPFolder) store.getFolder("inbox");
	        if(!folder.isOpen())
	      	  folder.open(Folder.READ_WRITE);
	        return folder;
    }

        
    private static String deleteSentEmail() throws MessagingException, IOException{
    	Store store = null;
        IMAPFolder folder = null;
        try 
        {
          folder = openFolder(store);	

          if(!folder.isOpen())
        	  folder.open(Folder.READ_WRITE);
          Message[] messages = folder.getMessages();
          messages[messages.length - 1].setFlag(Flags.Flag.DELETED, true);
          return messages[messages.length - 1].getContent().toString();
        }
        finally 
        {
          if (folder != null && folder.isOpen()) { folder.close(true); }
          if (store != null) { store.close(); }
        }
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

}

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
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
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


public class ImportingAnEmailAttachmentUsingTheIMAPConnectorIT extends FunctionalTestCase
{
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(ImportingAnEmailAttachmentUsingTheIMAPConnectorIT.class);
	
	private static String MAPPINGS_FOLDER_PATH = "./mappings";
	private static String USER;
	private static String PASSWORD;
	private static String SMTP_HOST;
	private static String SMTP_PORT;
	private static String IMAP_USER;
	private final static String TEST_DIR_PATH = "./src/test/resources/output";
	
	@Override
	protected String getConfigResources() {
		return "imap-to-xml.xml";
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
		IMAP_USER = props.getProperty("imap.user");
		System.setProperty("imap.host", props.getProperty("imap.host"));
		System.setProperty("imap.password", props.getProperty("imap.password"));
		System.setProperty("imap.user", props.getProperty("imap.user_enc"));
		File testDir = new File(TEST_DIR_PATH);
		if (!testDir.exists()){
			testDir.mkdirs();
		}
		
		String to = IMAP_USER;
		String from = USER;
		Properties properties = System.getProperties();
		properties.put("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.host", SMTP_HOST);
		properties.setProperty("mail.smtp.port", SMTP_PORT);
		properties.put("mail.smtp.starttls.enable", "true");
		Session session = Session.getInstance(properties,
			new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USER, PASSWORD);
			}
		});
		
		try{
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO,
			new InternetAddress(to));
			message.setSubject("This is the IMAP Subject Line!");
			MimeBodyPart attachmentPart = new MimeBodyPart();
			FileDataSource fileDataSource = new FileDataSource("./src/main/resources/input.csv") {
				@Override
				public String getContentType() {
					return "application/octet-stream";
				}
			};
			attachmentPart.setDataHandler(new DataHandler(fileDataSource));
			attachmentPart.setFileName("report.csv");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(attachmentPart);
			message.setContent(multipart);
			Transport.send(message);
			log.info("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
	
	@Test
	public void recieveImapMessage() throws Exception {
		Thread.sleep(10000);
		assertTrue(new File(TEST_DIR_PATH).listFiles().length == 1);
	}
	
	@AfterClass
	public static void tearDown() {
		File testDir = new File(TEST_DIR_PATH);
		for (File file : testDir.listFiles()){
			file.delete();
		}
	}
	
	@Override
	protected Properties getStartUpProperties() {
		Properties properties = new Properties(super.getStartUpProperties());
		String pathToResource = MAPPINGS_FOLDER_PATH;
		File graphFile = new File(pathToResource);
		properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY, graphFile.getAbsolutePath());
		return properties;
	}
}
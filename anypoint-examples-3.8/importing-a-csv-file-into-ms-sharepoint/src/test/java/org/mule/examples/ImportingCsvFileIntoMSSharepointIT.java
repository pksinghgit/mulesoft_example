/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.api.config.MuleProperties;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.module.client.MuleClient;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.util.IOUtils;

public class ImportingCsvFileIntoMSSharepointIT extends FunctionalTestCase {

	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(ImportingCsvFileIntoMSSharepointIT.class);

	private static final String MAPPINGS_FOLDER_PATH = "./mappings";
	private static final String TEST_FOLDER = "" + System.currentTimeMillis();
	private static final String FILE_NAME = "contacts.csv";

	private SubflowInterceptingChainLifecycleWrapper getCsvFromSharepointFlow;
	private SubflowInterceptingChainLifecycleWrapper deleteFolderInSharepointFlow;

	@Override
	protected String getConfigResources() {
		return "importing-a-csv-file-into-ms-sharepoint.xml,testflows/test-flows.xml";
	}

	@BeforeClass
	public static void init() {
		final Properties props = new Properties();
		try {
			props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
		} catch (Exception e) {
			log.error("Error occured while reading mule.test.properties", e);
		}
		System.setProperty("Sharepoint.Username", props.getProperty("Sharepoint.Username"));
		System.setProperty("Sharepoint.Password", props.getProperty("Sharepoint.Password"));
		System.setProperty("Sharepoint.SiteUrl", props.getProperty("Sharepoint.SiteUrl"));

		System.setProperty("csvImportFolder", TEST_FOLDER);
	}

	@Before
	public void setUp() throws Exception {
		getAndInitializeFlows();
	}

	private void getAndInitializeFlows() throws InitialisationException {
		// Flow for obtaining file in MS Sharepoint
		getCsvFromSharepointFlow = getSubFlow("getCsvFromSharepointFlow");
		getCsvFromSharepointFlow.initialise();

		// Flow for deleting folder in MS Sharepoint
		deleteFolderInSharepointFlow = getSubFlow("deleteFolderInSharepointFlow");
		deleteFolderInSharepointFlow.initialise();
		;
	}

	@Override
	protected Properties getStartUpProperties() {
		Properties properties = new Properties(super.getStartUpProperties());

		String pathToResource = MAPPINGS_FOLDER_PATH;
		File graphFile = new File(pathToResource);

		properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY, graphFile.getAbsolutePath());

		return properties;
	}

	@Test
	public void testImport() throws Exception {
		MuleClient client = new MuleClient(muleContext);
		String fileInputPath = "file://./src/main/resources/input";
		String payload = IOUtils.getResourceAsString("contacts.csv", this.getClass());
		client.dispatch(fileInputPath, payload, null);
		Thread.sleep(20000);

		MuleEvent response = getCsvFromSharepointFlow.process(getTestEvent("", MessageExchangePattern.REQUEST_RESPONSE));
		String sharepointFileContent = (String) response.getMessage().getPayload();

		String localFileContent = IOUtils.getResourceAsString(FILE_NAME, this.getClass());

		Assert.assertEquals("Local file and Sharepoint file should be same", localFileContent, sharepointFileContent);
	}

	@After
	public void tearDown() {
		try {
			deleteFolderInSharepointFlow.process(getTestEvent("", MessageExchangePattern.REQUEST_RESPONSE));
		} catch (Exception e) {
			log.error("Error occured while cleaning test data", e);
		}

	}
}

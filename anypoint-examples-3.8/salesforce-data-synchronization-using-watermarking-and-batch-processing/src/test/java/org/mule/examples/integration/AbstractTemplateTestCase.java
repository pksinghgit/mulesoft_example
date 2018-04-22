/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples.integration;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.junit.Rule;
import org.mule.api.config.MuleProperties;
import org.mule.context.notification.NotificationException;
import org.mule.examples.test.utils.ListenerProbe;
import org.mule.examples.test.utils.PipelineSynchronizeListener;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.tck.probe.PollingProber;
import org.mule.tck.probe.Prober;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class AbstractTemplateTestCase extends FunctionalTestCase {
	private static final String MAPPINGS_FOLDER_PATH = "./mappings";
	private static final String MULE_DEPLOY_PROPERTIES_PATH = "./src/main/app/mule-deploy.properties";
	
	private static final Logger log = LogManager.getLogger(AbstractTemplateTestCase.class);

	protected static final int TIMEOUT_SEC = 120;
	protected static final String POLL_FLOW_NAME = "triggerFlow";

	protected final Prober pollProber = new PollingProber(60000, 1000l);
	protected final PipelineSynchronizeListener pipelineListener = new PipelineSynchronizeListener(POLL_FLOW_NAME);

	protected SubflowInterceptingChainLifecycleWrapper retrieveContactFromBFlow;
	protected SubflowInterceptingChainLifecycleWrapper retrieveAccountFlowFromB;

	@Rule
	public DynamicPort port = new DynamicPort("http.port");

	@Override
	protected String getConfigResources() {
		String resources = "";
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(MULE_DEPLOY_PROPERTIES_PATH));
			resources = props.getProperty("config.resources");
		} catch (Exception e) {
			throw new IllegalStateException(
					"Could not find mule-deploy.properties file on classpath. Please add any of those files or override the getConfigResources() method to provide the resources by your own.");
		}

		return resources;
	}

	@Override
	protected Properties getStartUpProperties() {
		Properties properties = new Properties(super.getStartUpProperties());

		String pathToResource = MAPPINGS_FOLDER_PATH;
		File graphFile = new File(pathToResource);

		properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY, graphFile.getAbsolutePath());

		return properties;
	}

	protected void registerListeners() throws NotificationException {
		muleContext.registerListener(pipelineListener);
	}

	protected void waitForPollToRun() {
		log.info("Waiting for poll to run once...");
		pollProber.check(new ListenerProbe(pipelineListener));
		log.info("Poll flow done");
	}
	

}
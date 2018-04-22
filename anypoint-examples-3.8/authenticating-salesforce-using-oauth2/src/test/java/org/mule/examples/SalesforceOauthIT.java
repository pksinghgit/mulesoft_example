/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SalesforceOauthIT extends FunctionalTestCase
{
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(SalesforceOauthIT.class); 
	private WebDriver driver;
	
	private static String USERNAME;
	private static String PASSWORD;
	
    private static String MESSAGE = "Salesforce query returned";

	@Rule
	public DynamicPort port = new DynamicPort("http.port");
    @Override
    protected String getConfigResources()
    {
        return "salesforce-oauth.xml";
    }

    @BeforeClass
    public static void setUp(){
    	final Properties props = new Properties();
    	try {
    	props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	}    	
		// Salesforce dropped TLS 1.0 as of 2017-09-23 and Java 7 uses TLS 1.0 by default 
		System.setProperty("https.protocols", "TLSv1.1,TLSv1.2");
		System.setProperty("sfdc.key", props.getProperty("sfdc.key"));
    	System.setProperty("sfdc.secret", props.getProperty("sfdc.secret"));
    	USERNAME = props.getProperty("sfdc.user");
    	PASSWORD = props.getProperty("sfdc.password");
    }
    
    @Test
    public void testAuthentication() throws Exception
    {
    	loginToSalesforce();
    	// wait till the login process is complete
    	Thread.sleep(5000);                
        driver.get("http://localhost:" + port.getNumber() + "/");
        assertTrue(driver.getPageSource().contains(MESSAGE));
        driver.quit();
    }
    
    private void loginToSalesforce() {
    	ArrayList<String> cliArgsCap = new ArrayList<String>();
		DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
		capabilities.setCapability("phantomjs.binary.path", System.getProperty("phantomjs.binary"));
		capabilities.setJavascriptEnabled(true);
		cliArgsCap.add("--ssl-protocol=any");
    	capabilities.setCapability(
    	    PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);               
		driver = new PhantomJSDriver(capabilities);
		driver.get("https://login.salesforce.com/");
		WebDriverWait waitForScreen = new WebDriverWait(driver, 30);
		waitForScreen.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));

        WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        
        username.sendKeys(USERNAME);
        password.sendKeys(PASSWORD);
        username.submit();
    }

}

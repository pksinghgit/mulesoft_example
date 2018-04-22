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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HttpAuthorizationCodeWebIT extends FunctionalTestCase
{
	private static final String HTTP_ENDPOINT = "http://localhost:8081/web";
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(HttpAuthorizationCodeWebIT.class);
	private static final CharSequence REPLY = "The user successfully authorized"; 
	
	private static String USERNAME;
	private static String PASSWORD;
	private WebDriver driver;
	
	@Override
    protected String getConfigResources()
    {
        return "http-authorization-code-web.xml";
    }
    
    @BeforeClass
    public static void prepareTest() throws Exception {
    	final Properties props = new Properties();
    	try {
    	props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	}    	
    	USERNAME = props.getProperty("box.user");
    	PASSWORD = props.getProperty("box.password");
    	
    	System.setProperty("truststore.password", props.getProperty("truststore.password"));
    	System.setProperty("keystore.keyPassword", props.getProperty("keystore.keyPassword"));
    	System.setProperty("keystore.password", props.getProperty("keystore.password"));
    	System.setProperty("box.id", props.getProperty("box.id"));
    	System.setProperty("box.secret", props.getProperty("box.secret"));
    	
    }

    @Before
    public void setUp() {				
    	ArrayList<String> cliArgsCap = new ArrayList<String>();
    	DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();    
    	capabilities.setCapability("phantomjs.binary.path", System.getProperty("phantomjs.binary"));		
    	cliArgsCap.add("--ssl-protocol=any");
    	cliArgsCap.add("--ignore-ssl-errors=true");    	
    	capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);        
    	driver = new PhantomJSDriver(capabilities);
    }
    
    @Test
    public void oauthTest() throws Exception
    {
    	driver.get(HTTP_ENDPOINT);
    	WebDriverWait waitForScreen = new WebDriverWait(driver, 10);
		waitForScreen.until(ExpectedConditions.visibilityOfElementLocated(By.id("login")));

    	WebElement username = driver.findElement(By.id("login"));
        WebElement password = driver.findElement(By.id("password"));
        
        username.sendKeys(USERNAME);
        password.sendKeys(PASSWORD);
        username.submit();
    	    	
    	waitForScreen.until(ExpectedConditions.visibilityOfElementLocated(By.id("consent_accept_button")));
    	WebElement grantButton = driver.findElement(By.id("consent_accept_button"));
        grantButton.submit();
        driver.get("http://localhost:8081/web/loginDone");
        assertTrue(driver.getPageSource().contains(REPLY));               
    }

    @After
    public void tearDown(){
    	if (driver != null)
    		driver.quit();
    }       
}


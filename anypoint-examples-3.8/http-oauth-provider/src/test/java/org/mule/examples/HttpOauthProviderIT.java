/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
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

public class HttpOauthProviderIT extends FunctionalTestCase
{
	private static final Logger log = LogManager.getLogger(HttpOauthProviderIT.class);
	private static final Object REPLY_NAME = "payroll";
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	
	private static String USERNAME = "mule";
	private static String PASSWORD = "mule";
	private WebDriver driver;
	
	@Rule
	public DynamicPort httpProviderPort = new DynamicPort("http.provider.port");
	
	@Rule
	public DynamicPort httpListenerPort = new DynamicPort("http.listener.port");
	@Override
    protected String getConfigResources()
    {
        return "http-oauth-provider.xml";
    }
    
    @Before
    public void setUp() {				
    	ArrayList<String> cliArgsCap = new ArrayList<String>();
    	DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();    
    	capabilities.setCapability("phantomjs.binary.path", System.getProperty("phantomjs.binary"));		
    	cliArgsCap.add("--ssl-protocol=any");
    	capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);        
    	driver = new PhantomJSDriver(capabilities);
    }


    @BeforeClass
    public static void prepareTest() throws Exception {
    	final Properties props = new Properties();
    	try {
    	props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	}
    }    
    
    @Test
    public void oauthTest() throws Exception
    {
    	driver.get("http://localhost:"+ httpProviderPort.getNumber() + 
    			"/authorize?response_type=code&client_id=myclientid&scope=READ_RESOURCE&redirect_uri=http://localhost:" + 
    			httpListenerPort.getNumber() + "/redirect");
    	WebDriverWait waitForScreen = new WebDriverWait(driver, 10);
		waitForScreen.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));

    	WebElement username = driver.findElement(By.id("username"));
        WebElement password = driver.findElement(By.id("password"));
        
        username.sendKeys(USERNAME);
        password.sendKeys(PASSWORD);
        username.submit();
    	    	
        JSONObject jso = new JSONObject(driver.getPageSource().substring(driver.getPageSource().indexOf("{"), driver.getPageSource().indexOf("}") + 1));        
                
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        props.put("Authorization", "Bearer " + jso.get("access_token"));
        MuleMessage result = client.send("http://localhost:" + httpListenerPort.getNumber() + "/resources", "", props);
        jso = new JSONObject(result.getPayloadAsString());
        assertEquals(REPLY_NAME, jso.get("name"));
        assertEquals("http://localhost:" + httpListenerPort.getNumber() + "/resources/payroll", jso.get("uri"));
    }
        
    @After
    public void tearDown(){
    	if (driver != null)
    		driver.quit();
    }       
}


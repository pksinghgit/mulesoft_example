/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import org.mule.transport.NullPayload;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProxyingRestApiIT extends FunctionalTestCase
{
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(ProxyingRestApiIT.class); 
	
	private static String USERNAME;
	private static String PASSWORD;
	private static String CLIENT_ID;
	private static String CLIENT_SECRET;
	private WebDriver driver;
	
	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	
	@Override
    protected String getConfigResources()
    {
        return "proxying-a-rest-api.xml";
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
    	CLIENT_ID = props.getProperty("box.id");
    	CLIENT_SECRET = props.getProperty("box.secret");
    	System.setProperty("keystore.key", props.getProperty("keystore.key"));
    	System.setProperty("keystore.password", props.getProperty("keystore.password"));
    	
    }

    @Before
    public void setUp() {				
    	ArrayList<String> cliArgsCap = new ArrayList<String>();
    	DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
    	capabilities.setCapability("phantomjs.binary.path", System.getProperty("phantomjs.binary"));
		capabilities.setJavascriptEnabled(true);
		
    	cliArgsCap.add("--ssl-protocol=any");
    	capabilities.setCapability(
    	    PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        driver = new PhantomJSDriver(capabilities);        
    }
    
    @Test
    public void restAuthenticationTest() throws Exception
    {
    	driver.get("http://localhost:" + port.getNumber() + "/oauth2/authorize?response_type=code&client_id=" + CLIENT_ID);    	
    	WebDriverWait waitForScreen = new WebDriverWait(driver, 30);
		waitForScreen.until(ExpectedConditions.visibilityOfElementLocated(By.id("login")));

    	WebElement username = driver.findElement(By.id("login"));
        WebElement password = driver.findElement(By.id("password"));
        
        username.sendKeys(USERNAME);
        password.sendKeys(PASSWORD);
        username.submit();
    	    	
    	waitForScreen.until(ExpectedConditions.visibilityOfElementLocated(By.id("consent_accept_button")));
    	WebElement grantButton = driver.findElement(By.id("consent_accept_button"));
        grantButton.submit();
        
        String code = driver.getCurrentUrl().substring(driver.getCurrentUrl().indexOf("code=") + 5);        
        String token = getAccessToken(code);
        
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        props.put("Authorization", "Bearer " + token);
        MuleMessage result = client.send("http://localhost:" + port.getNumber() + "/2.0/folders/0", "", props);
        
        assertNotNull(result);
        assertFalse(result.getPayload() instanceof NullPayload);
        
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner((InputStream)result.getPayload());
        while (scanner.hasNext()){
        		sb.append(scanner.next());
    	}
        scanner.close();
        JSONObject jo = new JSONObject(sb.toString());
        assertEquals("folder", jo.get("type"));
        JSONObject jo1 = new JSONObject(jo.get("owned_by").toString());
        assertEquals(USERNAME, jo1.get("login"));
                
    }

    @After
    public void tearDown(){
    	if (driver != null)
    		driver.quit();
    }
    
    private String getAccessToken(String code) throws ClientProtocolException, IOException{

		DefaultHttpClient httpClient = new DefaultHttpClient(); 
		HttpPost httpPost = new HttpPost("http://localhost:" + port.getNumber() + "/oauth2/token"); 
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		
		formparams.add(new BasicNameValuePair("grant_type", "authorization_code")); 
		formparams.add(new BasicNameValuePair("code", code)); 
		formparams.add(new BasicNameValuePair("client_id", CLIENT_ID)); 
		formparams.add(new BasicNameValuePair("client_secret", CLIENT_SECRET)); 
		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "utf-8");
		httpPost.setEntity(entity); 
		
		HttpResponse response = httpClient.execute(httpPost);
		Scanner scanner = new Scanner(response.getEntity().getContent());
		String token = null;
		while (scanner.hasNext()){
			String line = scanner.next();
			if (line.contains("access_token")){
				token = line.split(":")[1].replace("\"", "").trim().substring(0, 
						line.split(":")[1].replace("\"", "").trim().indexOf(",expires_in"));
			}
		}
		scanner.close();
    	return token;
    }
}

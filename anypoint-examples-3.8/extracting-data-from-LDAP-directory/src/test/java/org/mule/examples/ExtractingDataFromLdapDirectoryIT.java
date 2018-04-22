/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.transport.NullPayload;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFException;
import com.unboundid.ldif.LDIFReader;

public class ExtractingDataFromLdapDirectoryIT extends FunctionalTestCase
{
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(ExtractingDataFromLdapDirectoryIT.class); 
	
	private static String LDAP_ADMIN;
	private static String LDAP_PASSWORD;	
	private static String LDAP_ROOT;
	private static String LDAP_PORT;
	
	@Rule
	public DynamicPort port = new DynamicPort("http.port");
	
	@Override
    protected String getConfigResources()
    {
        return "ldap.xml";
    }

	@BeforeClass
	public static void init() {
    	final Properties props = new Properties();
    	try {
    	props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	}    	
    	LDAP_ADMIN = props.getProperty("ldap.admin");
    	LDAP_PASSWORD = props.getProperty("ldap.password");	
    	LDAP_PORT = props.getProperty("ldap.port");
    	LDAP_ROOT = props.getProperty("ldap.root");
    	System.setProperty("ldap.dn", LDAP_ADMIN);
    	System.setProperty("ldap.password", LDAP_PASSWORD);
    	System.setProperty("ldap.port", LDAP_PORT);
	}
    
    @Before
    public void setUp() throws LDAPException, LDIFException, IOException{
    	InMemoryDirectoryServerConfig config =
    		     new InMemoryDirectoryServerConfig(LDAP_ROOT);
    	
    	config.addAdditionalBindCredentials(LDAP_ADMIN, LDAP_PASSWORD);
    	config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("LDAP", Integer.parseInt(LDAP_PORT)));
    	InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
    	InputStream resourceAsStream = getClass().getResourceAsStream("/ldap-add.ldif");
    	LDIFReader reader = new LDIFReader(resourceAsStream);
    	ds.importFromLDIF(true, reader);    	
    	ds.startListening("LDAP");
    	
    }
       
    @Override
	protected Properties getStartUpProperties() {
		Properties properties = new Properties(super.getStartUpProperties());

		properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY,
					"./src/main/app/");

		return properties;
	}

    
    @Test
    public void connectAndExtract() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);        
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "GET");
        MuleMessage result = client.send("http://localhost:" + port.getNumber(), "", props);
        assertNotNull(result);
        assertFalse(result.getPayload() instanceof NullPayload);
        assertTrue(result.getPayloadAsString().contains("dn: cn=mmc,ou=people"));
        assertTrue(result.getPayloadAsString().contains("dn: cn=testuser1,ou=people"));
        assertTrue(result.getPayloadAsString().contains("dn: cn=admin,ou=people"));
                              
    }  
           
}

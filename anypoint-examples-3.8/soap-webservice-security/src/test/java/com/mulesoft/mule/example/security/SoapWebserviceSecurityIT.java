/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package com.mulesoft.mule.example.security;

import org.mule.tck.junit4.FunctionalTestCase;

import java.util.Map;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public final class SoapWebserviceSecurityIT extends FunctionalTestCase
{
    
    @Override
    protected String getConfigResources()
    {
        return "mule-config.xml";
    }

    @Test
    public void testUnsecure() throws Exception
    {
        Greeter service = SecureClient.createService("http://localhost:63081/services/unsecure?wsdl", null);
        String reply = service.greet("Mule");
        assertEquals("Hello Mule", reply);
    }

    @Test
    public void testUsernameToken() throws Exception
    {
        Greeter service = SecureClient.createService("http://localhost:63081/services/username?wsdl",
            SecureClient.getUsernameTokenProps("UsernameToken Timestamp", "wssecurity.properties"));
        String reply = service.greet("Mule");
        assertEquals("Hello Mule", reply);
    }

    @Test
    public void testUsernameTokenWrongPasswordError() throws Exception
    {
        Map<String, Object> wss4jProps = SecureClient.getUsernameTokenProps("UsernameToken Timestamp", "wssecurity.properties");
        wss4jProps.put("passwordCallbackClass", WrongPasswordCallback.class.getName());

        Greeter service = SecureClient.createService("http://localhost:63081/services/username?wsdl", wss4jProps);
        try
        {
            service.greet("Mule");
            fail("Request should have thrown a security exception");
        }
        catch (SOAPFaultException e)
        {
            assertTrue(e.getMessage().contains("security token"));
        }
    }

    @Test
    public void testUsernameTokenSigned() throws Exception
    {
        Greeter service = SecureClient.createService("http://localhost:63081/services/signed?wsdl",
            SecureClient.getUsernameTokenProps("UsernameToken Signature Timestamp", "wssecurity.properties"));
        String reply = service.greet("Mule");
        assertEquals("Hello Mule", reply);
    }

    @Test
    public void testUsernameTokenUnsignedError() throws Exception
    {
        Greeter service = SecureClient.createService("http://localhost:63081/services/signed?wsdl",
            SecureClient.getUsernameTokenProps("UsernameToken Timestamp", "wssecurity.properties"));
        try
        {
            service.greet("Mule");
            fail("Request should have thrown a security exception");
        }
        catch (SOAPFaultException e)
        {
            assertTrue(e.getMessage().contains("<wsse:Security> header"));
        }
    }

    @Test
    public void testUsernameTokenEncrypted() throws Exception
    {
        Greeter service = SecureClient.createService("http://localhost:63081/services/encrypted?wsdl",
            SecureClient.getUsernameTokenProps("UsernameToken Timestamp Encrypt",
                "wssecurity.properties"));
        String reply = service.greet("Mule");
        assertEquals("Hello Mule", reply);
    }

    @Test
    public void testSamlToken() throws Exception
    {
        Greeter service = SecureClient.createService("http://localhost:63081/services/saml?wsdl",
            SecureClient.getSamlTokenProps("SAMLTokenUnsigned Timestamp", "saml.properties"));
        String reply = service.greet("Mule");
        assertEquals("Hello Mule", reply);
    }
    
  @Test
  public void testSamlTokenWrongIssuer() throws Exception
  {
      Greeter service = SecureClient.createService("http://localhost:63081/services/saml?wsdl",
          SecureClient.getSamlTokenProps("SAMLTokenUnsigned Timestamp", "wrong-saml.properties"));
      try
      {
          service.greet("Mule");
          fail("Request should have thrown a security exception");
      }
      catch (SOAPFaultException e)
      {
          assertTrue(e.getMessage().contains("<wsse:Security> header"));
      }
  }

    @Test
    public void testSignedSamlToken() throws Exception
    {
        Greeter service = SecureClient.createService("http://localhost:63081/services/signedsaml?wsdl",
                                                     SecureClient.getSignedSamlTokenProps("SAMLTokenSigned", "saml.properties"));
        String reply = service.greet("Mule");
        assertEquals("Hello Mule", reply);
    }

}

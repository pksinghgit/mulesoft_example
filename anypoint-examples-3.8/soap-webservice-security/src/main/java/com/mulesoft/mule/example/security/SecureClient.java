/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package com.mulesoft.mule.example.security;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.handler.WSHandlerConstants;

public class SecureClient
{
    public static void main(String[] args) throws Exception
    {
        startClient();
    }
    
    protected static void startClient() throws IOException
    {
        int response = 0;
        while (response != 'q')
        {
            System.out.println("\n1. No security\n2. UsernameToken\n3. UsernameToken with wrong password (error)\n4. UsernameToken Signed\n5. UsernameToken missing signature (error)\n6. UsernameToken Encrypted\n7. SAMLToken\n8. SAMLToken wrong issuer (error)\n9. SAMLToken signed (error)\nq. Quit");

            response = readCharacter();

            switch (response)
            {
                case '1' :
                {
                    Greeter service = createService("http://localhost:63081/services/unsecure?wsdl", null);
                    System.out.println(service.greet("Mule"));
                    break;
                }
                case '2' :
                {
                    Greeter service = createService("http://localhost:63081/services/username?wsdl",
                        getUsernameTokenProps("UsernameToken Timestamp", "wssecurity.properties"));
                    System.out.println(service.greet("Mule"));
                    break;
                }
                case '3' :
                {
                    Map<String, Object> wss4jProps = SecureClient.getUsernameTokenProps("UsernameToken Timestamp", "wssecurity.properties");
                    wss4jProps.put("passwordCallbackClass", WrongPasswordCallback.class.getName());
                    Greeter service = SecureClient.createService("http://localhost:63081/services/username?wsdl", wss4jProps);
                    try
                    {
                        service.greet("Mule");
                    }
                    catch (SOAPFaultException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case '4' :
                {
                    Greeter service = createService("http://localhost:63081/services/signed?wsdl",
                        getUsernameTokenProps("UsernameToken Signature Timestamp", "wssecurity.properties"));
                    System.out.println(service.greet("Mule"));
                    break;
                }
                case '5' :
                {
                    Greeter service = createService("http://localhost:63081/services/signed?wsdl",
                        getUsernameTokenProps("UsernameToken Timestamp", "wssecurity.properties"));
                    try
                    {
                        service.greet("Mule");
                    }
                    catch (SOAPFaultException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case '6' :
                {
                    Greeter service = createService("http://localhost:63081/services/encrypted?wsdl",
                        getUsernameTokenProps("UsernameToken Timestamp Encrypt", "wssecurity.properties"));
                    System.out.println(service.greet("Mule"));
                    break;
                }
                case '7' :
                {
                    Greeter service = createService("http://localhost:63081/services/saml?wsdl",
                        getSamlTokenProps("SAMLTokenUnsigned Timestamp", "saml.properties"));
                    System.out.println(service.greet("Mule"));
                    break;
                }
                case '8' :
                {
                    Greeter service = createService("http://localhost:63081/services/saml?wsdl",
                        getSamlTokenProps("SAMLTokenUnsigned Timestamp", "wrong-saml.properties"));
                    try
                    {
                        service.greet("Mule");
                    }
                    catch (SOAPFaultException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case '9' :
                {
                    Greeter service = createService("http://localhost:63081/services/signedsaml?wsdl",
                                                    getSignedSamlTokenProps("SAMLTokenSigned", "saml.properties"));
                    System.out.println(service.greet("Mule"));
                    break;
                }
                case 'q' :
                {
                    System.out.println("Bye");
                    System.exit(0);
                }                
            }
        }
    }

    protected static Map<String, Object> getUsernameTokenProps(String action, String propertiesFile)
    {
        Map<String, Object> wss4jProps = new HashMap<String, Object>();
        wss4jProps.put("action", action);
        wss4jProps.put("signaturePropFile", propertiesFile);
        wss4jProps.put("encryptionPropFile", propertiesFile);
        wss4jProps.put("user", "joe");
        wss4jProps.put("encryptionUser", "joe");
        wss4jProps.put("passwordCallbackClass", PasswordCallback.class.getName());
        return wss4jProps;
    }
    
    protected static Map<String, Object> getSamlTokenProps(String action, String propertiesFile)
    {
        Map<String, Object> wss4jProps = new HashMap<String, Object>();
        wss4jProps.put("action", action);
        wss4jProps.put("samlPropFile", propertiesFile);
        SAMLCallbackHandler samlCallbackHandler = new SAMLCallbackHandler();
        wss4jProps.put(WSHandlerConstants.SAML_CALLBACK_REF, samlCallbackHandler);
        return wss4jProps;
    }

    protected static Map<String, Object> getSignedSamlTokenProps(String action, String propertiesFile)
    {
        Map<String, Object> wss4jProps = new HashMap<String, Object>();
        wss4jProps.put("action", action);
        wss4jProps.put("samlPropFile", propertiesFile);
        wss4jProps.put("signatureKeyIdentifier", "DirectReference");
        wss4jProps.put("user", "joe");
        wss4jProps.put("passwordCallbackClass", PasswordCallback.class.getName());
        wss4jProps.put(WSHandlerConstants.SAML_CALLBACK_REF, new SAMLCallbackHandler());
        return wss4jProps;
    }
    
    public static Greeter createService(String url, Map<String, Object> wss4jProps)
    {
        URL wsdlDocumentLocation;
        try
        {
            wsdlDocumentLocation = new URL(url);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException("Invalid test definition", e);
        }
        QName serviceName = new QName("http://security.example.mule.mulesoft.com/", "GreeterService");

        Service dynService = Service.create(wsdlDocumentLocation, serviceName);
        Greeter service = dynService.getPort(Greeter.class);
        Client client = ClientProxy.getClient(service);

        if (wss4jProps != null)
        {
            client.getOutInterceptors().add(new WSS4JOutInterceptor(wss4jProps));
        }

        return service;
    }
    
    protected static int readCharacter() throws IOException
    {
        byte[] buf = new byte[16];
        System.in.read(buf);
        return buf[0];
    }
}



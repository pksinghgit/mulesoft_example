# SOAP Web Service Security Example 

This example demonstrates how to apply varying levels of security to a SOAP Web service.

#### Web Service Security 

When a Web service exposes private-network data to the outside world, that data travels through four to seven separate protocol layers (see: [TCP/IP](http://en.wikipedia.org/wiki/TCP/IP_model) or [OSI](http://en.wikipedia.org/wiki/OSI_model)) thus introducing potential security vulnerabilities. Implementing security at a higher layer on the protocol stack — the application layer, for instance — generally provides better protection than an implementation at the lower transport layer, which provides only HTTPS security.

To implement application-layer security, enable [WS-security](http://msdn.microsoft.com/en-us/library/ms977327.aspx) (a [CXF](http://cxf.apache.org/) configuration) on your Web service. WS-security defines a new SOAP header which is capable of carrying various security tokens that systems use to identify a Web service caller's identity and privileges.

### Assumption

This document describes the details of the example within the context of **Anypoint™ Studio**, Mule ESB’s graphical user interface (GUI). Where appropriate, the XML configuration is provided.

This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Mule Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial).

### Example Use Case 
The example application applies different security measures to five of the six variations of a SOAP service it exposes (the sixth is unsecure). Each variation accepts end user salutation requests via a Web browser, then responds with a personalized greeting. For instance, when a user submit his name, “John”, to one of the six variations, he receives a response that reads, *Hello John*. The table below outlines the six variations in the WS-Security example application, and the security Mule applies to each.

- UnsecureServiceFlow - none - Processes all requests.
- UsernameTokenServiceFlow - UsernameToken - Processes requests with a valid username and password.
- UsernameTokenSignedServiceFlow - UsernameToken + signature - Processes requests with a valid username and password, and a valid digital signature.
- UsernameTokenEncryptionServiceFlow - UsernameToken + encryption - Processes encrypted requests with a valid username and password.
- SamlTokenServiceFlow - SAML2 - Processes requests in conjunction with an identity provider to verify message security.
- SignedSamlTokenServiceFlow - SAML2 + signature - Processes requests with valid digital signature in conjunction with an identity provider to verify message security.

This application also includes several variations of a **client-side Web service** to ensure a functional example. These service-clients enable end users to submit salutation requests to the server-side Web service. Refer to the [Client-Side Flows](http://www.mulesoft.org/documentation/display/current/SOAP+Web+Service+Security+Example#SOAPWebServiceSecurityExample-Client-sideFlows) section below for more details.

This document will help you understand some of the ways you can apply security to a SOAP Web service in Mule ESB applications. To understand more about Mule ESB’s ability to integrate services and systems, access the [Mule examples](https://www.mulesoft.com/exchange#!/?types=example)) and see other applications in action.
### Set Up and Run the Example 

You can create template applications straight out of the box in **Anypoint Studio** or **Mule Standlone** (Mule ESB without Studio). You can tweak the configurations of these use case-based templates to create your own customized applications in Mule.

Follow the procedure below to create, then run the WS-Security example application.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!
1. Open your Web browser.
1. In the address bar, type *localhost:63080/client?clientType=unsecure&name=John*
1. Press **enter** to elicit a response from the WS-Security application (see image below). 
1. Adjust the URL to change the **name** from John to your name, then press **enter** to elicit a new response from the application.
1. Adjust the **clientType** parameter from unsecure to any of the following five types (note that each type is case-sensitive):
	- usernameToken
	- usernameTokenSigned
	- usernameTokenEncrypted
	- samlToken
	- samlTokenSigned
1. Press **enter** to elicit a new response from the application. 

Regardless of the **clientType** you use to submit a request, Mule returns the same response. The type of security Mule uses to process the message is not reflected in the content of message, so even though Mule processes each request in a different way, the content of the response remains unchanged.

### Alternative Set Up

Rather than interacting with the application via a Web browser, you can submit end user requests via the SecureClient java class within Anypoint Studio.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!
1. In the **Package Explorer**, navigate to *src/main/java*, then expand the *com.mulesoft.mule.example.security *folder to reveal the *SecureClient* file. 
1. Right click the *SecureClient* file, then select **Run As > Java Application**.
1. In the **Console**, Mule displays a menu which lists the different types of requests you can submit to the WS-Security application.
1. Click below the last line in the list, *q*. *Quit*, to activate your cursor in the console pane.
1. Below the last line, type 2, then press **enter** to submit a request with Username Token security to the WS-Security application.
1. Mule processes the request and returns a response in the console pane which reads, *Hello Mule*. 
1. Mule displays the same menu of request submission options in the console again. Type another number, 1 – 9, below the last list item, then press **enter**.
1. Mule processes the request and returns a response in the console pane which reads, *Hello Mule*.
1. When you wish to terminate the Java application, type q, then press **enter**.

### How it Works 

This example application consists of several [flows and subflows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture). Each of these flows exposes a variation of the same Web service which processes end user requests for a salutation. The only difference between the flows is the type of Web service security each employs.

The sections below offer flow-by-flow descriptions of the WS-Security application’s actions as it processes end user requests. The Web service variation of each flow in this document is more secure than the one preceding it.

##### UnsecureService Flow 

When an end user submits an unsecure salutation request, the Web service client sends a simple SOAP request message (see below) to the UnsecureService flow in the WS-Security application.

	<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	    <soap:Body>
	        <ns2:greet xmlns:ns2="http://security.example.mule.mulesoft.com/">
	            <name>John</name>
	        </ns2:greet>
	    </soap:Body>
	</soap:Envelope>

The request-response [HTTP Endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) in this flow receives the end user request. Because it has a two-way message exchange pattern, this HTTP endpoint is responsible for both receiving and returning messages.

A JAX-WS service, the [CXF Component](http://www.mulesoft.org/documentation/display/current/CXF+Component+Reference) in this flow evaluates the message according to its security configurations. In this case, the Web service is unsecure (see image below) so the CXF component processes all requests it receives.

The [Java Component](http://www.mulesoft.org/documentation/display/current/Java+Component+Reference) executes a simple script to prepare a personalized salutation for the end user.
	
Where is the Java code?
	To access the Java code in Anypoint Studio, navigate to the source file in the Package Explorer.
	Find the Java
	Finally, the HTTP endpoint returns a simple SOAP response (see below) to the client.

	<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	    <soap:Body>
	        <ns2:greetResponse xmlns:ns2="http://security.example.mule.mulesoft.com/">
	            <name>Hello John</name>
	        </ns2:greetResponse>
	    </soap:Body>
	</soap:Envelope>

##### UsernameTokenService Flow

When an end user submits a usernameToken salutation request, the Web service client sends a SOAP request message (see below) to the UsernameTokenService flow in the WS-Security application.
Like the UnsecureService flow, this flow uses an HTTP endpoint to receive the request and a CXF component to process the message. In this case, however, the CXF component’s configuration specifies the action, which is a list of WS-security features against which Mule validates a message. This component’s UsernameToken specification verifies the following:

- username and password — confirms the client’s username and password are valid
- timestamp — verifies that the message is not stale 

##### Username and password
To demonstrate a functional example, the WS-Security application includes several **client-side flows** which provide the **server-side flows** with security information.

Normally, an independent Web service client provides the Web service provider with end user security information, such as **username** and **password**. In this case, however, Mule generates this information within its **service-clients flows** to simulate secure request submissions.

Refer to the [client-side flows](http://www.mulesoft.org/documentation/display/current/SOAP+Web+Service+Security+Example#SOAPWebServiceSecurityExample-Client-sideFlows) section below for more details.

##### UsernameTokenSignedService Flow ###

This flow validates the digital signature of a message. A message with a digital signature — in addition to username, password and timestamp — is more secure than a message without.

When an end user submits a usernameTokenSigned salutation request, the Web service client sends a SOAP request message (see below) to the **UsernameTokenSignedService** flow in the WS-Security application.

 View the SOAP Request Message
The only difference between the **UsernameTokenService** and **UsernameTokenSignedService** flows is the CXF component’s configuration. In this flow, the component includes a signature action, **signaturePropFile** (see image below), which Mule uses to validate the digital signature.

The **signaturePropFile** property specifies the keystore against which Mule must validate the digital signature on the message. The **keystore**, which is a repository containing security certificates, resides in the **wssecurity.properties** file embedded in the application.

##### Where is the wssecurity.properties File? 
To access the wssecurity.properties file in Studio, navigate to the source file in the Package Explorer.

1. In your WS-Security project, navigate to src/main/resources.
1. Double-click the wssecurity.properties file to open it in a new tab on the Studio canvas (see image below). 

The **wssecurity.properties** file contains the following properties:

1. *org.apache.ws.security.crypto.merlin.file=keystore.jks*
1. *org.apache.ws.security.crypto.merlin.keystore.password=keyStorePassword*
 
To validate the digital signature, Mule uses a Java keytool command to verify that the certificate for user joe exists in the keystore (see image below). 

Note that this example certificate is self-signed (i.e. the Owner and Issuer are the same entity). Normally, a trusted third party Issuer, such as VeriSign, issues the certificate.

##### UsernameTokenEncryptedService Flow 

In the preceding flows, the header of the SOAP message contained all the message’s security information, and the body of the message was completely transparent. This flow not only validates all the message using all the security information in the SOAP header, it decrypts the encrypted content in the body of the message. A message with an encrypted body is more secure than one with unencrypted content.

When an end user submits a usernameTokenEncrypted salutation request, the Web service client sends a SOAP request message (see below) to the **UsernameTokenEncryptedService** flow in the WS-Security application.

In this flow, the CXF component must validate the username, password, timestamp and digital signature before decrypting the body of the SOAP message. Mule uses the keystore to perform the decryption.

The **signaturePropFile** property specifies the keystore which Mule uses to perform two tasks:

1. validate the digital signature on the message
1. decrypt the body of the SOAP message

How is the Message Body Encrypted?
To demonstrate a functional example, the WS-Security application includes several **client-side flows** which provide the **server-side flows** with security information.

The **CXF Component** in the client-side **usernameTokenEncrypted** subflow encrypts the body of the SOAP message. Refer to the client-side flows section below for more details.

##### SAMLTokenService Flow 

This flow uses the SAMLTokenUnsigned timestamp action to validate [SAML2](http://en.wikipedia.org/wiki/SAML_2.0) assertions. A Web service uses Security Assertion Markup Language (SAML) to manage [single sign-on](http://saml.xml.org/web-sso) (SSO) authentication. Assuming that an external resource has already authenticated a user — confirmed username and password, for example — the SAML token in the SOAP header of a message contains one or more **subjects** to confirm that the original authenticator is trustworthy. SAML offers two different methods for a Web service to gauge the trustworthiness of a message’s sender.

1. **Sender Vouches** — The message sender must prove to the Web service that it is trustworthy. For example, a sender includes authenticity subjects in the SOAP header of a message, or provides a digital signature to prove its identity and trustworthiness.
1. **Holder of Key** — The message receiver must confirm the trustworthiness of the sender. To do so, it demands that a message contain a SAML token subject which, in turn, contains a key from a trusted source (for example, an X.509 certificate from VeriSign).
	
For more information on **SAML**, refer to the [saml.xml.org knowledge base](http://saml.xml.org/wiki/saml-wiki-knowledgebase).
When an end user submits a samlTokenSigned salutation request, the Web service client sends a SOAP request message (see below) to the **SignedSAMLTokenService** flow in the WS-security application.

In this flow, the CXF component uses the **Sender Vouches** method to confirm the trustworthiness of the message sender. The *SAMLTokenUnsigned Timestamp* action ensures that the Web service will only process messages which contains a valid subject.

Rather than perform all the security validation itself, the CXF component delegates the validation to other elements.

In this example, the CXF component delegates the chore of validating the SAML Token to the SAML2 Token Validator. (In Studio, a checked **SAML 2** box indicates this delegation.) The SAML2 Token Validator references a **custom SAML2 token validator** (see below, left), configured as a Spring bean, to determine how to validate the SAML token. The samlCustomValidator bean uses a Java class, *com.mulesoft.mule.example.security.SAMLCustomValidator*, to confirm the contents of the SAML subject are correct (see Java code below). In this case, Mule validates that the subject's NameID is *AllowGreetingService*.

For Mule Studio Users

To access the **SAMLCustomValidator** Java class in Studio, navigate to the source file in the **Package Explorer**.

1. In your WS-Security project, navigate to *src/main/java > com.mulesoft.mule.example.security*.
1. Double-click the *SAMLCustomValidator.java* file to open it in a new tab on the Studio canvas.

How Does the Web Service Client Send a SAML2 Assertion?

To demonstrate a functional example, the WS-Security application includes several **client-side flows** which provide the **server-side flows** with security information.

The **CXF Component** in the client-side **samlToken** subflow uses a SAMLCallbackHandler (see Java code below this box) to populate the assertion it sends to the Web Service. (see image below).
Refer to the client-side flows section below for more details.

##### SignedSAMLTokenService Flow

Much the same as SAMLTokenService, this flow uses SAMLTokenUnsigned action to validate SAML2 assertions. However, the action in this flow uses a digital signature to verify the trustworthiness of the original authenticator. To validate the digital signature on the message, the CXF component uses a keystore specified in the signaturePropFile. (Refer to UsernameTokenEncryptedService Flow section above for more information on verifying digital signatures.)

When an end user submits a samlTokenSigned salutation request, the Web service client sends a SOAP request message (see below) to the **SignedSAMLTokenService** flow in the WS-security application.

Where the **SAMLTokenService** flow processed unsigned messages — messages which without a digital signature from the original authenticator in the SOAP header — this flow processes signed messages.

As with the SAMLTokenService flow, the CXF component delegates SAML Token validation to the SAML2 Token Validator. The token validator, in turn, references a custom SAML2 token validator. The *samlCustomValidator* uses a Java class to confirm the contents of the SAML subject are correct. Additionally, this CXF component validates the digital signature of the message’s original authenticator against a keystore in the *wssecurity.properties* file.

##### Client-Side Flows 

This example application includes client side configurations which enable the application to function. The **service-clients** flow in the WS-Security application consists of a flow and several subflows which facilitate client-side submissions to the server side.

Double-click the *service-clients.mflow* file in the package explorer to open the **service-clients** configuration in a separate tab in Anypoint Studio.

The client-side subflows contain different kinds of client security information which enables Mule to successfully process requests on the server side. For example, the client-side **usernameToken** subflow contains the end user security information against which the server-side **UsernameTokenService** validates the message. It sets the UsernameToken and Timestamp actions, provides the user, and relies on a password callback class (see below) to provide the user password.

Without client-side services, there would be no mechanism for submitting requests to the server-side of the example application. Ergo, these client-side services exist to simulate the various real-life requests that end users might submit to a Web service.

### Go Further 
1. For more information on the SOAP Component, see [CXF Component Reference](http://www.mulesoft.org/documentation/display/current/CXF+Component+Reference).
1. For more information on WS-security, see [Enabling WS-Security](http://www.mulesoft.org/documentation/display/current/Enabling+WS-Security).

# HTTP OAuth2 Provider Example

The primary responsibility of an OAuth2 Web service provider is to control access to protected resources. Playing the part of both the Authorization server and the Resource server, the OAuth provider module hosts the protected resources and issues tokens to access protected resources without sharing the resource owner's credentials with the client applications. 

### Assumptions

This document describes the details of the example within the context of Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Mule Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial).
Ir order to see this example properly you should have the OAuth Provider Module installed in Studio. All Anypoint Enterprise Security modules for Studio can be found in the following update site:

	http://security-update-site.s3.amazonaws.com/


#### OAuth 2.0a Elements

Mule provides two elements that enable you to configure a Web service provider capable of completing the tasks listed below.

##### Global OAuth Element
	
You specify most of the configuration settings for the provider here, such as login page details, security providers, whether to issue refresh tokens, token lifespan, and supported grant types and scopes. This global element is meant to be referenced by message processors. The element in the XML format is:
	
	<oauth2-provider:config />

##### Validate

Configure to validate tokens, confirming that the client presents the correct scopes to access the protected resource. The element in the XML format is:

	<oauth2-provider:validate /> 

#### Applying OAuth 2.0a to a Web Service Provider

To apply OAuth 2.0a to a Web service that you publish, you must complete, at minimum, five tasks. The first two tasks define resources which the OAuth Provider elements reference; the last three apply OAuth 2.0a to Mule flows, initiating OAuth2 authentication when a consumer calls the Web service. The table below lists these tasks, along with the Mule elements involved in each task and the OAuth tasks for which each element is responsible.

Tasks

1. Create a Spring bean to define an authentication manager and provider that performs client authentication

2. Configure a Mule Security Manager that delegates client authentication

3. Create a Global OAuth 2.0a provider to define several OAuth parameters. It defines most of the service provider's OAuth 2.0 parameters

4. Create a Client Registration flow. It stores client IDs and secrets. **Not implemented in this example**.

5. Create OAuth Validation flows. OAuth provider module will be configured to Validate or Validate-client. It validates the access token, thereby granting, or rejecting, access to a protected resource

#### Paths to Authentication

Before tackling the work of creating an OAuth 2.0a Web service, it is important to understand the various ways in which a service provider can authenticate a client.

When a client calls an OAuth Web service, it must identify itself by one of two types: PUBLIC or CONFIDENTIAL.

+ A **PUBLIC** client provides a client ID which the Web service provider uses for authentication
+ A **CONFIDENTIAL** client provides validation credentials (client ID and client secret) which the Web service provider uses for authentication

If **CONFIDENTIAL**, a client must provide validation credentials in one of three different parts of the request:

+ In the **query**
+ In the **body**
+ In the **authentication header**. Therefore, you must configure your OAuth 2.0a Web service provider to match the type(s) of client requests you expect to receive. The figure below illustrates the different types of requests and their resulting paths to authentication.

If the client sends validation credentials in the **body** or the **query** of the request, the OAuth Web service provider simply validates the incoming credentials (client ID and client secret) against the content in the clientStore. If, on the other hand, the client sends validation credentials in the **authentication header** of the request, the service provider uses a security manager to delegate authentication to an authentication manager. The authentication manager users an authentication provider to validate a client's principals (username and password, for example).

### Example Use Case

In this example, a user will attempt to gain access to the protected resource. For this purpose, OAuth dance will be triggered. A user will be asked to enter his user name and password. If successful, the access to the resource will be granted.  

### Set Up and Run the Example ###

Complete the following procedure to create, then run this example in your own instance of Anypoint Studio. You can create template applications straight out of the box in Anypoint Studio and tweak the configurations of the use case-based templates to create your own customized applications in Mule.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!
2. Open *http-oauth-provider.xml* from the Package Explorer.
3. Specify *http.provider.port* and *http.listener.port*, e.g. values 8081, 8082 respectively. 
3. Open your Web browser.
3. In the address bar, hit the following URL: 

		http://localhost:8081/authorize?response_type=code&client_id=myclientid&scope=READ_RESOURCE&redirect_uri=http://localhost:8082/redirect 

	The SampleAPI web page will be displayed with the login form. Fill in *mule* for Username and *mule* for Password.

4. Click *Login and Authorize* button. The page content will be reloaded with a data structure containing an access token issued by the OAuth provider. It can be used to gain access to the protected resource. Copy its value.
5. Make a GET request to *http://localhost:8082/resources*. Add a following header to the request:

		Header			Value
		Authorization	Bearer ACCESS_TOKEN

To send this request, use a browser extension such as [Advanced Rest Client](https://chrome.google.com/webstore/detail/advanced-rest-client/hgmloofddffdnphfgcellkdfbfbjeloo) (Google Chrome), or the [curl](http://curl.haxx.se/) command line utility.
ACCESS_TOKEN is the value from the previous step. The OAuth provider validates the request and permits the access to the resource which contents are shown to the user:

		{"name":"payroll","uri":"http://localhost:8082/resources/payroll"} 

### How it works

The HTTP OAuth Provider example consists of two [Mule flow](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture). The first one, *protectedAuthcodeFlow*, is responsible for validating incoming requests to the protected resources. The *redirectFlow* flow acts as an aid to help manual testing of the OAuth dance.

The following global elements were defined to allow the OAuth provider functionality. 

#### resourceOwnerAuthenticationManager

It defines a single user (*mule*) that is allowed to access the resources.

#### resourceOwnerSecurityProvider

It is a security manager that delegates the client authentication to *resourceOwnerAuthenticationManager*.

#### oauth2Provider

This global element defines a single CONFIDENTIAL client with a clientId and a secret. Next, it defines an authorization grant type (*AUTHORIZATION_CODE*), a redirect URI and scopes (*READ_RESOURCE, POST_RESOURCE*). 

#### protectedAuthcodeFlow

The flow starts with an [HTTP inbound endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) that listens at *http://localhost:8082/resources*. All requests must pass through OAuth validate operation. To simulate a protected resource, a simple map representing a payroll, is [set as a payload](http://www.mulesoft.org/documentation/display/current/Set+Payload+Transformer+Reference). Finally, the payload is [transformed to a JSON format](http://www.mulesoft.org/documentation/display/current/Transformers).

#### redirectFlow
 
The flow starts with an [HTTP inbound endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) that listens at *http://localhost:8082/redirect*. The [Property transformer](http://www.mulesoft.org/documentation/display/current/Property+Transformer+Reference) follows, setting the *http.status* property to 302. Finally, the processing is redirected to:

	http://localhost:8081/token?grant_type=authorization_code&&client_id=myclientid&client_secret=myclientsecret&code=#[message.inboundProperties['http.query.params'].code] 

Again, the Property component is implemented to achieve it by setting the *Location* property.

### Go Further

- Learn more about [HTTP Connector](http://www.mulesoft.org/documentation/display/current/HTTP+Connector).
- Learn more about [Anypoint Enterprise Security](http://www.mulesoft.org/documentation/display/current/Anypoint+Enterprise+Security).
- Learn more about [OAuth2 in Mule](http://www.mulesoft.org/documentation/display/current/Creating+an+OAuth+2.0a+Web+Service+Provider).
- Learn more about [Transformers](http://www.mulesoft.org/documentation/display/current/Transformers).
# Testing APIKit with MUnit #

This application illustrates an API implementation using Anypoint Studio's tooling for building REST APIs with RAML interfaces: [APIkit](http://www.mulesoft.org/documentation/display/current/APIkit). The application takes a [RAML](http://raml.org/) file and maps it to an implementation of an API in Mule. This example implementation route the request according the method which was used (GET, POST, PUT, DELETE) and generates dummy message, but you can replace these flows with a full implementation of your choice.
The main goal of the example is to show, how to test the APIKit using the MUnit.

#### APIkit ####

APIkit is an open-source, declarative toolkit specially created to facilitate REST API implementation with RAML definitions. As a simple framework that caters to API-first development, it enforces good API implementation practices. 

### Assumptions ###

This document assumes that you are familiar with Mule and the [Anypointâ„¢ Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture), [APIKit](http://docs.mulesoft.com/anypoint-platform-for-apis/apikit-tutorial) and testing with [MUnit](https://docs.mulesoft.com/mule-user-guide/v/3.7/munit). 

This document describes the details of the example within the context of Anypoint Studio, Mule ESB graphical user interface, and includes configuration details for XML Editor where relevant. 

### Example Use Case ###

This application employs APIKit Router component to route requests to exact flow by resource/method/content-type and generate the dummy message. MUnit is used to test, if the right status code and the dummy message was generated.
### Set Up and Run the Example ###

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).
2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8080
3. In the Package Explorer pane in Studio, right-click the project name, then select **Run As > Mule Application**. Studio runs the application and Mule is up and kicking!
2. Anypoint Studio starts the application and automatically opens an API console below the canvas.
3. Click **GET** for the **/munit** resource.
The console opens a details panel for the GET method for this resource that allows you to view details about the request format and expected responses, as well as a **Try It** section in the right.
4. In the **Try It** section, click **GET**.
5. Scroll down to view the response. The API returned the status code 200 and dummy message "GET RESPONSE". 
6. Click through the other methods to test out other API calls.

### Test the Example ###

1. After we set up and run the example, we can switch to MUnit tests.
2. Open src/test/munit/api-test-suite.xml file, where the tests are located.
3. We can run the tests by right click in the Message flow working area and then choose **Run MUnit suite**.
4. The new View for MUnit tests are opened and the tests start.
5. We can check the result of the tests inside MUnit View (green - PASSED, red - FAILED).

### How it Works ###

This application is based on a RAML specification file, which you can find in the **src/main/api** folder in the package explorer. Anypoint Studio allows you to import a RAML spec and automatically generate an APIkit project with a main flow, backend flows for each allowed method for each resource, and exception strategy mappings. The following sections walk through the RAML file and each of the auto-generated aspects of the APIkit project based on it.

For example this flow will be called when we hit **"/munit"** path by **GET** method.

	<flow name="get:/munit:api-config" >
        <set-payload value="#['GET RESPONSE']" doc:name="Set Payload" />
    </flow>
    
The sample flow above is tested by MUnit throuh this code:

	<munit:test name="api-test-get" description="Test">
        <munit:set payload="#['']" doc:name="Set Message"/>
        <http:request config-ref="HTTP_Request_Configuration" path="/munit" method="GET" doc:name="HTTP"/>
        <object-to-string-transformer doc:name="Object to String"/>
        <munit:assert-on-equals message="The HTTP Status code is not correct!" expectedValue="#[200]" actualValue="#[message.inboundProperties['http.status']]" doc:name="Assert Equals"/>
        <munit:assert-on-equals message="The response payload is not correct!" expectedValue="&quot;GET RESPONSE&quot;" actualValue="#[payload]" doc:name="Assert Equals"/>
    </munit:test>

1. At the beginning we set the payload to empty String.
2. Then we hit HTTP endpoint by **"/munit"** path and **GET** method through HTTP request (via HTTP connector).
This enables you to use the HTTP request to define everything you need in order to hit a resource of your API (HTTP verbs, headers, paths, MIME types, etc.).
3. The returned payload from HTTP connector is in the format of InputStream, so we have to transform it to String for further testing.
4. The two assertions in the test **validate the HTTP status code** and the other one **validate the returned payload**.

### MUnit Test Suit "Musts" ###

Each MUnit test file _must_ contain the following beans:

* _MUnit config_
* The _import section_

These are shown in the snippet below:

*MUnit Musts*

	<munit:config doc:name="Munit configuration" mock-connectors="false" mock-inbounds="false"/>    

	<spring:beans>
        <spring:import resource="classpath:api.xml"/>
    </spring:beans>

In the _MUnit config_, we have to set the _mock-connectors_ and _mock-inbounds_ attributes to false.

* **mock-connectors** defines if the app connectors(transports not cloud connectors) for outbound/inbound endpoints have to be 
 mocked. If they are then all outbound endpoints/inbound endpoints must be mocked. _The default value is true_.
 
* **mock-inbounds** defines if the inbounds are mocked or not. _The default value is true_.


In the _import section_, we define the files needed for this test to run. This section usually includes the file containing the flows we want to test, and additional files required for the first file to work.

**WARNING**: MUnit Test Suite files will not run without MUnit config.

#### RAML File ####

Open the RAML file in the src/main/api folder to review the details of this API implementation.
Compare the resource and methods defined in the RAML file with what you see in the API console in Studio. The API console provides interactive documentation for your API. When you publish your finished API, you can share this console with users by sending them to your API's baseURI with /console appended to the end. For instance, the base URI in this RAML definition is currently [http://localhost:8080/api](http://localhost:8080/api,), so you can access the console for this API at http://localhost:8080/api/console. To deploy this API, you would replace the baseURI in the RAML file with the deployed baseURI, so the console would then be accessed at http://myapibaseURI.com/console.

#### Main Flow 
The main flow is standard for an APIkit project. It contains an inbound endpoint and an APIkit Router. The exception strategies are explained separately, below.

#### Exception Strategy Mappings ####

Studio automatically generates several global exception strategy mappings that the Main flow references to send error responses in HTTP-status-code-friendly format. Defined at a global level within the project's XML config, this standard set of exception strategy mappings ensure that anytime a backend flow throws an exception, the API responds to the caller with an HTTP-status code and corresponding plain-language message. 


### Go Further ###

- Learn more about [APIkit](http://www.mulesoft.org/documentation/display/current/APIkit) by following the [APIkit Tutorial](http://www.mulesoft.org/documentation/display/current/APIkit+Tutorial).
- Start designing your own RAML-based APIs in [API Designer](http://api-portal.anypoint.mulesoft.com/raml/api-designer).
- Create more advanced tests with [MUnit](http://docs.mulesoft.com/mule-user-guide/v/3.7/munit).

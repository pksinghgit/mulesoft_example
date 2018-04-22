# Legacy Modernization

This application illustrates how to automate communication between a legacy system and a Web service which accepts HTTP requests. Mule "front-ends" the legacy system – which only accepts input via a file – so as to prepare data from an HTTP Web service to a format that the legacy system accepts. To demonstrate these capabilities, this example adopts the use case of a legacy fulfillment system which must adapt to accept orders via HTTP request. You can think of this example as an application that acts as a Web service proxy for a legacy, file-based system.

### Legacy Modernization

An older, legacy system may be limited in the form of data that it accepts. For example, an older system may only accept input as a file or via FTP. To update such a system so that it accepts more modern input formats, such as Web service calls, Mule can sit in front of a legacy system, converting HTTP requests, for example, to strings. This conversion activity effectively "modernizes" the legacy system so that it accepts HTTP requests.

### Assumptions 

This document assumes that you are familiar with Mule ESB and the [Anypoint™ Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial).  Further, this example assumes you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/33X/Mule+Application+Architecture), SOAP Web services and using [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation) in Anypoint Studio.

This document describes the details of the example within the context of Anypoint Studio, Mule ESB’s graphical user interface (GUI), and includes configuration details for both the visual and XML editors.

### Example Use Case 

This example demonstrates legacy system modernization within the context of a simple use case.

An organization uses an old fulfillment system which only accepts orders in flat file format. However, the company wants to begin accepting orders via a SOAP Web service and automatically submitting the orders to the legacy system for fulfillment. In order to process orders, the company uses Mule to convert HTTP requests into a file format that the legacy system accepts. 

### Set Up and run the Example

Complete the following procedure to create, then run this example in your own instance of Anypoint Studio. You can create template applications straight out of the box in Anypoint Studio and tweak the configurations of this use case-based template to create your own customized applications in Mule.

Skip ahead to the next section if you prefer to simply examine this example code snippets.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking! 
1. To simulate a request submission to the Mule application, use the **soapUI interface** available for free download at [www.soapui.org](http://www.soapui.org/). This tool enables you to submit a request to simulate the submission of a new order in this example's use case.  If you haven't already done so, download and launch soapUI. 
2. In soapUI, select **File > Import Project**. Browse to the AnypointStudio folder on your local drive to locate the sample soapUI project file: **AnypointStudio > workspace > Legacy Modernization > src > main > resources > LegacyModernizationExample-soapui-project.xml**. Click **Open**.
2. In the new LegacyModernizationExample project in soapUI, expand the folders to reveal **Request 1**. Double-click **Request 1** to open the request-response window.
2. Click the submit request icon (green "play" button at upper left) to submit the request to the Mule application (see below, left). soapUI displays the response from the Mule application in the response pane (see below, right).
3. Review the contents of the SOAP response, to examine the details of your processed request. Note, in particular, the orderReceivedStatus with the value "true".
3. In the **Package Explorer** in Studio, navigate to *src/main/resources/Output*, then hit F5 to refresh the contents of the Output folder. The order you submitted via soapUI appears as a new ShippingOrder flat file; note the date and time stamp of the new order (see below). Double-click the flat file to open it in Studio and examine the contents.

### How it Works 

Using a single flow, this application exposes a SOAP Web service which accepts new order requests from customers. A Java component processes the order, returning a response to the caller to advise that the order has been accepted. Asynchronously (relative to the HTTP request-response activity), the application uses a DataWeave transformer to map data from the shipping order POJO into a CSV file, which it sends to the legacy system for fulfillment. In this example, since there is no actual legacy system to perform order fulfillment, the File endpoint at the end of the flow simply outputs the CSV file to the Output folder in the src/main/resources folder within the application. 

There are a couple important configurations to take note of in this example application.

- **The application exposes a SOAP Web service**.  Configured as a JAX-WS service, the CXF component in the flow makes the Web service available using the Apache CXF Web services framework.
- **DataWeave transforms and maps data from POJO to CSV in one step**. Click **Preview** to examine the CSV output that the DataWeave transformer will produce when the application processes orders.

### Documentation 

Anypoint Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to easily share your project with others outside the Studio environment, you can export the project's documentation to print, email or share online. Studio's auto-generated documentation includes:

- a visual diagram of the flows in your application
- the XML configuration which corresponds to each flow in your application
- the text you entered in the Notes tab of any building block in your flow

Follow [the procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.

### Go Further 
- Learn more about the [CXF Component in Studio](http://www.mulesoft.org/documentation/display/current/CXF+Component+Reference).
- Learn more about the [Anypoint DataWeave transformer](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation).

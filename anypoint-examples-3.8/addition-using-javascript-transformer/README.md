#Addition using JavaScript Transformer

This example shows how to parse an incoming JSON message and process it using the JavaScript transformer.

###Assumption

This document describes the details of the example within the context of Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture)

### Example Use Case

In this example, we send a JSON message containing two numbers to an HTTP endpoint. This Byte Array of data is transformed to a String. The JavaScript transformer then processes the data and adds up the numbers. The sum is then returned to the HTTP endpoint.

### Set up and Run the Example


1. Open this example project in studio

2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081

3. Run the example

4. Use Postman, curl or REST console to make a **POST** request using JSON. The message body should contain the following data:

	{ 
    "a": 3, "b": 4
    }

5. If the project is running successfully you should get the following message on your studio console.

		INFO  2014-07-02 16:44:34,235 [[javascript-calculator].connector.http.mule.default.receiver.03] org.mule.api.processor.LoggerMessageProcessor: Sum is: 7.0

### How it Works ###

The request-response inbound [HTTP endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) receives requests the end user submits to the Web service. Because it has a request-response message exchange pattern, this HTTP endpoint is responsible for both receiving and returning messages. The request body is converted from byte array to String and the evaluated using Javascript component. The formatted output is returned back to the client. 

The following steps outline the process to build this application.
    
1. Drop an HTTP Connector to the flow. 
2. Click on the plus sign next to the Connector Configuration field. A dialogue for creating HTTP Listener Configuration will be displayed. Fill in:

		Host 	localhost
		Port 	8081 
   Click Ok button.	
2. Set *Path* attribute to */* value and *Allowed methods* to *POST* value.
2. Add a Byte Array to String transformer.
3. Add a Javascript transformer with the following script:

		var list = eval("(" + payload + ")");
		var sum = 0;
		for (var i in list)
		 sum += list[i];
		payload = sum;
4. Add a Logger component with Message property set to:

		Sum is: #[payload]
5. Finish the flow by dropping a Set Payload processor with Value property set to:

		Sum is: #[payload].

### Documentation ###

Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to share your project with others outside the Studio environment, you can export the project's documentation to print, email or share online. Studio's auto-generated documentation includes:

- A visual diagram of the flows in your application
- The XML configuration which corresponds to each flow in your application
- The text you entered in the Notes tab of any building block in your flow

Follow [the procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.

###Go Further

* Read more about Scripting in the Anypoint platform [here](http://www.mulesoft.org/documentation/display/33X/Scripting+Example)

* Read more about the JavaScript Component [here](http://www.mulesoft.org/documentation/display/current/JavaScript+Component+Reference)

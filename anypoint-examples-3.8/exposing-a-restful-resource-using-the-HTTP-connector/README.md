# Exposing a RESTful resource using the HTTP connector Example

This example application illustrates how to use Mule ESB to expose a RESTful resource using the HTTP connector. After reading this document, creating and running the example in Mule, you should be able to leverage what you have learned to create a simple HTTP request-response application that is able to expose a RESTful resource by providing different verbs (HTTP methods) using JSON data.

### JSON processing

This example was designed to demonstrate interaction between an end user and Mule via an HTTP request, and Mule's ability to process data in the JSON format.

### Assumptions

This document describes the details of the example within the context of Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Mule Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial).

### Example Use Case

In this example, a user calls the Mule application by submitting a request via REST client (i.e. entering a specific URL, http://localhost:8081/person/1). The application receives the request and process it based on the URL. The application is capable of retrieving and inserting person data.  

	To send this request, use a browser extension such as [Advanced Rest Client](https://chrome.google.com/webstore/detail/advanced-rest-client/hgmloofddffdnphfgcellkdfbfbjeloo) (Google Chrome), or the [curl](http://curl.haxx.se/) command line utility.  


### Set Up and Run the Example

As with other [examples](https://www.mulesoft.com/exchange#!/?types=example), you can create template applications straight out of the box in Anypoint Studio. You can tweak the configurations of these use case-based examples to create your own customized applications in Mule.

Follow the procedure below to create, then run the HTTP with JSON application.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).
2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081
3. In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!
4. Send a POST request to **http://localhost:8081/person** with the body equals to:
		
		{
		 	"firstname":"John",
		 	"lastname":"Doe",
		 	"age": "12",
		 	"address": 
		    {
		    	"streetAddress":"Lincoln St.",
		        "city":"San Francisco",
		        "state":"CA",
		        "zipCode":"90401"
			}
		} 
5. You should recieve a response saying a person was created successfully: 

		{ "status": "success", "statusDescription": "person created successfully"}
6. Send a GET request to **http://localhost:8081/person/1**
7. You should recieve a response with the person data:

		{"firstname":"John","lastname":"Doe","address":{"streetAddress":"Lincoln St.","city":"San Francisco","state":"CA","zipCode":"90401"},"age":12}

8. Send a GET request to **http://localhost:8081/person**
9. You should recieve a response with all created persons. If you inserted only 1 person from Step 2, then you should get only that one. 

### How it Works

The **Exposing a RESTful resource using the HTTP connector** example application consists of three, simple [flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture) which receives end user HTTP requests, sets payloads based on the request paths and sent data and returns the payloads to end users as HTTP responses.

The sections below elaborate further on the configurations of the application and how it works to respond to end user requests.


### retrieveAllPersonsFlow

This flow makes use of three [building blocks](http://www.mulesoft.org/documentation/display/current/Elements+in+a+Mule+Flow) to receive, process and respond to an end user requests. When an end user request encounters the application, the first building block it meets is [HTTP Inbound Endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector). Because it has a two-way message exchange pattern, this HTTP endpoint is responsible for both receiving requests from, and send sending responses to, the end user.

Then the Set Payload Transformer uses a [MEL Expression](http://www.mulesoft.org/documentation/display/current/Mule+Expression+Language+MEL) to extract all persons from the data store. In this case, the expression is:

	 #[app.registry.'personDataStore'.values()]

Next, the flow uses a Convert Object to JSON to convert a persons' list to JSON format.

Finally, Mule moves the message back to the [HTTP Inbound Endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) which simply returns the message payload as the response to an end user.

### storePersonFlow

This flow makes use of five [building blocks](http://www.mulesoft.org/documentation/display/current/Elements+in+a+Mule+Flow) to receive, process and respond to an end user requests. When an end user request encounters the application, the first building block it meets is [HTTP Inbound Endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector). It listens to POST requests at http://localhost:8081/person. 

Then it converts JSON data structure from the payload into a Person Object. A Java bean of AtomicInteger datatype is defined in order to generate unique ID for each person. This ID will be used as a key in a map where values are  of Person data type. 

Next, payload is set to a constant String: 

	{ "status": "success", "statusDescription": "person created successfully"}

Finally, Mule moves the message back to the [HTTP Inbound Endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) which simply returns the message payload as the response to an end user.

### retrievePersonFlow

This flow is responsible for retrieving a specific person data based on the provided person ID. It starts with an [HTTP Inbound Endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) that listens at http://localhost:8081/person/{personId}.

Then, it extracts a person ID from the request URL using MEL and stores it in the flow variable named *personId*:

	#[Integer.valueOf(message.inboundProperties.'http.uri.params'.personId)]

The logger prints the simple message with the person id to the console:

	 Person id is #[personId]

The [filter component](http://www.mulesoft.org/documentation/display/current/Filters) follows. It tests whether the person data store contains an entry with the given person Id. If there is no such entry, the processing stops and the following message is returned:

	Message has been rejected by filter. Message payload is of type: NullPayload

If yes, then the person object is returned to the end user as a response in JSON format. 

This flow defines an exception strategy in order to correctly process requests for non-existing records. In such case error status code and phrase are set and returned to the end user.
  
### Go Further

- Learn more about the [HTTP endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector).
- Learn more about the [Set Payload Transformer](http://www.mulesoft.org/documentation/display/current/Set+Payload+Transformer+Reference).
- Learn more about the [Error Handling](http://www.mulesoft.org/documentation/display/current/Error+Handling)
- Learn more about the [Filter component](http://www.mulesoft.org/documentation/display/current/Filters). 
# Filtering a Message #

This example shows how to use filter components within Anypoint Studio to filter an incoming message.  

### Assumptions ###

This document describes the details of the example within the context of **Anypoint™ Studio**, Mule ESB’s graphical user interface (GUI). Where appropriate, the XML configuration is provided.

This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Mule Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture).

### Example Use Case ###

This example application receives the order in JSON format for filtering. It employs And, Message property, Payload and Custom filters to validate some order properties. The Message property filter filters out messages based on whether they are HTTP POSTs or not while the Payload filter filters messages that are of the data type: ContentLengthInputStream. The AND filter combines the Message Property filter and the Payload filter and allows only those messages that satisfy both the conditions to pass by. The Custom filter uses a JAVA class to filter order messages that meet a specific criteria.

### Set Up and Run the Example ###

Complete the following procedure to create, then run this example in your own instance of Anypoint Studio. Skip ahead to the next section if you prefer to simply examine this example.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).
2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081
3. In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!
4. Send a POST request with the following JSON in the body to *http:/localhost:8081*.

		{
		 "purchases": 2000,
		 "months": 12,
		 "membership": "free"
		}

5. Examine the response body to see that a customer was granted a discount.

		the discount was granted.

### How it Works ###

The application contains several [filter components](http://www.mulesoft.org/documentation/display/current/Filters). Firstly, it checks the data type of incoming message using **Payload Filter** and (using [Logic Filter](http://www.mulesoft.org/documentation/display/current/Logic+Filter)) [Message Property Filter](http://www.mulesoft.org/documentation/display/current/Message+Property+Filter) to check the *http.method* property. [JSON to Object Transformer](http://www.mulesoft.org/documentation/display/current/Transformers) transforms the input to 
*java.util.HashMap*. The [Custom Filter](http://www.mulesoft.org/documentation/display/current/Custom+Filter) which is set to a custom Java Class that implements a custom filtering criterion.The messages that meet all of these criteriapass through all the filters. The message *the discount was granted*. is set in [Set Payload](http://www.mulesoft.org/documentation/display/current/Set+Payload+Transformer+Reference) and is displayed to the user as a respomse message to acknowledge that the message met all the filtering criteria.

The following steps outline the process to build this application.

1. Create a new Mule Project by going to **File > New... > Mule Project** and name it **Filtering a Message**.
2. Drop an HTTP Connector to the flow. 
2. Click on the plus sign next to the Connector Configuration field. A dialogue for creating HTTP Listener Configuration will be displayed. Fill in:

		Host 	localhost
		Port 	8081 
   Click Ok button.	
2. Set *Path* attribute to */* value and *Allowed Methods* to *POST*.
3. Drag an **And Filter**. 
4. Add a **Message Property Filter** to the **And Filter**.

		Pattern			http.method=post
		Scope			Inbound
		
5. Add a **Payload Filter** and set Expected Type to *org.apache.commons.httpclient.ContentLengthInputStream*. 
6. Add a **JSON to Object Transformer** and set Return class to *java.util.HashMap* in the Advanced tab. 
7. Add a **Custom Filter** and set Class to *org.mule.examples.filters.FreeMembershipDiscountFilter*.
8. Add a **Set Payload Transformer** and set Value to **the discount was granted.**.
9. Finish the flow with a **HTTP Response Builder** and use the following data to configure it:

		Status			200
		Content Type	text/plain
10. Now you have a complete flow with various filters that act on the incoming message.


### Go Further ###

- For more information on filters, please visit the [Filters Reference](http://www.mulesoft.org/documentation/display/current/Filters) section.  

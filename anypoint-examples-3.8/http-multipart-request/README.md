# HTTP Multipart Request Example

This example application illustrates how to use Mule ESB to build a simple HTTP application with the file submit form. After reading this document, creating and running the example in Mule, you should be able to leverage what you have learned to create an HTTP request-response application that is able to process incoming HTTP requests containing submitted file data and store it in the file system.

### Assumptions

This document describes the details of the example within the context of Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Mule Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial).

### Example Use Case

In this example, a user submits a file in the HTML form that is sent via HTTP to Mule ESB. The application receives the data, extracts some properties and stores it to the specified directory of the file system. 

### Set Up and Run the Example

As with other [examples](https://www.mulesoft.com/exchange#!/?types=example), you can create template applications straight out of the box in Anypoint Studio. You can tweak the configurations of these use case-based examples to create your own customized applications in Mule.

Follow the procedure below to create, then run the Login using HTML Form application.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).
2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081
3. In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!
4. Open your browser and hit **http://localhost:8081/uploadFile**.
5. Click *Browse...* and choose a file to upload. Hit *Send* button. 
6. Either you will see the file contents in your browser or a dialogue will prompt you to save the file contents, based on the content type.

### How it Works

The **HTTP Multipart Request** example application consists of two, simple [flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture) which receives end user HTTP requests, sets payloads based on the request paths and sent data and returns the payloads to end users as HTTP responses.

The sections below elaborate further on the configurations of the application and how it works to respond to end user requests.

### httpRenderFlow

This flow makes use of two [building blocks](http://www.mulesoft.org/documentation/display/current/Elements+in+a+Mule+Flow) to receive and respond to an end user requests. When an end user request encounters the application, the first building block it meets is [HTTP Inbound Endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector). Because it has a two-way message exchange pattern, this HTTP endpoint is responsible for both receiving requests from and sending responses to the end user.

Then, [Parse Template component](http://www.mulesoft.org/documentation/display/current/Parse+Template+Reference) loads an HTML document from the given location and Mule moves the message back to the [HTTP Inbound Endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) which simply returns the message payload as the response to an end user in the form of HTML.

### httpMultipartRequestFlow

This flow receives only POST requests as specified in the HTTP component. 

The following Expression component extracts *attachmentFileName* using [Mule Expression Language (MEL)](http://www.mulesoft.org/documentation/display/current/Mule+Expression+Language+MEL). 

The Logger component prints a content-type of the incoming file attachment to the console. 

The Set Payload component sets the file contents as a payload. Again, MEL is put to practice. 

Finally, Mule moves the message back to the [HTTP Inbound Endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) which simply returns the message payload as the response to an end user.
  
### Go Further

- Learn more about the [HTTP endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector).
- Learn more about the [Error Handling](http://www.mulesoft.org/documentation/display/current/Error+Handling)
- Learn more about the [Mule Expression Language](http://www.mulesoft.org/documentation/display/current/Mule+Expression+Language+MEL) 
- Learn more about the [Filter component](http://www.mulesoft.org/documentation/display/current/Filters).
- Learn more about the [Parse Template component](http://www.mulesoft.org/documentation/display/current/Parse+Template+Reference)
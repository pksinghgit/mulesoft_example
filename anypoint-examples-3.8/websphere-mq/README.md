# WebSphere MQ Example #
#### Enterprise Edition, CloudHub  ####

The WebSphere MQ example demonstrates how to use the Websphere MQ Transport  to send and receive transactional messages to WebSphere MQ. This example is available in Mule Enterprise Edition.

### Cooperate with WMQ ###

At times, you may find the need to bridge a communication gap between IBM's message queue product, Websphere MQ, and Mule.  To facilitate message processing across products, Mule uses a WMQ Endpoint. 

### Assumptions ###

This document assumes that you are familiar with Mule ESB and the [Anypoint™ Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). 

This document describes the details of the example within the context of Anypoint Studio, Mule ESB’s graphical user interface (GUI), and includes configuration details for both the visual and XML editors. 

### Set Up and Run the Example ###

As with other [examples](https://www.mulesoft.com/exchange#!/?types=example), you can create template applications straight out of the box in **Anypoint Studio** or **Mule Standalone** (Mule ESB without Studio). You can tweak the configurations of these use case-based templates to create your own customized applications in Mule.

#### Prerequisites ####

Before you run the example, ensure that your system meets the following conditions:

- Mule Enterprise is installed
- WebSphere MQ 6 or 7 is installed
- the WebSphere MQ client JARs are available to you

Follow the procedure below to create, then configure and run the Websphere MQ application in Anypoint Studio or Mule Standalone.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). Do not run the application.
1. Add your WebSphere MQ client libraries to the project build path.
1. Make sure your WebSphere MQ installation has a channel and queues named "in" and "out."
1. Edit the following attributes of the global Websphere MQ configuration that are appropriate for your WebSphere MQ installation by opening *websphere-mq.xml* under src/main/app, then opening *Global elements* and choosing *WMQ* component:
 + ${wmq.host}
 + ${wmq.port}
 + ${wmq.username}
 + ${wmq.password}
 + ${wmq.channel}
 + ${wmq.queue}
1. Run the application. 

In this section we will demonstrate basic usage of the application: sending text messages, then retrieving them from a Websphere MQ using Mule.

1. After Mule starts up, open a Web browser and navigate to the following URL: 
    
	[http://localhost:8086/services/wmqExample](http://localhost:8086/services/wmqExample)
1. The application prompts you to enter text to send a message to WebSphere MQ. The app transmits your input via AJAX/WMQ to the Mule application's "in" queue and you will receive confirmation that your message has been submitted. Enter some text, then hit "Send".
1. Mule processes the message sent to the "in" queue, then it sends the confirmation to you via the "out" queue and WMQ/AJAX. Note that Mule introduces an intentional 15 second delay between receiving your message and sending you a confirmation . Mule notifies you when the message is received and its content will be added to the table below.
1. Play with it! Start typing several messages in the text box, then hit "Send" to submit them to Mule via AJAX/WMQ. After 15 seconds of intentional delay, the Mule confirms receipt of the messages.

### How it works ###

### Global Elements ###

The WMQ connector is configured with the values you defined using Anypoint Studio. In Anypoint Studio, this configuration is stored as a [Global Element](http://www.mulesoft.org/documentation/display/current/Global+Elements), named *wmqConnector*. To view the configured global element, click the **Global Elements** tab under the canvas, then double-click the *wmqConnector* Global Element. Studio displays the **Global Element Properties**, shown below:

		<wmq:connector name="wmqConnector" hostName="${wmq.host}" port="${wmq.port}" queueManager="${wmq.queue}" username="${wmq.username}" password="${wmq.password}" doc:name="WMQ Connector">
    	<ee:reconnect-forever/>
		</wmq:connector>
To be able to send and receive Mule events asynchronously to and from a web browser, the application uses an AJAX connector. To view the configured global element for the AJAX connector, click the Global Elements tab under the canvas, then double-click the ajaxServer Global Element. Studio displays the Global Element Properties, shown below:

		<ajax:connector name="ajaxServer" serverUrl="http://0.0.0.0:8086/services/wmqExample"
        resourceBase="${app.home}/docroot" disableReplyTo="true" doc:name="Ajax"/>

#### Flows 

The application contains three flows, which process, then retrieve messages from a WMQ.

#### Input flow 

The first building block in the flow is an [AJAX Inbound Endpoint](http://www.mulesoft.org/documentation/display/current/Ajax+Connector), which listens for messages on the configured channel, */services/wmqExample/enqueue*. The flow adds the incoming messages to the "in" queue.

#### MessageProcessor Flow 

The MessageProcessor flow reads from the "in" queue. The flow's **test component** appends a string to the message, waits 15 seconds, then adds the message to the 'out' queue.

#### Output Flow 

The **Output** flow reads messages from the "out" queue, then publishes via the AJAX outbound endpoint.

### Documentation ###

Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to share your project with others outside the Studio environment, you can export the project's documentation to print, email or share online. Studio's auto-generated documentation includes:

- A visual diagram of the flows in your application
- The XML configuration which corresponds to each flow in your application
- The text you entered in the Notes tab of any building block in your flow

Follow [the procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.

**Go Further**

- Explore more [Mule example applications](https://www.mulesoft.com/exchange#!/?types=example).
- Learn more about the [WMQ Connector](http://www.mulesoft.org/documentation/display/current/WMQ+Connector).
- Learn more about the [AJAX Connector](http://www.mulesoft.org/documentation/display/current/Ajax+Connector).
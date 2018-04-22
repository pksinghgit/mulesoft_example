# Service Orchestration and Choice Routing

This application illustrates the orchestration of Web service calls and message queue submissions in order to fulfill an HTTP request for order fulfillment. Routing messages according to the content of their payload, the application efficiently coordinates order fulfillment with different vendors and initiates auditing of orders.

#### Foreach Processing 

An iterative processor, Foreach splits collections into elements, then processes them iteratively without losing any of the message payload. After Foreach splits a message collection and processes the individual elements, it returns a reference to the original message thus resulting in "Java in, Java out" processing.

#### Content-Based Routing 

Mule has the ability to intelligently route a message through different processing pathways according to its content. Using a Choice Router, Mule uses an expression to evaluate a message's properties, or part of its payload, then routes it to a specific pathway (i.e. series of message processors). Content-based routing, as this activity is called, dynamically applies routing criteria to a message at runtime.

#### Service Orchestration 
This term applies to the activity of coordinating calls to several different Web services in order to process a single Web service request. As the name implies, an application, such as this example, can orchestrate the sequence of calls to services.  Like the conductor of an orchestra, a single Mule flow signals when to submit calls to services, ensuring that all the moving pieces work together to produce a single response.

#### Cache

Caching message content during processing saves time and processing load by storing and reusing frequently called data. The cache scope processes the message, delivers the output, and saves the output (i.e. caches the response). The next time the flow encounters the same kind of request, Mule may offer a cached response rather than invoking, again, a potentially time-consuming process.

### Assumption

This document assumes that you are familiar with Mule ESB and the [Anypoint™ Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes you are familiar with XML coding, and that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture), SOAP as a Web service paradigm, and the practice of WSDL-first Web service development. 

This document describes the details of the example within the context of Anypoint Studio, Mule ESB’s graphical user interface (GUI), and includes configuration details for both the visual and XML editors. 

### Example Use Case

This example application simulates a Web application for an electronics store. Customers can place purchase orders through a Web interface. The backend application classifies these orders, then processes them according to their classification.

The application acquires the price for each order, sets a status for the order's result (success or failure), then audits the order and amalgamates the results into a summary reply to be sent back to the customer. If the order fails and the purchase price is above $5000, the application initiates the rollback exception strategy.

### Set Up and Run the Example 

As with the other example templates, you can create a fully-functioning applications straight out of the box in Anypoint Studio. You can tweak the configurations of these use case-based examples to create your own customized applications in Mule.

Follow the procedure below to run and test the functionality of this example application in Anypoint Studio.


1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!
1. Open a Web browser, then navigate to the following URL:
[http://localhost:8091/populate/](http://localhost:8091/populate/)
This causes the application to initialize a local database to store the orders you place. Your browser displays a message that reads: *db populated*.
3. In your Web browser, navigate to the following URL: [http://localhost:8090/orders](http://localhost:8090/orders).  Note that sometimes the AJAX server may be delayed for a few seconds while starting up.
1. Enter order data in the fields, then click Add.
1. The interface adds your order to the table structure on the right. Click Submit to send your order to the Mule application.
2. Mule processes the order and returns a response which your browser displays in the **Confirmation** tab.

The second approach to running this example is using raw SOAP that bypasses the ordering process via the GUI. To achieve this, follow the procedure bellow:

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!
2. Open a Web browser, then navigate to the following URL:
[http://localhost:8091/populate/](http://localhost:8091/populate/)
This causes the application to initialize a local database to store the orders you place. Your browser displays a message that reads: *db populated*.
3. In your Web browser, navigate to the following URL: [http://localhost:8090/orders](http://localhost:8090/orders). Click the third tab **Soap**.
4. Insert the following XML code and click *Submit >>*:

		<soapenv:Envelope
			xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
			xmlns:ord="http://orders.se.mulesoft.com/">
		   <soapenv:Header />
		   <soapenv:Body>
		      <ord:processOrder>
		         <order>
		            <orderId>12</orderId>
		            <customer>
		               <address>Main Street 123</address>
		               <firstName>John</firstName>
		               <lastName>Doe</lastName>
		            </customer>
		            <orderItems>
		               <item>
		                  <manufacturer>Samsung</manufacturer>
		                  <name>s-1</name>
		                  <productId>AX02</productId>
		                  <quantity>5</quantity>
		               </item>               
		            </orderItems>
		         </order>
		      </ord:processOrder>
		   </soapenv:Body>
		</soapenv:Envelope>

5. The acknowledging response is generated and shown in the right panel. The formatted XML code should look like this:

		<?xml version="1.0" encoding="UTF-8"?>
		<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
		   <soap:Body>
		      <ns2:processOrderResponse xmlns:ns2="http://orders.se.mulesoft.com/">
		         <summary>
		            <orderId>12</orderId>
		            <customer>
		               <address>Main Street 123</address>
		               <firstName>John</firstName>
		               <lastName>Doe</lastName>
		            </customer>
		            <orderItems>
		               <item>
		                  <manufacturer>Samsung</manufacturer>
		                  <name>s-1</name>
		                  <productId>AX02</productId>
		                  <purchaseReceipt>
		                     <id>1</id>
		                     <status>ACCEPTED</status>
		                     <totalPrice>12750.0</totalPrice>
		                  </purchaseReceipt>
		                  <quantity>5</quantity>
		               </item>
		            </orderItems>
		         </summary>
		      </ns2:processOrderResponse>
		   </soap:Body>
		</soap:Envelope>

Notice the purchaseReceipt XML element with the calculated total price.

### How It Works

The Service Orchestration and Choice Routing application exposes a SOAP-based Web service, which accepts order requests and processes each item inside the order. If a requested item is manufactured by Samsung, then the application routes that particular item request to an external SOAP-based Web service hosted by Samsung. The application sends all other item requests to an in-house order processor, defined in the [inhouseOrder](http://www.mulesoft.org/documentation/display/current/Service+Orchestration+and+Choice+Routing+Example#ServiceOrchestrationandChoiceRoutingExample-inhouseOrderFlow) flow.

Regardless of who processes the request, the application stores the result for the request (success or failure) with the price, which it retrieves from an in-house RESTful Web service. To reduce calls to this service and improve order processing efficiency, the application caches orders' results for a period of time.

Finally, the application audits the order and amalgamates the results of item requests into a summary reply to be sent back to the client. If the order fails and the purchase price is above $5000, the application initiates the rollback exception strategy.

If an invocation to Samsung's Web service fails, the application does not retry to invoke the service, but sets the purchase receipt status for the order to *REJECTED*.

The application stores the files for the Web page in *src/main/app/docroot/*. The index.html file in this directory contains the click handler for the Submit button ($(#submitButton)), which uses [RPC](http://en.wikipedia.org/wiki/Remote_procedure_call) (via mule.rpc) to call the orderService flow. 

#### orderService Flow 

The main flow of the application, the orderService flow accepts orders via a SOAP Web service, routes the order to the appropriate order queue according to the content of the message, and dispatches a request to initiate an audit of the order. This flow is also responsible for returning a response to the caller to confirm that the order was processed.  

The first building block in the orderService flow, an [HTTP Inbound connecto](http://www.mulesoft.org/documentation/display/current/HTTP+Connector)r, receives orders entered by the user in the Web page served by the application. A [CXF Component](http://www.mulesoft.org/documentation/display/current/Service+Orchestration+and+Choice+Routing+Example#) converts the incoming XML into the [JAXB annotated classes](http://en.wikipedia.org/wiki/JAXB) referenced in the Web service interface. The [Choice Router](http://www.mulesoft.org/documentation/display/current/Choice+Flow+Control+Reference) in the flow parses the message payload; if the payload defines the manufacturer as Samsung, the Choice Strategy routes the message to a [VM Outbound connector](http://www.mulesoft.org/documentation/display/current/VM+Transport+Reference) which calls the samsungOrder flow. Otherwise, the Choice Strategy routes the message to a [JMS Outbound connector](http://www.mulesoft.org/documentation/display/current/JMS+Transport+Reference) which calls the inhouseOrder flow.

When either the samsungOrder flow or the inhouseOrder flow replies, the orderService flow enriches the item with the purchase receipt provided by the replying flow. Then, the orderService flow uses another VM Outbound connector to asynchronously dispatch the enriched message to the auditService flow.

Notes:

- This flow uses a Session Variable Transformer to initialize the totalValue variable with the price of the item, in order to enable the auditService flow to use this value for auditing.
- Each iteration replaces the payload variable with the result of inhouseOrder or samsungOrder. So, in order to access the original payload as it was before it entered the loop, we use the special foreach variable *rootMessage*:

		#[rootMessage.payload.orderItems[counter - 1].purchaseReceipt]

#### samsungOrder Flow 
The samsungOrder flow delegates processing of Samsung order item requests to an external, SOAP-based Web service at Samsung.

The first building block is a [VM Inbound connector](http://www.mulesoft.org/documentation/display/current/VM+Transport+Reference), which provides the flow with the information from the orderService flow. The second building block, an [Anypoint DataWeave Transformer](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation), transforms the message into one suitable for the samsungService flow. After successfully invoking the Samsung Web service, a [Session Variable Transformer](http://www.mulesoft.org/documentation/display/current/Session+Variable+Transformer+Reference) increments the session variable totalValue with the price returned by Samsung. Then, a new DataWeave building block transforms the response again for processing by the orderService flow. In case of an error, the flow creates a purchase receipt marked *REJECTED*. After processing in the flow is complete, the flow sends the information back to the orderService flow.

Notes:

- The application separates this processing in a separate flow rather than a subflow in order to limit the scope of our exception handling. (It is not possible to have an [Exception Strategy](http://www.mulesoft.org/documentation/display/current/Error+Handling) on a subflow.)
- The application uses a [Message Filter](http://www.mulesoft.org/documentation/display/current/Message+Filter) to throw an exception if the HTTP response code is anything other than 200 (success). Without it, the application would consider any HTTP response as successful, including errors such as a SOAP fault.

#### inhouseOrder Flow 

The inhouseOrder flow processes requests for all non-Samsung items.

The message source for this flow is a [JMS Inbound connector](http://www.mulesoft.org/documentation/display/current/JMS+Transport+Reference). The flow immediately initializes the flow variable price, then assigns to it the value returned by the in-house priceService flow. The inhouseOrder flow then saves this value to the company database using the [Database Connector](http://www.mulesoft.org/documentation/display/current/Database+Connector). The session variable totalValue holds the total price of this item. The last building block in the flow, a [Groovy Component](http://www.mulesoft.org/documentation/display/current/Groovy+Component+Reference), creates a purchase receipt with the relevant information.

Notes:

- This flow is transactional. It must not save data in the database if any errors occur in the life of the flow.
- The JMS connector is configured to "ALWAYS-BEGIN" the transaction, and the DB operation is set to "ALWAYS-JOIN" it.
- The Rollback Exception Strategy allows us to reinsert the message in the JMS queue in the event of an error.
- The *Redelivery exhausted* configuration allows us to determine what to do when the number of retries has reached the maximum specified in the maxRedeliveryAttempts attribute of the [Exception Strategy](http://www.mulesoft.org/documentation/display/current/Rollback+Exception+Strategy).
- Mule caches the price returned by the priceService flow in an in-memory [Object Store](http://www.mulesoft.org/documentation/display/current/Mule+Object+Stores). The key to the store is the ID of the product requested. The first time that a given product ID appears, the Enrich with price [Message Enricher](http://www.mulesoft.org/documentation/display/current/Message+Enricher) invokes the priceService to obtain the price for the product. After that, the flow uses the cached value for the product.
- A timeout can be configured on the object store used by the cache.

#### priceService Flow 
The inhouse RESTful priceService flow returns the price of non-Samsung products.

The HTTP Inbound connector passes the request to a Jersey backend REST Message Processor.

It's important to note that the [JAX-RS](http://en.wikipedia.org/wiki/Java_API_for_RESTful_Web_Services) annotated Java implementation is one way of implementing your Web service. A whole flow can serve as the implementation of a Web service, whether it's RESTful or SOAP-based.

#### samsungService Flow

The samsungService flow mocks the supposedly external Samsung Web service. 

This flow is sourced by the HTTP Inbound connector followed by a CXF Component configured as a JAX-WS Service. The service implementation is in the *Samsung Service Impl*, a [Java Component](http://www.mulesoft.org/documentation/display/current/Java+Transformer+Reference). 

#### auditService Flow 

The auditService flow, which is invoked asynchronously by the orderService flow, audits the item requests, which have been enriched with the responses from the inhouseOrder flow and the samsungOrder flow.

The auditService flow's transactional configuration is again XA due to the disparity between the VM Inbound connector and the Database Connector.

Notes:

- The source for the flow is a VM Inbound connector, in contrast to the JMS connector on the inhouseOrder flow. The reason is that the auditService flow invocation does not need to be synchronous, as is the case with the invocation for inhouseOrder. All transactional flows must be started by a one-way exchange pattern on the Inbound connector, which can be defined by using a *request-response* exchange pattern on the invoking service.
- In order to ensure reliable messaging (i.e., that messages are not lost in case processing stops due to an error), we wrap our Rollback Exception Strategy together with a sibling Catch Exception Strategy. These are both contained in a Choice Exception Strategy which defines which of them to use (whether Rollback or Catch Exception). If the Catch Exception Strategy is used, then the message is logged.

#### databaseInitialisation Flow

The databaseInitialisation flow initializes a local database to store any orders you place.

The databaseInitialisation flow initializes a local database to store any orders you place. As explained in [Set Up](http://www.mulesoft.org/documentation/display/current/Service+Orchestration+and+Choice+Routing+Example#ServiceOrchestrationandChoiceRoutingExample-SetUpandRuntheExample), you invoke this flow by pointing your Web browser to [http://localhost:8091/populate/](http://localhost:8091/populate/). Invoke this flow the first time you run the application; it is not necessary to do so in subsequent runs.

### Documentation
Anypoint Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to easily share your project with others outside the Studio environment, you can export the project's documentation to print, email, or share online. Studio's auto-generated documentation includes:

- a visual diagram of the flows in your application
- the XML configuration which corresponds to each flow in your application
- the text you entered in the Notes tab of any building block in your flow

Follow the [procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.

### Go Further 

- Learn more about about the [CXF Component](http://www.mulesoft.org/documentation/display/current/Service+Orchestration+and+Choice+Routing+Example#).
- Learn more about the [Choice Router](http://www.mulesoft.org/documentation/display/current/Choice+Flow+Control+Reference).
- Learn more about the [VM](http://www.mulesoft.org/documentation/display/current/VM+Transport+Reference) and [JMS](http://www.mulesoft.org/documentation/display/current/JMS+Transport+Reference) connectors. 
- Learn more about the [Database Connector](http://www.mulesoft.org/documentation/display/current/Database+Connector).
- Learn more about [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation).
- Learn more about the [Cache](http://www.mulesoft.org/documentation/display/current/Cache+Scope) and [Foreach](http://www.mulesoft.org/documentation/display/current/Foreach) scopes.

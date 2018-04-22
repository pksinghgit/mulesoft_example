# Foreach Processing and Choice Routing

This Mule ESB example application demonstrates the power of content-based routing in Web service integrations.

This application minimizes a borrower’s work in acquiring the best interest rate for a loan. This example should help you to make informed decisions about using the four elements in your Mule applications listed below. 

#### Content-Based Routing ####

Mule has the ability to intelligently route a message through different processing pathways according to its content. Using a Choice Router, Mule uses an expression to evaluate a message's properties, or part of its payload, then routes it to a specific pathway (i.e. series of message processors). Content-based routing, as this activity is called, dynamically applies routing criteria to a message at runtime.

#### Foreach Processing ####

An iterative processor, Foreach splits collections into elements, then processes them iteratively without losing any of the message payload. After Foreach splits a message collection and processes the individual elements, it returns a reference to the original message thus resulting in "Java in, Java out" processing.

#### Message Enriching ####

Mule uses Message Enrichers to enrich message payloads with data (i.e. add to the payload), rather than changing payload contents. Mule enriches a message’s payload so that other message processors in the application can access the original payload.

### Assumptions ###

This document assumes that you are familiar with Mule ESB and the [Anypoint™ Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial).

This document describes the details of the example within the context of Anypoint Studio, Mule ESB’s graphical user interface (GUI), and includes XML configuration details in separate tabs.

#### Example Use Case ####

This example application exposes a Web service that processes end user requests for interest rate quotes from banks. From a Web browser, an end user submits a request which contains the following:

- borrower’s name
- amount of the loan
- repayment term of the loan
- borrower’s social security number (SSN)

The application accesses other Web services to process the request, then responds to the end user. The response identifies the bank name and interest rate of the lowest quote.

The Web service uses five elements to maximize its processing efficiency:

- Choice Flow Controls to route messages based on content
- Transformers to convert message payloads’ data format
- Message Enrichers to add to, rather than change, message payloads
- Foreach to iteratively, and synchronously, process message payloads
- Dynamic connectors to send requests to different banks depending on the message payload

### Set Up and Run the Example ###

As with other [examples](https://www.mulesoft.com/exchange#!/?types=example), you can create template applications straight out of the box in **Anypoint Studio** or, in this case, also in **Mule Standalone** where this example is called **loanbroker-simple**. You can tweak the configurations of these use case-based examples to create your own customized applications in Mule.

Follow the procedure below to create, then run the Loan Broker application in Mule ESB.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!
1. Open your Web browser.
1. Type http://localhost:11081/?name=Muley&amount=20000&term=48&ssn=1234 into the address bar of your browser, then press enter to elicit a response from the application. 
2. In your browser’s address bar, replace the amount value with 5000, then press enter to elicit a new response from the application. 

### How it Works 

This example application consists of several [flows and subflows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture). These flows integrate Web services to process end user requests for interest rate quotes from banks.

The sections below offer flow-by-flow descriptions of the application’s actions as it processes end user requests.

#### loan-broker-sync Flow 

As the main flow in the application, this flow coordinates data collection among flows to produce an end user response.

The request-response [HTTP Inbound connector](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) in this flow receives an end user request. Because it has a request-response exchange pattern, this HTTP connector is responsible for both receiving and returning messages.

Next, the **Body to Parameter Map Transformer** converts the data format of the message payload from [HTTP body/data](http://en.wikipedia.org/wiki/HTTP_body_data) to a Java [map](http://en.wikipedia.org/wiki/Associative_array). The Loan Broker application only processes Java message payloads.

Then, Mule employs a content-based router to direct the message for further processing. The [Choice Router](http://www.mulesoft.org/documentation/display/current/Choice+Flow+Control+Reference) routes each message to one of two processing pathways according to its payload contents.

- If the message payload contains a complete request (i.e. the borrower’s name and SSN, and the amount and the term of the loan), the choice flow control passes the message to the create customer request **Expression Component**.
- If the message payload is an incomplete request, the choice flow control passes the message to the *set error message* expression component. This component sets the payload of the message to read *Error: incomplete request*. Mule processes the message no further. Instead, it responds to the end user with the error message. 

The create customer request component uses expressions to extract data from the message payload. It uses the data to create a new Java object with three values:

1. the Customer, which identifies both the borrower’s name and SSN
1. one Integer, which identifies the amount of the loan
1. a second Integer, which identifies the loan repayment term

		<expression-component doc:name="create customer request"><![CDATA[import org.mule.example.loanbroker.message.CustomerQuoteRequest;
		import org.mule.example.loanbroker.model.Customer;
		payload = new CustomerQuoteRequest(new Customer(payload.name,
		Integer.parseInt(payload.ssn)),
		Integer.parseInt(payload.amount),
		Integer.parseInt(payload.term));]]></expression-component>

With a new CustomerQuoteRequest object in its payload, the message encounters its first [Message Enricher](http://www.mulesoft.org/documentation/display/current/Message+Enricher). Throughout this flow, Mule enriches messages with data rather than changing the payload contents. By enriching a message, Mule preserves the payload content so that other elements in the application can access the original data.

The *Enrich with* *creditProfile* enricher contains only a [Flow Reference Component](http://www.mulesoft.org/documentation/display/current/Flow+Reference+Component+Reference). This type of component invokes other flows, or subflows, in the application to acquire, then add data to the message. In this case, the *lookupCustomerCreditProfile* component demands that the lookupCustomerCreditProfile subflow access an external Web service to acquire the borrower’s credit score. Mule enriches the message with the credit score, then passes the message to the next enricher in the flow.

As with its predecessor, the Enrich with Banks enricher uses a flow reference component to invoke a subflow and acquire data. In this case, instead of adding a credit score, Mule uses the result of the LookupBanks subflow to add a list of banks to the message payload.

Mule then uses a [Variable Transformer ](http://www.mulesoft.org/documentation/display/current/Variable+Transformer+Reference)to create an empty list variable. Mule will fill this empty quotes list variable with the quotes it fetches from banks. With an empty list to fill, the message next encounters a [Foreach](http://www.mulesoft.org/documentation/display/current/Foreach) scope. One by one, this iterative processor fetches data to populate each item on the list.

To fetch these data, the flow reference component first invokes the lookupLoanQuote subflow to acquire a quote from a bank. Then, the message enricher adds the quote to the list variable. Foreach continues to invoke, then enrich, until it has acquired a quote from each bank on the list of banks. Foreach then passes the message to the next [message processor](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials) in the flow.

To illustrate foreach’s behavior with an example, imagine a message payload with the following contents:

- an empty quotes list variable
- a banks list variable naming two banks from which Mule must request a quote: MultiNational Bank and IndustrialGrowth Bank

Foreach processes the message payload as follows:

1. Foreach consults the banks list variable to learn that it should send its first request to MultiNational.
1. Foreach invokes the lookupLoanQuote subflow.
1. The lookupLoanQuote subflow calls the getLoanQuote Web service to obtain an interest rate quote from MultiNational.
1. The lookupLoanQuote subflow provides the Web service response to the loan-broker-sync flow.
1. The message enricher inserts the interest rate quote from MultiNational into the quotes list variable.
1. Foreach consults the banks list variable to learn that it should send its second request to IndustrialGrowth.
1. Foreach invokes the lookupLoanQuote subflow.
1. The lookupLoanQuote subflow calls the getLoanQuote Web service to obtain an interest rate quote from IndustrialGrowth.
1. The lookupLoanQuote subflow provides the Web service response to the loan-broker-sync flow.
1. The message enricher inserts the interest rate quote from IndustrialGrowth into the quotes list variable.
1. Foreach consults the banks list variable to find no more items on the list. It passes the message — now with a list containing two interest rate quotes — to the next message processor. 

The penultimate message processor in this flow references yet another subflow in the application. The *findLowestLoanQuote* subflow determines which quote in the list is the lowest, then logs the result in the message payload.

Finally, the **Object to String Transformer** converts the message payload’s data format from Java to a string. The HTTP connector sends the response to the end user.

Notice that the loan-broker-sync flow also contains a [Catch Exception Strategy](http://www.mulesoft.org/documentation/display/current/Catch+Exception+Strategy). Rather than use Mule’s [default exception strategy](http://www.mulesoft.org/documentation/display/current/Error+Handling), this flow uses a customized exception strategy to handle errors. If an error occurs in the flow, the exception strategy’s Set **Payload Transformer** sets an error message on the payload. The application sends this error message, which reads, *Error processing loan request*, as a response to the end user.

#### lookupCustomerCreditProfile Subflow ####

Invoked upon demand by the loan-broker-sync flow, this subflow acquires and logs the borrower’s credit score on the message payload.

To acquire the credit score, the customer transformer sets the payload to *Customer*, as defined by the *create customer request* expression transformer. (Recall that the Customer variable contains the borrower’s name and SSN.) Mule sends a request to the *getCreditProfile* SOAP Web service. The HTTP connector inserts the Web service’s response into the subflow.

Mule leverages [Apache’s CXF framework](http://cxf.apache.org/) to build Web services. The Processor Chain that wraps the [CXF Component](http://www.mulesoft.org/documentation/display/current/CXF+Component+Reference) and HTTP outbound connector is a CXF requirement. It ensures that Mule completes all processing activities prior to logging the processing result.

Last in this flow, the [Logger Component](http://www.mulesoft.org/documentation/display/current/Logger+Component+Reference) logs the payload of the Web service’s response on the message payload as the *Credit Profile*.

#### lookupBanks Subflow ####

The application prevents exposing all banks to all loan quote requests. A bank that caters to premiere clients, for example, would be irked to receive a request for a quote for a small loan from a borrower with poor credit. To prevent such irksome calls to banks’ Web services, the Loan Broker application employs the **LookupBanks** subflow.

Mule first uses a choice flow control to examine the amount in the payload, then routes the message according to the size of the loan.

- If the loan is more that $20,000, the flow control routes the message to the first expression component, labeled Bank 1, Bank 2.
- If the loan is more than $10,000, the flow control routes the message to the second expression component, labeled Bank 3, Bank 4.
- if otherwise (i.e. if the loan is $10,000 or less), the flow control routes the message to the third expression component, labeled Bank 5. 

Note that the choice flow control directs the message to the first expression that evaluates to true. For example, it directs a quote request for a loan of $30,000 only to the *Bank 1, Bank 2* component.

Each expression component in this subflow contains the URIs of the banks willing to provide an interest rate quote. For example, messages that pass into the *Bank 3, Bank 4* component earn, as a payload addition, the URIs for Banks 3 and 4. The banks Logger component records the list of appropriate banks to which to send a request.

#### lookupLoanQuote Subflow ####

This sends a quote request to banks' Web services.

First, the variable transformer stores the Mule message payload — the bank’s URI — as a variable named bankUri. (Recall that this subflow receives requests one at a time from foreach in the Loan-broker-sync flow. Each request's payload a the URI of a bank.)

The *create* *LoanBrokerLoanRequest* component uses expressions to extract the borrower’s credit profile (logged by the *creditProfile* logger in the LookupCustomerCreditProfile flow) from the message payload. It uses the data to create a request to send to the getLoanQuote Web service. Mule uses a CXF component configured as a JAXWS-client to send the request to a bank's Web service. The HTTP outbound connector dynamically determines where to send the request based on the bank's URI in the message payload. It receives the response from the banks’ Web service and pushes the response payload to the *quote* logger to record.

#### findLowestLoanRequest Subflow ####

This simple subflow uses an expression component to determine which item, in the list of quotes, offers the lowest interest rate. The Logger records the result.
The expression in the component compares the getInterestRate of items in the list to each other to determine which one is the lowest.

#### Mock Flows ####

The remaining six flows in the application are “mock flows.” They act as external Web services to which the five legitimate flows and subflows call to request data.

Each flow contains:

- a request-response HTTP connector and CXF component to receive the requests
- a [Java Component](http://www.mulesoft.org/documentation/display/current/Java+Component+Reference) which produces random data to mimic Web service processing

You do not need to include these flows your customized application; they exist only to support a functional example.

#### Go Further ####

- For more information on routing messages, see [Choice Router](http://www.mulesoft.org/documentation/display/current/Choice+Flow+Control+Reference).
- For more information on enriching messages, see [Scopes](http://www.mulesoft.org/documentation/display/current/Scopes).
- For more information on setting variables on messages, see the [Variable Transformer Reference](http://www.mulesoft.org/documentation/display/current/Variable+Transformer+Reference).
- For more information on iterative processing, see [Foreach](http://www.mulesoft.org/documentation/display/current/Foreach).
- For more information on applying exception strategies to flows, see [Error Handling](http://www.mulesoft.org/documentation/display/current/Error+Handling).
- For more information on configuring a CXF component, see [CXF Component Reference](http://www.mulesoft.org/documentation/display/current/CXF+Component+Reference).

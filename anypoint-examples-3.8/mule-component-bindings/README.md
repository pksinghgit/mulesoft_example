# Component Bindings #

Components can use bindings to call an external service during execution. The bindings used with a Java component bind a Java interface, or single interface method, to an outbound endpoint. The external service to be called should implement the same interface, and the component should encapsulate a reference to that interface, which is initialized during the bootstrap stage by the Mule configuration builder. The reference will be initialized using a reflective proxy class.

### Assumptions ###

This document describes the details of the example within the context of **Anypoint™ Studio**, Mule ESB’s graphical user interface (GUI). Where appropriate, the XML configuration is provided.

This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Mule Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture) and [Mule Global Elements](http://www.mulesoft.org/documentation/display/current/Global+Elements).

### Example Use Case ###

This example demonstrates integration of external services (Stocklytics, Sentiment140) and a binding of another external service (Twitter) to the local interface. Stocklytics is used to retrieve stock data, Twitter to execute the search for tweets about the given stock on the given date and Sentiment140 to classify tweets to positive, negative or neutral groups.

### Set Up and Run the Example ###

Complete the following procedure to create, then run this example in your own instance of Anypoint Studio. Skip ahead to the next section if you prefer to simply examine this example.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). *Do not run the application*.
2. Acquire **stocklyticsApiKey** by registering at [http://developer.stocklytics.com/signup/](http://developer.stocklytics.com/signup/).
3. Acquire **sentiment140ApiKey** by registering at [http://help.sentiment140.com/api/registration](http://help.sentiment140.com/api/registration). It is the email you used for the registration.
4. Register at [http://dev.twitter.com/](http://dev.twitter.com/) and navigate to [https://apps.twitter.com/](https://apps.twitter.com/) to create a new app. Then click **API Keys** tab and get:
    
		twitter.accessKey=Access token
	    twitter.accessSecret=Access token secret
	    twitter.apiKey=API key
	    twitter.apiSecret=API secret
5. In the **Package Explorer**, right-click the component-bindings project name, then select **Run As > Mule Application**. Studio runs the application on the embedded server.
6. Open your browser and put [http://localhost:8180/api/stockStats?stock=AAPL&date=2012-11-28](http://localhost:8180/api/stockStats?stock=AAPL&date=2012-11-28) to the address bar.
7. The output in the browser contains the formatted processed data from external sources.  

### How it Works ###

An [HTTP endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) is responsible for receiving requests and sending response to the end user. [REST Component](http://www.mulesoft.org/documentation/display/current/REST+Component+Reference), that is binded to the resource which implements the local interface and initiates the query processing, follows. It starts the second flow which main purpose is to perform Twitter search operation and to return the search result. The flow contains two custom processors: *TwitterRequestProcessor* for preparing a request message and *TwitterResponseProcessor* for processing a response. 

The following steps outline the process to build this application. 

1. Open **Global Elements** Tab and configure a Bean using following properties:

		ID			stackStatsResource
		Name 		Bean
		Class 		stockstats.impl.StockStatsResourceImpl
		Scope		prototype
	
1. Add a property in the **Bean Subelements** Section and name it *stockService*.
2. Double click the bean property to edit it. In the **Advanced** tab click **Add Bean** and set the class to *stockstats.impl.stocklytics.StocklyticsStockService*. Add a Constructor-Arg element and add a parameter. Set it to *stocklyticsApiKey* acquired from the previous section.
1. Add a property in the **Bean Subelements** Section and name it *sentimentService*.
2. Double click the bean property to edit it. In the **Advanced** tab click **Add Bean** and set the class to *stockstats.impl.sentiment140.Sentiment140SentimentService*. Add Constructor-Arg and add a parameter at index 0 and set it to *sentiment140ApiKey* acquired from the previous section.
2. Go the **Message Flow** tab and drag an HTTP Component to the flow and configure it:

		Display Name	Receive HTTP Requests From Apps
		Host			localhost
		Port			8180
		Path	 		api	
	Name the flow as **StockServiceFlow**.
2. Add a **REST Component** to the flow and edit it in the XML View as follows:
	
		<jersey:resources doc:name="REST">
			<component >
	        	<spring-object bean="stackStatsResource" />
	        	
	        	<binding interface="stockstats.impl.TwitterService" method="search">
	        		<vm:outbound-endpoint exchange-pattern="request-response"
	        				path="twitterFlowRequest" />
	        	</binding>
	        </component>
		</jersey:resources>

3. Add a flow and name it **TwitterFlow**.
4. Start this flow by adding a **VM Connector** and cofingure it as shown here:

		Exchange pattern	request-response 
		Queue path			twitterFlowRequest
5. Add a **Custom Processor** after the VM Connector by pasting this XML code:

		<custom-processor class="stockstats.impl.mule.TwitterRequestProcessor" 
        		name="TwitterRequestProcessor" 
        		doc:name="TwitterRequestProcessor"/>
6. Add a **Twitter Connector** to the flow. Click + sign next to the Connector Configuration and configure it using the data obtained from Twitter service described in the previous section:

		AccessKey		twitter.accessKey
		AccessSecret	twitter.accessSecret
		ConsumerKey		twitter.apiKey 
		ConsumerSecret	twitter.apiSecret 
	And the very connector using the following properties:
	
		Operation	Search
		Query 		#[header:invocation:stock] 
		Since 		#[header:invocation:since]
7. Add another **Custom Processor** by pasting this XML code:

		<custom-processor class="stockstats.impl.mule.TwitterResponseProcessor" 
        		name="TwitterResponseProcessor" 
        		doc:name="TwitterResponseProcessor"/>

8. Add a **Logger Component** that outputs *#[payload]* to finish the flow.
9. Now you have a functional flow that binds an external service to the local interface.

### Documentation ###

Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to share your project with others outside the Studio environment, you can export the project's documentation to print, email or share online. Studio's auto-generated documentation includes:

- A visual diagram of the flows in your application
- The XML configuration which corresponds to each flow in your application
- The text you entered in the Notes tab of any building block in your flow

Follow [the procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.

### Go Further ###

- For more information on using REST, see [REST Component Reference](http://www.mulesoft.org/documentation/display/current/REST+Component+Reference).
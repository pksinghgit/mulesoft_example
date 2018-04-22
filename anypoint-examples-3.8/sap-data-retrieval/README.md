# SAP data retrieval Example

This example application illustrates how to use Mule ESB to build a simple HTTP application using APIKit to fetch data from SAP. After reading this document, creating and running the example in Mule, you should be able to leverage what you have learned to create an HTTP request-response application that is able to retrieve requested data from Salesforce instance based on your criteria.

This example can also be used while configuring any of our [Anypoint SAP Templates](https://www.mulesoft.com/library#!/?filters=SAP) for retrieving Customers / Materials / Sales orders data

### Assumptions

This document describes the details of the example within the context of Anypoint Studio, Mule ESB graphical user interface (GUI). This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Mule Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial).

### Example Use Case

This application employs APIKit Router component to route HTTP requests to exact flow defined by resource and method. Afterwards, the flows prepare XML request for SAP connector using the HTTP input parameters. The response is transformed to the JSON and returned to the user.

### Set Up and Run the Example

As with other [examples](https://www.mulesoft.com/exchange#!/?types=example), you can create template applications straight out of the box in Anypoint Studio. You can tweak the configurations of these use case-based examples to create your own customized applications in Mule.

Follow the procedure below to create, then run the **SAP data retrieval** application.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).
2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081
3. Go to Global Elements and open SAP Connector element. Fill in your SAP credentials.
4. Run the application.
The APIKit console should start and the user can choose which endpoint wants to hit. The main
	+	GET /customers 	- return customers data from SAP instance according the specified parameters
						- user are able to define 2 HTTP query parameters - max_count, name
		+ max_count - defining the maximum count of the retrieved customers
		+ name - defining the String that the customer's name have to start with
	+	GET /materials	- return materials data from SAP instance according the specified parameters
						- user are able to define 2 HTTP query parameters - max_count, name
		+ name - defining the String that the material's name have to start with
		+ max_count - defining the maximum count of the retrieved materials
	+	GET /salesOrders	- return sales orders data from SAP instance according the specified parameters
							- user should define 2 HTTP query parameters -customer\_number, sales_organization
		+ customer_number 		- specifying the customer number for sales orders
      	+ sales_organization	- specifying the sales organuzation for sales orders 

5. Click on the resource which yhou want to use, specify the parameters and click on *GET* button.
6. You will see the retrieved data structure. If the data in SAP instance does not match your criteria, "No data found" message is returned,

### How it Works

The **SAP data retrieval** example application contains four [flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture) and one [exception strategy](https://docs.mulesoft.com/mule-user-guide/v/3.7/choice-exception-strategy). The main flow receive user HTTP requests and using the [APIKit router](https://docs.mulesoft.com/anypoint-platform-for-apis/apikit-basic-anatomy) process them to the right flow or exception strategy.

The sections below elaborate further on the configurations of the application and how the flow works to respond to end user requests.

### get:/customers:sap-api-config

This flow is responsible for displaying a customers data from SAP system. 
At the beginning, using the [DataWeave transformer](https://docs.mulesoft.com/mule-user-guide/v/3.7/dataweave-reference-documentation)the XML request from the input parameters is prepared. 
Next, the [SAP connector](https://docs.mulesoft.com/mule-user-guide/v/3.7/sap-connector) is used to retrieve the data using the standart BAPI - BAPI_CUSTOMER_FIND. 
Finally, he response from SAP system is transformed by DataWeave transformer into JSON structure and sent back to end user. 

### get:/salesOrders:sap-api-config

This flow is responsible for displaying a sales orders data from SAP system. 
At the beginning, using the [DataWeave transformer](https://docs.mulesoft.com/mule-user-guide/v/3.7/dataweave-reference-documentation)the XML request from the input parameters is prepared. 
Next, the [SAP connector](https://docs.mulesoft.com/mule-user-guide/v/3.7/sap-connector) is used to retrieve the data using the standart BAPI - BAPI_SALESORDER_GETLIST.
Finally, he response from SAP system is transformed by DataWeave transformer into JSON structure and sent back to end user. 

### get:/materials:sap-api-config

This flow is responsible for displaying a materials data from SAP system. 
At the beginning, using the [DataWeave transformer](https://docs.mulesoft.com/mule-user-guide/v/3.7/dataweave-reference-documentation)the XML request from the input parameters is prepared. 
Next, the [SAP connector](https://docs.mulesoft.com/mule-user-guide/v/3.7/sap-connector) is used to retrieve the data using the standart BAPI - BAPI_MATERIAL_GETLIST.
Finally, he response from SAP system is transformed by DataWeave transformer into JSON structure and sent back to end user. 

### sap-api-apiKitGlobalExceptionMapping
This is the default exception mapping generated by the APIKit component. It ensures that the right status code with appropriate message is shown to end user. Here are the list of defined status codes with their messages:

+ 404 - Resource not found
+ 405 - Method not allowed
+ 415 - Unsupported media type
+ 406 - Not acceptable
+ 400 - Bad request

### Go Further

- Learn more about the [HTTP endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector).
- Learn more about the [Mule Expression Language](http://www.mulesoft.org/documentation/display/current/Mule+Expression+Language+MEL) 
- Learn more about the [SAP component](https://docs.mulesoft.com/mule-user-guide/v/3.7/sap-connector)
- Learn more about the [Error Handling](http://www.mulesoft.org/documentation/display/current/Error+Handling)
- Learn more about the [APIKit router](https://docs.mulesoft.com/anypoint-platform-for-apis/apikit-tutorial)

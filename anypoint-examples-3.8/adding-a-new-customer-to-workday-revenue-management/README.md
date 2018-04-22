#Adding a new customer to Workday Revenue Management

The Workday Anypoint Connector facilitates connections between Mule integration applications and Workday by making API calls to the Workday Web Services. This example shows you how to use this connector in an integration application where a customer has to be added to the Workday Revenue Management Console.

### Assumptions ###

This document assumes that you are familiar with Mule and the [Anypoint™ Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture), [Mule Global Elements](http://www.mulesoft.org/documentation/display/current/Global+Elements), and [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation). 

This document describes the details of the example within the context of Anypoint Studio, Mule ESB’s graphical user interface.

### Example Use Case ###

The application accepts an XML code which containing the required customer information (customer name, status and category). It parses this XML using the DataWeave transformer and creates a new customer record in a Workday instance.

### Set Up and Run the Example ###

Complete the following procedure to create, then run this example in your own instance of Anypoint Studio.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). Do not run the application.
2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 9090
3. In your application in Studio, click the **Global Elements** tab. Double-click the Workday Connector global element to open its **Global Element Properties** panel. Change the contents of the **user**, **password** **tenant**and **hostname** fields to your account-specific values as follows:

		user						<USER>
		tenant						<TENANT ID> - It usually ends with _pt1
		password					<PASSWORD>
		hostname					<HOST NAME OF ONE OF THE WORKDAY CLOUD SERVERS> - for example impl-cc.workday.com

	Then click **OK** to save your changes. 
4. In the **Package Explorer**, right-click the adding-a-new-customer-to-workday-revenue-management project name, then select **Run As > Mule Application**. Studio runs the application on the embedded server.  
5. Make an HTTP POST request to *http://localhost:9090/* with the 'Content-Type' header set as 'application/xml' and the request body as follows:

		<?xml version="1.0" encoding="UTF-8"?>
		<root>
			<Account>
				<CustomerName>John Doe</CustomerName>
				<BusinessEntityName>John Doe</BusinessEntityName>
				<Customer_Category_Reference_Type>Customer_Category_ID</Customer_Category_Reference_Type>
				<Customer_Category_Reference_Value>CUSTOMER_CATEGORY-5</Customer_Category_Reference_Value>
				<Customer_Status_Reference_Type>Business_Entity_Status_Value_ID</Customer_Status_Reference_Type>
				<Customer_Status_Reference_Value>ACTIVE</Customer_Status_Reference_Value>
			</Account>
		</root>

	To send this request, use a browser extension such as [Advanced Rest Client](https://chrome.google.com/webstore/detail/advanced-rest-client/hgmloofddffdnphfgcellkdfbfbjeloo) (Google Chrome), or the [curl](http://curl.haxx.se/) command line utility. 
6. Login to your Workday account. Navigate to View Customer Report and enter John Doe in the search field. Verify that the customer was added.

### How it Works ###

Using a single flow with four elements, this application accepts XML with the customer information, then uploads a customer to Workday. 

The [HTTP connector](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) listens to POST requests at the predefined URL. When such request arrives, it passes the content to the [Anypoint DataWeave transformer](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation). This transformer converts the format of the data from XML to a XML required for the Workday request. After data conversion, the application uses the [Workday Connector](http://www.mulesoft.org/documentation/display/current/Workday+Connector) to push data into your Workday system. The connector's configuration specifies the **operation** – *Put customer*. 

### Documentation ###

Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to share your project with others outside the Studio environment, you can export the project's documentation to print, email or share online. Studio's auto-generated documentation includes:

- A visual diagram of the flows in your application
- The XML configuration which corresponds to each flow in your application
- The text you entered in the Notes tab of any building block in your flow

Follow [the procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.

### Go Further ###

- Learn more about [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation).
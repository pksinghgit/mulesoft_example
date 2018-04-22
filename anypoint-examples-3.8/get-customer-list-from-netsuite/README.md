# Retrieving a List of Customers from NetSuite

Anypoint Connector for NetSuite synchronizes data and automates business processes between NetSuite and third party applications, either on-premise or in the cloud. This example shows you how to use the NetSuite connector in an Anypoint integration application.

### Assumptions ###

This document assumes that you are familiar with Mule 101 and the [Anypoint™ Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture) and [Mule Global Elements](http://www.mulesoft.org/documentation/display/current/Global+Elements).


### Example Use Case ###

This application connects to NetSuite to retrieve a list of customers based on the predefined criteria (all customers that have a last name that starts with 'a'). The data is then parsed and formatted to improve readability and is displayed in the web browser for the end user. 

### Set Up and Run the Example ###

Complete the following procedure to create, then run this example in your own instance of Anypoint Studio. 

1.  Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). *Do not run the application*.
2. Replace *http.port* parameter in *http:listener-config* element with the valid value (for example: 8081). 
1. Log in to your NetSuite account. You will need the login email (*netsuite.email*) and the password (*netsuite.password*) to connect to NetSuite using the connector later on. 
2. Choose **Setup > Integration > Web Services Preferences** from the main menu. Store the value *ACCOUNT ID* that will be used as a *netsuite.account* parameter. 
3. On the same page, if not present, insert a row into the table with your Name and a Web Services Default Role. Store the Role ID that will be used as a *netsuite.roleId* parameter. 
1. In your application in Studio, click the **Global Elements** tab. 
1. Double-click the NetSuite global element to open its **Global Element Properties** panel. Configure it as follows:

		Email			netsuite.email
		Password		netsuite.password
		Account			netsuite.account
		Role Id			netsuite.roleId


	You should verify the configuration by clicking Test Connection... button. Alternatively, you can also configure the global element in the XML Editor.
1. In the **Package Explorer**, right-click the get-customer-list-from-netsuite project name, then select **Run As > Mule Application**. Studio runs the application on the embedded server.
2. Hit your web browser with the following url: *http://localhost:8081/customers?lastName=a* to retrive a list of all customers having a last name starting with *a*. The data is stored in the HTML table.  
1. Stop the Mule application by clicking the square, red terminate button in the **Console**.

### How it Works ###

Using a single flow, this application accepts incoming HTTP requests, performs a query in NetSuite and delivers results to the end user. 

The [HTTP connector](http://www.mulesoft.org/documentation/display/current/File+Connector) listens at *http://localhost:8081/customers* to incoming HTTP Get requests. The dynamic part of the NetSuite customer query is extracted from the url under *lastName* parameter key using [Mule Expression Language](http://www.mulesoft.org/documentation/display/current/Mule+Expression+Language+MEL). Next, the MEL Expression block iterates over the returned collection and prepares an HTML table body that is sent to a [Parse Template](http://www.mulesoft.org/documentation/display/current/Parse+Template+Reference) component that injects it into the HTML template.

### Documentation ###

Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to share your project with others outside the Studio environment, you can export the project's documentation to print, email or share online. Studio's auto-generated documentation includes:

- A visual diagram of the flows in your application
- The XML configuration which corresponds to each flow in your application
- The text you entered in the Notes tab of any building block in your flow

Follow [the procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.

### Go Further ###

- Learn more about [Connection Testing](http://www.mulesoft.org/documentation/display/current/Testing+Connections).
- Learn more about [NetSuite Connector](http://www.mulesoft.org/documentation/display/35X/NetSuite+Connector#NetSuiteConnector-Assumptions)
- Learn more about [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation).	

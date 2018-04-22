# Salesforce data synchronization using Watermarking and Batch processing

This example illustrates the concept of data synchronozation using Watermarking.

### Watermarking ###

The concept of Watermark refers to a flood after-match in which you look at the water stains in a wall to figure how high the water got, which is what we want to do in this use case: figure out which was the last item we updated and move from there on. 

### Assumptions ###

This document describes the details of the example within the context of **Anypoint™ Studio**, Mule ESB’s graphical user interface (GUI). Where appropriate, the XML configuration is provided.

This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Mule Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture) and [Mule Global Elements](http://www.mulesoft.org/documentation/display/current/Global+Elements).

### Example Use Case ###

In this example, watermarking feature is used to periodically make pull requests to the data store to retrieve only records that were modified since the last request. Specifically, Salesforce CRM is queried for contacts that were recently modified and just prints them in the console.

### Set Up and Run the Example ###

Complete the following procedure to create, then run this example in your own instance of Anypoint Studio. Skip ahead to the next section if you prefer to simply examine this example.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). *Do not run the application*.
1. Log in to your Salesforce account. From your account menu (your account is labeled with your name), select **Setup**.
1. In the left navigation bar, under the **Personal Setup** heading, click to expand the **My Personal Information** folder. 
1. Click **Reset My Security Token**. Salesforce resets the token and emails you the new one.
1. Access the email that Salesforce sent and copy the new token onto your local clipboard.
1. In your application in Studio, click the **Global Elements** tab. 
1. Double-click the Salesforce global element to open its **Global Element Properties** panel. In the **Security Token** field, paste the new Salesforce token you copied from the email. Alternatively, configure the global element in the XML Editor.
1. Change the contents of the **Username** and **Password** fields to your account-specific values, then click **OK** to save your changes. 
1. In the **Package Explorer**, right-click the connect-with-salesforce project name, then select **Run As > Mule Application**. Studio runs the application on the embedded server.  
1. In your browser, access your Salesforce account, then navigate to the **Contacts** tab.
2. Use the drop-down menu to display** All Contacts**, then select one contact and edit (e.g. change the first name).
3. Go back to Anypoint Studio and click the console view and search for a log similar to this one:
 	
		INFO  2014-07-04 08:17:01,502 [batch-job-watermarkingBatch1-work-manager.05] org.mule.api.processor.LoggerMessageProcessor: contact recently modified: {Name=Avi123 Green, LastModifiedDate=2014-07-04T06:16:54.000Z, Id=0032000001DpkrEAAR, type=Contact}
1. Stop the Mule application by clicking the square, red terminate button in the **Console**.


### How it Works ###

Using a single flow, this application queries Salesforce CRM for recently modified contacts and prints them in the console view.

The [Poll Component](http://www.mulesoft.org/documentation/display/current/Poll+Reference) periodically invokes [Salesforce Connector](http://www.mulesoft.org/documentation/display/current/Salesforce+Connector+Reference) to perform a query in your Salesforce account: to get *Id,Name* and *LastModifiedDate* attributes of Contact objects that were modified since the last polling. The polling is set to be executed every 10 seconds right from the start but can be easily changed by accessing component configuration. The polling is executed in the Input Phase of Batch Processing. To keep the example simple, Process Records Phase only prints the obtained records to the console.

The following steps outline the process to build this application. 

1. Drop a Batch Scope in your application.
2. Drop a Poll Component in the Input Phase of the Batch enabling the polling mechanism. Set Frequency and Time units to 10 seconds or any time period you wish. Check the Enable watermarking checkbox, set Variable Name to *timestamp* and Default Expression to:

		YESTERDAY

	or to precise time in the following format:

		2015-06-17T10:00:00.000Z

3. Check the Selector checkbox to be able to select how the watermarking variable will be updated. Select *MAX* so the watermarking process updates it to the latest time value. Set the Selector Expression to #payload.LastModifiedDate].
4. Next, put a Salesforce Connector into the Poll Component. At this point, you can configure the connector with your Salesforce account-specific details and test the connection to Salesforce. 
3. When you click Test Connection, Mule tests the connection to Salesforce. With a valid username, password and security token, the connection test results in success and Mule saves your global element configurations. If any of the values are invalid, the connection test results in failure, and Mule does not save the global element, prompting you to correct the invalid configurations.
4. Back in the Salesforce connector properties editor, choose **Query** operation from the dropdown menu and set Query Text to
 
		SELECT Id,Name,LastModifiedDate FROM Contact WHERE LastModifiedDate > #[flowVars['timestamp']]
3. Add a Logger Component to the Process Records Phase of the batch and set Message to 
	
		Contact recently modified: #[payload]
4. The configuration now complete, you can save, then run the application. Modify an existing contact in your Salesforce account and watch it appear in the console.

### Documentation ###

Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to share your project with others outside the Studio environment, you can export the project's documentation to print, email or share online. Studio's auto-generated documentation includes:

- A visual diagram of the flows in your application
- The XML configuration which corresponds to each flow in your application
- The text you entered in the Notes tab of any building block in your flow

Follow [the procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.

### Go Further ###

- For more information on the Salesforce Connector, see [Salesforce Connector Reference](http://www.mulesoft.org/documentation/display/current/Salesforce+Connector+Reference).
- For more information on Batch processing, see [Batch processing](http://www.mulesoft.org/documentation/display/current/Batch+Processing+Reference).

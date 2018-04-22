# Import contacts into Microsoft Dynamics CRM

This application uses pre-packaged tools to intelligently connect with Microsoft Dynamics CRM. Based on a simple use case, the application takes a CSV file of contacts and uploads the contact information to an active Dynamics CRM user account. It uses the Anypoint DataWeave Transformer to map and transform data, thereby facilitating quick integration with this Software as a Service (SaaS) provider.

### Connect with Microsoft Dynamics CRM ###

At times, you may find that you need to connect one or more of your organization's on-premises systems with a SaaS such as Microsoft Dynamics. Ideally, these independent systems would talk to each other and share data to enable automation of end-to-end business processes. Use Mule applications to facilitate communication between your on-prem system(s) and Microsoft Dynamics. (Though this use case does not extend as far, you can also use Mule to facilitate communication between SaaS providers).

**Note:** You need to install Java Cryptography Extensions to be able to connect to MS Dynamics. Please [choose](http://www.oracle.com/technetwork/java/javase/downloads/index.html) a relevant version according to your Java installation.

### Assumptions ###

This document assumes that you are familiar with Mule and the [Anypoint™ Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture), [Mule Global Elements](http://www.mulesoft.org/documentation/display/current/Global+Elements), and [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation).

This document describes the details of the example within the context of Anypoint Studio, Mule ESB’s graphical user interface.

**Note:** Be sure to have Microsoft Dynamics CRM connector installed in your Anypoint Studio.

### Example Use Case ###

The application accepts a CSV file by polling a local folder frequently. The CSV file contains contact information with columns as follows: first name, surname, phone number and email. These columns are mapped to each of the respective fields in a specific Dynamics CRM account and the rows are uploaded.

### Set Up and Run the Example ###

Complete the following procedure to create, then run this example in your own instance of Anypoint Studio. 

1. Create your free trial MS Dynamics account [here](http://www.microsoft.com/en-us/dynamics/crm-free-trial-overview.aspx). Remember your registration data as you will need it to connect to Dynamics, namely: a username, a password and a company name.
1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). *Do not run the application*.
1. In your application in Studio, click the **Global Elements** tab. 
1. Double-click the MS Dynamics CRM global element to open its **Global Element Properties** panel. 
1. Change the contents of the **Username**, **Password** and **Organization Service Url** fields to your account-specific values as follows:

		Username					<USERNAME>@<COMPANY_NAME>.onmicrosoft.com
		Password					<PASSWORD>
		Organization Service Url	https://<COMPANY_NAME>.api.crm4.dynamics.com/XRMServices/2011/Organization.svc
 
**Note:** The Url might differ depending on your location. Choose an appropriate one from [For Microsoft Dynamics CRM Online](https://msdn.microsoft.com/en-us/library/gg309401.aspx) section.

4. Then click **OK** to save your changes. 
1. In the **Package Explorer**, right-click the connect-with-ms-dynamics project name, then select **Run As > Mule Application**. Studio runs the application on the embedded server.  
1. This project includes a sample CSV file, called *contacts.csv*, that you can use to witness end-to-end functionality of the application. In the **Package Explorer**, click the *src/main/resources* folder to expand it, then find the *contacts.csv* file inside this folder.
1. Click and drag the *contacts.csv* file into an *input* folder in the same directory.
1. The File connector in the application polls the input folder every ten seconds. It picks up the CSV file, processes it, then deposits it into the output folder in the same directory. (Hit **F5** to refresh the contents of the input and output folders.)
1. In your browser, access your Dynamics account, then navigate from **Sales** menu to the **Contacts** tab.
1. Use the drop-down menu to display **All Contacts**, then scan your contacts for two new entries:  
	- John Doe
	- Jane Doe
1. Stop the Mule application by clicking the square, red terminate button in the **Console**.
1. Delete the two sample contacts from your MS Dynamics account.

### How it Works ###

Using a single flow with four elements, this application accepts CSV files which contain contact information, then uploads the contacts to MS Dynamics. 

The [File connector](http://www.mulesoft.org/documentation/display/current/File+Connector) polls the input folder for new files every ten seconds. When it spots a new file, it reads it and passes the content to the [Anypoint DataWeave transformer](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation). This transformer not only converts the format of the data from CSV to a collection, it automatically maps the input fields from the CSV file – firstname, surname, etc. – to output fields that MS Dynamics uses in a collection. When it has converted all the contacts in the file to a collection of MS Dynamics-friendly data, the application uses a [MS Dynamics Connector](https://www.mulesoft.com/resources/esb/ms-dynamics-integration) to push data into your MS Dynamics account. The connector's configurations specify the **operation** – *Create* – and the **Logical name** – *contact* – which dictate exactly how the data uploads to MS Dynamics; in this case, it creates new contacts. 

### Documentation ###

Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to share your project with others outside the Studio environment, you can export the project's documentation to print, email or share online. Studio's auto-generated documentation includes:

- A visual diagram of the flows in your application
- The XML configuration which corresponds to each flow in your application
- The text you entered in the Notes tab of any building block in your flow

Follow [the procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.

### Go Further ###

- Learn more about [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation).	

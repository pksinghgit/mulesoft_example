# Importing a CSV file into Microsoft Sharepoint ###

This application uses pre-packaged tools to intelligently connect with Microsoft Sharepoint Online. Based on a simple use case, the application takes a CSV file of contacts and uploads it to Sharepoint Online instance.

### Assumptions ###

This document assumes that you are familiar with Mule and the [Anypoint™ Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture), [Mule Global Elements](http://www.mulesoft.org/documentation/display/current/Global+Elements), and Studio's [File connector](http://www.mulesoft.org/documentation/display/current/File+Connector) and [Flow Variable](https://developer.mulesoft.com/docs/display/current/Variable+Transformer+Reference)

This document describes the details of the example within the context of Anypoint Studio, Mule ESB’s graphical user interface.

**Note:** Be sure to have Microsoft SharePoint 2013 connector installed in your Anypoint Studio.

### Example Use Case ###

Through a simple example, this application employs complex functionality to demonstrate a basic use case. The application accepts CSV file and uploads it into a MS Sharepoint Online instance under the specified folder. If the folder hasn't existed already, it is created before uploading the CSV file.

### Set Up and Run the Example ###

Complete the following procedure to create, then run this example in your own instance of Anypoint Studio. 

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). Do not run the application.
2. In your application in Studio, click the **Global Elements** tab.
3. Double-click the Microsoft SharePoint 2013: Online Connection global element to open its **Global Element Properties** panel. 
4. Change the contents of the **Username**, **Password** and **Site Url** fields to your account-specific values as follows:

		Username					<USERNAME>@<COMPANY_NAME>.onmicrosoft.com
		Password					<PASSWORD>
		Site Url					https://<COMPANY_NAME>.sharepoint.com

5. Then click **OK** to save your changes.
6. In your application in Studio, click the **Message Flow** tab.
7. Replace the **${csvImportFolder}** value in *Create folder in Sharepoint* and *Add file to Sharepoint* connectors with valid value (for example: my_folder)
8. In the **Package Explorer**, right-click the importing-a-csv-file-into-ms-sharepoint project name, then select **Run As > Mule Application**. Studio runs the application on the embedded server.  
9. This project includes a sample CSV file, called *contacts.csv*, that you can use to witness end-to-end functionality of the application. In the **Package Explorer**, click the *src/main/resources* folder to expand it, then find the *contacts.csv* file inside this folder.
10. Click and drag the *contacts.csv* file into an *input* folder in the same directory.
11. The File connector in the application polls the input folder every ten seconds. It picks up the CSV file, processes it, then deposits it into the output folder in the same directory. (Hit **F5** to refresh the contents of the input and output folders.)
12. In your browser, access your Sharepoint Online account, then navigate to **Documents** menu item on the left side.
13. There you can see your newly created folder and inside your folder you can see the uploaded file *contacts.csv*.
14. Stop the Mule application by clicking the square, red terminate button in the **Console**.
15. Delete the newly created folder from your Sharepoint Online account.

### How it Works ###

Using a single flow with five elements, this application accepts CSV file which contain contact information, then uploads the file to MS Sharepoint Online under the specified folder. If the folder hasn't existed already, it is created before uploading the CSV file. 

The [File connector](http://www.mulesoft.org/documentation/display/current/File+Connector) polls the input folder for new files every ten seconds. When it spots a new file, it reads it and store the content to the [Flow Variable](https://developer.mulesoft.com/docs/display/current/Variable+Transformer+Reference) for the later usage. Then a new folder in MS Sharepoint Online is created for storing the polled file. When creating the folder the connector's configurations specify the **operation** – *Folder create* –  and */Shared Documents* part of the path specifies that folder will be created under the *Documents* menu item on the left side. When folder is created, the file is uploaded to the MS Sharepoint Online from the previously stored variable. The connector's configurations for uploading file specify the **operation** – *File add* – and file is obtained from stream stored in the variable. For both actions the [MS Sharepoint 2013 Connector](https://developer.mulesoft.com/docs/display/current/Microsoft+SharePoint+2013+Connector) is used.

### Documentation ###

Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to share your project with others outside the Studio environment, you can export the project's documentation to print, email or share online. Studio's auto-generated documentation includes:

- A visual diagram of the flows in your application
- The XML configuration which corresponds to each flow in your application
- The text you entered in the Notes tab of any building block in your flow

Follow [the procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.

### Go Further ###

- Learn more about [File connector](http://www.mulesoft.org/documentation/display/current/File+Connector)
- Learn more about [Flow Variable](https://developer.mulesoft.com/docs/display/current/Variable+Transformer+Reference)
- Learn more about [MS Sharepoint 2013 Connector](https://developer.mulesoft.com/docs/display/current/Microsoft+SharePoint+2013+Connector).	
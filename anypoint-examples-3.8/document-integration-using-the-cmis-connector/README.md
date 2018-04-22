# Document integration using the CMIS Connector #

This application demonstrates how to connect to CMIS system. Based on the simple use case, the application loads a locally stored file and uploads it to an active CMIS account. 

### Connect with CMIS ###

Content Management Interoperability Services standard enables information sharing between different Content Management System. You may wish to exchange such content with various systems, e.g. filesystems, various document cloud  sharing systems. In these situations, CMIS Connector offers integration capabilities.

### Assumptions ###


This document assumes that you are familiar with Mule and the [Anypoint™ Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture) and [Mule Global Elements](http://www.mulesoft.org/documentation/display/current/Global+Elements).

This document describes the details of the example within the context of Anypoint Studio, Mule ESB’s graphical user interface, and includes configuration details for XML Editor where relevant. 

### Example Use Case ###

This application presents a simple use case scenario: by making an HTTP request to the designated HTTP endpoint the file is loaded from the local filesystem and uploaded to the predefined folder inside the CMIS. Id of the uploaded file is shown as the HTTP response.


### Set Up and Run the Example ###

Complete the following procedure to create, then run this example in your own instance of Anypoint Studio. Skip ahead to the next section if you prefer to simply examine this example.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). 
2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081 
3. In the **Package Explorer**, right-click the cmis-document-integration project name, then select **Run As > Mule Application**. Studio runs the application on the embedded server.
4. Open your Web browser.
5. In the address bar, type the following URL: *http://localhost:8081/cmis*
6. Press enter to elicit a response from the cmis-document-integration application. Notice the **id** in the response displayed in the browser window.
7. Navigate to *http://demo.openkm.com/OpenKM/login*. Use following login data:

		User Name: user3
		Password:  pass3

8. Click on the *okm:root* node in the left panel to verify that file with the generated name **id** was uploaded.
9. Stop the Mule application by clicking the square, red terminate button in the **Console**.

### How it Works ###

Using a single flow with four elements, the application receives HTTP requests, loads a locally stored file, uploads it there and shows its assigned ID as the integration result.

The request-response inbound [HTTP endpoint](http://www.mulesoft.org/documentation/display/current/HTTP+Connector) receives requests the end user submits to the Web service. Because it has a request-response message exchange pattern, this HTTP endpoint is responsible for both receiving and returning messages. 
[CMIS Connector](http://www.mulesoft.org/connectors/cmis-connector-3.4.0) offers a lot of operations to be performed on the defined system. It requires Connection Configuration that is created as a Global Element. In this use case we utilize Create document by path from content. [Scripting component](http://www.mulesoft.org/documentation/display/current/Script+Component+Reference) is responsible for loading a jpg file from the *scr/test/resources*.

The following steps outline the process to build this application. 

1. Drop an HTTP Connector to the flow. 
2. Click on the plus sign next to the Connector Configuration field. A dialogue for creating HTTP Listener Configuration will be displayed. Fill in:

		Host 	localhost
		Port 	8081 
   Click Ok button.	
2. Set *Path* attribute to *cmis* value.
6. Add Groovy Component and set Script Text to:
		
		import java.io.FileInputStream;
		return new FileInputStream("./src/test/resources/mulesoft-logo.jpg");
	
	You need to put a file mulesoft-logo.jpg in *src/test/resources* for this to work.
2. Add CMIS Connector. Click + sign next to Connection Configuration.
3. Set Username, Password and Base Url for your CMIS account.
4. When you click Test Connection, Mule tests the connection to CMIS. With a valid username, password and base url, the connection test results in success and Mule saves your global element configurations. If any of the values are invalid, the connection test results in failure, and Mule does not save the global element, prompting you to correct the invalid configurations.
7. Configure the CMIS connector:
		
		Operation    	  Create document from local file
		Filename 		  pic#[System.currentTimeMillis()].jpg
		Folder Path  	  /okm:root/
		Mime Type 		  image/jpg
		Object Type 	  cmis:document
		Versioning State  NONE
8. Add Object to JSON transformer to finish the flow.
9. The configuration now complete, you can save, then run the application. You can hit the HTTP endpoint and verify your file upload.

### Documentation ###

Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to share your project with others outside the Studio environment, you can export the project's documentation to print, email or share online. Studio's auto-generated documentation includes:

- A visual diagram of the flows in your application
- The XML configuration which corresponds to each flow in your application
- The text you entered in the Notes tab of any building block in your flow

Follow [the procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.

### Go Further ###
- Explore more [Mule example applications](https://www.mulesoft.com/exchange#!/?types=example).
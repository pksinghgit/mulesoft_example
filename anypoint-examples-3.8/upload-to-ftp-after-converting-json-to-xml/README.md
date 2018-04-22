# Upload to FTP after converting JSON to XML  

This example application illustrates the concept of datamapping to convert JSON data to XML. It also shows you how to configure and use the FTP connector to upload a file to a FTP server.

### Assumptions ###

This document assumes that you are familiar with Mule and the [Anypoint™ Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture), [Mule Global Elements](http://www.mulesoft.org/documentation/display/current/Global+Elements), and [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation). 

This document describes the details of the example within the context of Anypoint Studio, Mule ESB’s graphical user interface.

###Example Use Case
In this example JSON data is sent to the mule application through an HTTP end point. This data is then converted to the XML format using the DataWeave transformer after which the message payload is uploaded to the FTP folder. 

###Set up and run the example
1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).
2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081
3. In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!

4. Make a POST request using Postman to http://localhost:8081 with following JSON message body, and setting the 'Content-Type' header as 'application/json':

	    {
	    "employees": {
	    "employee": [
	      {
	        "name": "John",
	        "lastName": "Doe",
	        "addresses": {
	          "address": [
	            {
	              "street": "123 Main Street",
	              "zipCode": "111"
	            },
	            {
	              "street": "987 Cypress Avenue",
	              "zipCode": "222"
	            }
	          ]
	        }
	      },
	      {
	        "name": "Jane",
	        "lastName": "Doe",
	        "addresses": {
	          "address": [
	            {
	              "street": "345 Main Street",
	              "zipCode": "111"
	            },
	            {
	              "street": "654 Sunset Boulevard",
	              "zipCode": "333"
	            }
	          ]
	        }
	      }
	    ]
	    }
	    } 
	
5. Verify if the file was uploaded at ***http://ftp-server.demo.solarwinds.com/*** with the following credentials
```	
	login: demo
	password: demo
	folder: Upload
```
**Note:** The FTP uploaded file name is set to *muleExample.xml*, but it can be changed using the *outputPattern* attribute within the FTP connector operation.

### Documentation ###

Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to share your project with others outside the Studio environment, you can export the project's documentation to print, email or share online. Studio's auto-generated documentation includes:

- A visual diagram of the flows in your application
- The XML configuration which corresponds to each flow in your application
- The text you entered in the Notes tab of any building block in your flow

Follow [the procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.
   
### Go further
* Read about the DataWeave Transformer [here](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation)
* Read about the Mule FTP endpoint [here](http://www.mulesoft.org/documentation/display/current/FTP+Transport+Reference)
* Read about the Mule SFTP endpoint [here](http://www.mulesoft.org/documentation/display/current/SFTP+Transport+Reference)

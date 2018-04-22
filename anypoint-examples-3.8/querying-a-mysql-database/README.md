#Querying a MySQL Database

This example illustrates how to use the database connector to connect to a MySQL database. After reading this document and creating and running the example in Mule, you should be able to leverage what you have learned to create an application that connects to a MySQL database.


###Assumptions

This document describes the details of the example within the context of Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). Where appropriate, the XML configuration accompanies the Studio interface screenshots. This document assumes that you are familiar with Mule ESB, the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials), MySQL, the [database connector](http://www.mulesoft.org/documentation/display/current/Database+Connector) and [DataSense](http://www.mulesoft.org/documentation/display/current/Database+Connector).

###Example Use Case

In the Mule application, an inbound HTTP connector listens for HTTP GET requests with the form: http://<host>:8081/?lastname=<parameter>. The HTTP connector passes the value of <parameter> as one of the message properties to a database connector. The database connector is configured to extract this value and use it for the SQL query listed below.

	select first_name from employees where last_name = "#[message.inboundProperties['http.query.params'].lastname]" 

As you can see, the MEL expression in the SQL query references the value of the parameter passed to the HTTP connector. So if the HTTP connector receives http://localhost:8081/?lastname=Smith, the SQL query will be select first_name from employees where last_name = Smith.

The database connector instructs the database server to run the SQL query, retrieves the result of the query, and passes it to the object-to-JSON message processor which converts the result to JSON. Since the HTTP connector is configured as request-response, the result is returned to the originating HTTP client.

###Set Up and Run the Example

1. **[Download and setup the MySQL driver](http://dev.mysql.com/doc/refman/5.7/en/installing.html)** in it's default location. You can even check out some of the YouTube videos for assistance with this step.

2. **Start the MySQL server** from System Preferences
   

3. **Creating the MySQL Database**: Visit [this](http://www.mulesoft.org/documentation/display/current/Database+Connector+Examples#DatabaseConnectorExamples-script) page and click on **View the script for copy-paste** to copy the script. Navigate to the MySQL driver on the command terminal and then paste the script to create a MySQL database called Company which has tables for employees and roles. The script also creates a password protected user and grants it access to the database. 
   
        username: generatedata; password:generatedata
 
4. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).
5. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081

6. **Open database-to-json.xml file** located in src/main/app directory. Set **url** attribute of the **db:generic-config** element to:

		jdbc:mysql://localhost:3306/company?user=generatedata&password=generatedata

7. **Configuring the Database Connector for this Example**:
In this example, the database connector retrieves data from a MySQL database listening on port 3306, the default for MySQL. Ensure that MySQL_Configuration points to the local jdbc MySQL server on your machine. You may watch  [this](https://www.youtube.com/watch?v=9fqtSqngy1c) video for assistance with this step. 
 

8. Run the example application in Anypoint Studio or Standalone

9. Got to your web browser and type in the following request:
             
        http://localhost:8081/?lastname=Puckett
       
	and you should get the following JSON response:
       
        [{"first_name":"Chava"},{"first_name":"Quentin"}]
      


###Go Further

* View this [document](http://www.mulesoft.org/documentation/display/current/Database+Connector) for more information on how to configure a database connector on your machine.
* Access plain [Reference material](http://www.mulesoft.org/documentation/display/current/Database+Connector+Reference) for the Database Connector.
* Learn more about [DataSense](http://www.mulesoft.org/documentation/display/current/DataSense).

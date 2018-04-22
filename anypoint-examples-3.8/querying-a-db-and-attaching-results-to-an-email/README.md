# Querying a DB and attaching results to an Email

This example application shows you how to query a MySQL database, aggregate the query results and use the Attachment transformer to attach it to an email in a csv format

###Example use case
The XML data containing employee names is sent to the application using the HTTP POST method. The Splitter component then splits the list of employees and queries the MySQL DB individually for employee details. The Collection Aggregrator component aggregates all the employee information into a List. This List is then converted to a .csv file and attached to an email which is sent using SMTP.

### Set up and run the example

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). 

2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081
2. Start the MySQL server on your machine and create a connection by navigating to your mysql home directory and using the following command:
                  
        mysql  -u root -p

3. Now, run **import_orders.sql** which is placed under src/main/resources to create a DB table

4. Click on the SMTP connector and **configure its properties** as follows:

        Host=smtp.gmail.com
        Port=587
        User=senderemailid%40gmail.com
        Password=senderpassword

        To=receiveremailid@gmail.com
        From=senderemailid@gmail.com
        Subject=Export from Excel

5. In *Global Elements* tab click on the Generic Database Configuration and **configure its properties** as follows:

        URL=jdbc:mysql://localhost:3306/company?user=user&password=password
        
6. Now,run the mule application

7. Make a POST request using Postman to your localhost with the following xml code as the message body:

        <root>
         <employees>
          <employee>Chava Puckett</employee>
          <employee>Quentin Puckett</employee>
          <employee>Mona Sosa</employee>
         </employees>
        </root>

8. Verify that you recieved an email with the attachment which is basically a csv file of the queried employee records.

###Go further
* Read about the Database Connector [here](http://www.mulesoft.org/documentation/display/current/Database+Connector)
* Read about the Attachment Transformer Reference [here](http://www.mulesoft.org/documentation/display/current/Attachment+Transformer+Reference)
* Learn more about Anypoint DataWeave [here](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation)


# Importing an attached csv file using the IMAP connector


This example shows you how to use the IMAP connector to facilitate information transfer through email. It also illustrates how you can use DataWeave to transform a CSV file to XML.

### Assumptions

This document describes the details of the example within the context of Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). This document assumes that you are familiar with Mule ESB, [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation) and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). 

### Example Use Case

In this example a CSV file containing sample sales data which has been received as an attachment in an email inbox is imported using the IMAP connector. The DataWeave transformer then converts the CSV file to the XML format. The logger then logs this data on the studio console.

### Set Up and Run this Example

1.  Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).

2. Click on the IMAP Connector in the Mule flow and edit its properties as follows:

           imap.server=imap.googlemail.com
           imap.port=993
           imap.user=receiveremailaddress%40gmail.com
           imap.password=receiver_password
    
3. **Run** the project as a Mule application

4. Navigate to src/main/resources to find the input.csv file. Use any email address to **send the 'input.csv' file as an attachement**  to receiveremailaddress@gmail.com

5. If you have configured and run this example correctly, the csv file should appear in the xml format in the studio console. The log message should be similar to what is show below:

         INFO  2014-07-07 10:39:43,897 [[imap-to-xml].imap-to-csvFlow1.stage1.02]          
         org.mule.api.processor.LoggerMessageProcessor: recieved: <?xml version="1.0" encoding="UTF-8"?>
           <orders>
	           <order>
		           <orderId>1</orderId>
		           <name>aaa</name>
		           <units>2.0</units>
		           <pricePerUnit>10</pricePerUnit>
	           </order>
	           <order>
		           <orderId>2</orderId>
		           <name>bbb</name>
		           <units>4.15</units>
		           <pricePerUnit>5</pricePerUnit>
	           </order>
           </orders>

6. After processing the attachment, it will be saved in src/tests/resources/output folder with xml format.

### Go Further

* Read more about the [IMAP Connector](http://www.mulesoft.org/documentation/display/current/IMAP+Connector)

* Read more about the [IMAP Transport Reference](http://www.mulesoft.org/documentation/display/current/IMAP+Transport+Reference)

* Read more about the [Anypoint DataWeave transformer](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation).

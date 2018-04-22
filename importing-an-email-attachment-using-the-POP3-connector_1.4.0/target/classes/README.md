# Importing an attached csv file using the POP3 connector


This example shows you how to use the POP3 connector to facilitate information transfer through email. It also illustrates how you can use DataWeave to transform a CSV file to XML.

### Assumptions

This document describes the details of the example within the context of Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). This document assumes that you are familiar with Mule ESB, [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation) and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). 

### Example Use Case

In this example a CSV file containing sample sales data which has been received as an attachment in a mail inbox is imported using the POP3 connector. The DataWeave transformer then converts the CSV file to the XML format. The logger then logs this data on the studio console.

### Set Up and Run this Example

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).

2. Click on the POP3 endpoint in the Mule flow and edit its properties as follows:

           pop3.host=pop.gmail.com
           pop3.port=995
           pop3.user=receiveremailaddress%40gmail.com
           pop3.password=receiver_password
    
3. **Run** the project as a Mule application

4. Navigate to src/main/resources and use any email address to **send the 'input.csv' file as an attachement**  to receiveremailaddress@gmail.com


5. If you have configured and run this example correctly, the csv file should appear in XML format in the studio console. The log message should be similar to what is shown below:

        INFO  2014-07-07 16:49:09,973 [[pop3-to-xml-master].POP3.receiver.01] org.mule.api.security.tls.TlsPropertiesMapper:         Defaulting mule.email.pop3s trust store to client Key Store
        WARN  2014-07-07 16:49:09,988 [[pop3-to-xml-master].POP3.receiver.01] org.mule.api.security.tls.TlsProperties: File         tls-default.conf not found, using default configuration.
        INFO  2014-07-07 16:49:10,706 [[pop3-to-xml-master].pop-to-xmlFlow1.stage1.02]       
        org.mule.api.processor.LoggerMessageProcessor: recieved: <?xml version="1.0" encoding="UTF-8"?>                   
        < orders>
        < order>
        < orderId>1< /orderId>
        < name>aaa< /name>
        < units>2.0< /units>
        < pricePerUnit>10< /pricePerUnit>
        < /order>
        < /orders>
        < orders>
        < order>
        < orderId>2< /orderId>
        < name>bbb< /name>
        < units>4.15< /units>
        < pricePerUnit>5< /pricePerUnit>
        < /order>
        < /orders>

6. After processing the attachment, it will be saved in src/tests/resources/output folder with XML format.

### Go Further

* Read more about the [POP3 Connector](http://www.mulesoft.org/documentation/display/current/POP3+Connector)

* Read more about the [POP3 Transport](http://www.mulesoft.org/documentation/display/current/POP3+Transport+Reference)

* Read more about the [Anypoint DataWeave transformer](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation).

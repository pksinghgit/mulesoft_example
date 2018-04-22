#Sending JSON Data to a JMS Queue 

Mule Studio offers easy-to-use components to connect to JMS Queues and Topics. In today’s example, we’re going to learn how to use ActiveMQ, a leading open source JMS implementation from Apache that supports JMS 1.1 specification.   

###Assumptions
This document assumes that you are familiar with Mule and the Anypoint™ Studio interface. To increase your familiarity with Studio, consider completing one or more Anypoint Studio Tutorials. Further, this example assumes that you have Apache Maven and ActiveMQ running on your machine.

###Sample Use Case
 In this example, an HTTP request holding the JSON sales data reaches the HTTP endpoint in the byte array format. A transformer then converts the JSON data from a byte array into a string. A success message is logged and the string is added to a JMS queue. Once a message reaches the JMS queue it can be viewed through the activeMQ admin console.

###Set Up and Run the Example
Complete the following procedure to create, then run this example in your own instance of Anypoint Studio.


1.  Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange). Do not run the application yet.

2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081

3. Start the ActiveMQ server;  navigate to the activeMQ home directory in the command terminal and then   use the  bin/activemq start command to start the activeMQ server.   You should get a message that is similar to the one shown below when your activeMQ server is running

       INFO: Using java '/System/Library/Frameworks/JavaVM.framework/Home/bin/java'
       INFO: Starting - inspect logfiles specified in logging.properties and  log4j.properties to get details
       INFO: pidfile created : . . . 
       
4. Click on json-to-jms.xml in the Package Explorer and open its Global Elements tab. Doubleclick on Active MQ item and edit Broker URL to your Active MQ broker URL. The default installation value is **tcp://localhost:61616**. If blank, the JMS elements will act using vm protocol.    
5. In the Package Explorer, right-click on the project, then select Run As > Mule Application. Studio runs the application on the embedded server and connects to the localhost on the port to which the HTTP end-point is configured.

6. Send JSON Data to the url using REST Console. You could even use curl to do so. Use the following screenshot as a guide to fill in the required details within the REST Console. Click on the Send button to send the JSON data as an HTTP request to your localhost server. 
      
        Request URI: http://localhost:8081/sales
        Request method: POST
        Body : {"ITEM_ID"= 001, "ITEM_NAME" = "Shirt", "QTY" = 1, "PRICE" = 20}

7. Log in to ActiveMQ admin page at [http://localhost:8161/admin/queues.jsp](http://localhost:8161/admin/queues.jsp)
 with the default username and password “admin”. View if the message was added to the queue
In the ActiveMQ queue, click on the sales link and then on the link under the message id column. On doing so, you will see details of the message that was added to the JMS queue:


###Go Further

* [Blog](http://blogs.mulesoft.org/mule-school-jms-tutorial/)  post to understand the configuration of this example better
* [Blog](http://blogs.mulesoft.org/jms-message-rollback-and-redelivery/) post on JMS message rollback and redelivery with Mule
* [Blog](http://blogs.mulesoft.org/message-sequencing-with-mule-and-jms-message-groups/) post on Message sequencing with Mule and JMS message groups







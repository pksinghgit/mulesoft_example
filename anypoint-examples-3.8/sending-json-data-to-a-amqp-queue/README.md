# Sending JSON data to a AMQP queue



This example shows you how to use the AMQP connector to send JSON data to RabitMQ, an open source message broker that implements AMQP.

### Assumption
This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). Further, this example assumes that you have [RabbitMQ](https://www.rabbitmq.com/download.html) enabled with [Management Plugin](http://www.thegeekstuff.com/2013/10/enable-rabbitmq-management-plugin/) installed on your machine.


### Example Use Case
In this example a message containing sample sales data in JSON is received through an HTTP endpoint. This message is then converted to a string using a transformer and then sent to RabbitMQ using the AMQP connector. Once this message reaches the queue, it can be viewed throught he RabbitMQ web console.

### Set Up and Run the Example

1. After making sure that RabbitMQ is running, log in to the RabbitMQ web **admin console** at

        http://localhost:15672
        
2. Go to the **Exchanges** tab and click on **Add a new Exchange** to create an exchange called ***sales_exchange***. You may leave the other feilds as they are and then click on **Add exchange**.

3. Now go to the **Queues** tab and click on Add a new queue to create a queue called ***sales_queue***. You may leave the other fields as they are and then click on **Add queue**.

4. Click on **sales__exchange** under the **Exchanges** tab and then go to the section titled **Add binding from this exchange**. In the **To queue** field type in ***sales_queue*** and then click on **Bind**.
    
5. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).
6. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081
7. In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!

8. Make a HTTP POST request using Postman, curl or the REST Console to send the following JSON data:
        
        {"ITEM_ID"= 001, "ITEM_NAME" = "Shirt", "QTY" = 1, "PRICE" = 20}
        
9. Now, navigate back to the RabbbitMQ web admin console. You should notice an increase in the  number of messages in sales_queue. You may also **view the message** by clicking on sales_queue in the **Get messages** option.

### Go Further

* Read more about the [AMQP connector](https://www.mulesoft.com/cloud-connectors/mule-amqp-integration-connector)
* Check out the Mule AMQP master guide on [GitHub](https://github.com/mulesoft/mule-transport-amqp/blob/master/GUIDE.md) 
* You may get to know more about the features supported by this connector [here](https://github.com/mulesoft/mule-transport-amqp)
        
        

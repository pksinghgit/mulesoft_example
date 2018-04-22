# Sending a CSV file through email using SMTP


This example shows you how to use the SMTP connector to facilitate information transfer through email. It also illustrates how you can use the File connector to input a a csv file and then tranform it using the dataweave transformer.

### Assumptions

This document describes the details of the example within the context of Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). This document assumes that you are familiar with Mule ESB, [DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation) and the [Anypoint Studio](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials) interface. 

### Example Use Case

In this example a CSV file containing sample sales data which is stored in the local directory is converted to the collection of Maps using the DataWeave transformer and is sent to an email address using the SMTP connector. The DataWeave also computes the total price for each order by multiplying the unit price with the number of units. This example has been configured for Google's gmail.

### Set Up and Run this Example

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).

2. Create a folder called input under src/main/resources and **paste** the input.csv file in the input folder. Edit the path field in the file connector to:
    
        Path=src/main/resources/input
 

3. Click on the SMTP connector and **configure its properties** as follows:

        Host=smtp.gmail.com
        Port=587
        User=senderemailid%40gmail.com
        Password=senderpassword

        To=receiveremailid@gmail.com
        From=senderemailid@gmail.com
        Subject=Export from Excel
    
4. **Run** the project as a Mule application

5. Login to receiveremailid@gmail.com to **verify** if the sales data was received via email. You should get an email that has the following content:

       [{name=aaa, orderId=1, pricePerUnit=10, units=2, totalPrice=20}, {name=bbb, orderId=2, pricePerUnit=5, units=4.15, totalPrice=20.75}]

### Go Further

* Read more about the [SMTP Connector](http://www.mulesoft.org/documentation/display/current/SMTP+Transport+Reference)

* Read more about the [File Connector](http://www.mulesoft.org/documentation/display/current/File+Transport+Reference)

* Read more about the [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation)

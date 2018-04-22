# Scatter Gather flow control to email


This example shows us the usage of the scatter-gather flow to aggregate data in parallel and send the report message to a gmail account. The example uses prepared data as an input for two resources, which should be aggregated. The data represents contacts information with structure as follows:

	sourceA:
		firstContact  "Id": 	 "1"
					  "Name": 	 "vlado";
					  "Email":	 "vlado@email.com"
		secondContact "Id": 	 "2"
					  "Name": 	 "michal";
					  "Email":	 "michal@email.com"
and

	sourceB:
		firstContact  "Id": 	 "1"
					  "Name": 	 "peter";
					  "Email":	 "peter@email.com"
		secondContact "Id": 	 "2"
					  "Name": 	 "vlado";
					  "Email":	 "vlado@email.com"

For the aggregation is used [Java Transformer](http://www.mulesoft.org/documentation/display/current/Java+Transformer+Reference).
Transformer receives a Mule Message with the two Invocation variables to result in List of Maps with keys: Name, Email, IDInA, IDInB.

Contacts will be matched by Email, that is to say, a record in both data sources with the same Email is considered the same contact.
The result of aggregation for our input data should looks like as follows:
   
	Email				Name		IDInA	IDInB
	vlado@email.com		vlado		1		2
	michal@email.com	michal		2	
	peter@email.com		peter				1
	

### Assumptions

This document describes the details of the example within the context of Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture), and [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation).

### Example Use Case

In this example a sample data is prepared using the Expression component, which serve as a input for aggregation of the Scatter-Gather component. Then the report is transformed to the CSV format  and it is also sent to an email address using the SMTP connector. This example has been configured for gmail.

### Set Up and Run this Example

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).

2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081

3. Navigate to src/main/app/scatter-gather.xml and **edit Send an email via SMTP component** as follows:
 
        # Basic settings
        Host: smtp.gmail.com
        Port: 587
        User: senderemailid%40gmail.com
        Password: senderpassword

        # Email information
		To: receiveremailid@gmail.com
        From: senderemailid@gmail.com
        Subject: Processing Finished Report
    
4. **Run** the project as a Mule application

5. Open your Web browser.

6. In the address bar, type the following URL: http://localhost:8081/scatterGather 

7. Press enter to elicit a response from the Scatter-Gather flow control example.

8. Login to receiveremailid@gmail.com to **verify** if the  data was received via email.

### Go Further

* Read more about the [SMTP Connector](http://www.mulesoft.org/documentation/display/current/SMTP+Transport+Reference)

* Read more about the [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation)

* Read more about the [Expression Component](http://www.mulesoft.org/documentation/display/current/Expression+Component+Reference)

* Read more about the [Scatter Gather Flow Control](http://www.mulesoft.org/documentation/display/current/Scatter-Gather)

* Read more about the [Java Transformer](http://www.mulesoft.org/documentation/display/current/Java+Transformer+Reference) 

* Check out this [blog](http://blogs.mulesoft.org/parallel-multicasting-simplified/) on Parallel Processing in Mule.



   

# Implementing a Choice Exception Strategy

This example illustrates the concept of error handling in mule. This particular example deals with choice exception strategy.

### Example use case

JSON sales data is sent through the HTTP endpoint. The input data is validated and in case of missing or invalid data, an appropriate exception is thrown. A series of custom filters then catch the exception in the JSON data and route the message to the error handling flow. The input data is validated and in case of missing or invalid data, an appropriate exception is thrown.

### Set up and run the example

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).

2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081

3. Run the example project as a mule application

4. Use Postman to make the following JSON POST request:
 
        {
        "email": "aaa@aaa.aa", 
        "item name": "aa", 
        "item units": 10,  
        "item price per unit": 1,
        "membership": "free"
        } 
       
    This message throws no error and returns the following messsage:
       
        Input data validation passed.

5. Use Postman to make the following JSON POST request:
 
        {
        "item name": "aa", 
        "item units": 10, 
        "item price per unit": 1,
        "membership": "free"
        }
        
	This message throws an exception as the email field is missing. Status code 400 is returned with the following message:
    
        Missing input data: {item name=aa, membership=free, item price per unit=1, item units=10}

6. Use Postman to make the following JSON POST request:

        {
        "email": "aaa@aaa.aa", 
        "item name": "aa", 
        "item units": 10, 
        "item price per unit": -1,
        "membership": "free"
        }
    
 
	This message throws an exception as the item price per unit is negative, and the following error message is returned. This applies to negative **item units** as well.
   
		Invalid input data: {item name=aa, membership=free, item price per unit=-1, email=aaa@aaa.aa, item units=10}
       
 
### Go further
       
 * Read the documentation about choice exception strategy [here](http://www.mulesoft.org/documentation/display/current/Choice+Exception+Strategy)
   
   


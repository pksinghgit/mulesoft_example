#Cache Scope with Fibonacci  


The example that follows demonstrates the power of the cache scope with a Fibonacci function. On implementing this example you will understand how to deploy the cache scope within a Mule application. This will also give you some insight on caching strategies and how sub flows work within Mule. 

### Assumptions

This document describes the details of the example within the context of Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). Where appropriate, the XML configuration accompanies the Studio interface screenshots. This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). You should be also familiar with the concept of caching in Java to better understand the value proposition of the cache scope as demonstrated through this example.  


### Example Use Case
This example solves the Fibonacci function F(n) for the n which is given as input by the user. Intermediate results are cached and later can be retrieved by the subsequent requests. The Finobacci sequence is a series of numbers in which the next number in the series is always the sum of the two numbers preceding it.

In this example, the Mule flow receives and performs two tasks for each request:

executes, and returns the answer to, the Fibonacci equation (see below) using a number (n) provided by the caller 
F(n) = F(n-1) + F(n-2) with F(0) = 0 and F(1) = 1
records and returns the cost of the calculation, wherein each individual invocation of a calculation task (i.e. add two numbers in the sequence) adds 1 to the cost


### Set Up and Run this Example

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).
2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081
3. In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!
4. Once the application is running, go to your localhost server and type in the following request:

        http://localhost:8081/fibonacci?n=10
        
	You should get a response similar to what is shown below:
   
        Fibonacci(10) = 55
        COST: 11
        
5. Now, change the value of n to 5
        
        http://localhost:8081/fibonacci?n=5
   
	You should get a response similar to what is shown below:
        
        Fibonacci(5) = 5
        COST: 0
        
  >  Note that the cost for calculating f(n) is 0. This is because f(5) has already been calculated and cached in the cache scope block while calculating f(10). If you try calculating f(10) again, the cost would be 0 as f(10) has already been calculated and cached.
  
### Go Further

* Read more about the [Cache Scope](http://www.mulesoft.org/documentation/display/current/Cache+Scope) in Mule.

* Read more about [Scopes](http://www.mulesoft.org/documentation/display/current/Scopes) in Mule.
   

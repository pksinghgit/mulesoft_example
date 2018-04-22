# Web Service Consumer Example #

#### Enterprise, CloudHub

This application illustrates how to consume an existing Web service. There are two kinds of operations the application performs in two separate flows: one flow issues T-shirt purchase orders, the other requests an inventory list.

### Web Service Consumer

The [Web Service Consumer](http://www.mulesoft.org/documentation/display/current/Web+Service+Consumer) is a connector that encapsulates all the functionality related to consuming a Web service, greatly simplifying its implementation. Using the information contained in a service's WSDL, this connector enables you to configure a few details in order to establish the connection you need to consume a service from within your Mule application.

### Assumptions

This document assumes that you are familiar with Mule ESB and the [Anypoint Studio™ interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes you are familiar with XML coding and that you have a basic understanding of [Mule flow](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture)s, SOAP as a Web service paradigm, and the practice of WSDL-first Web service development.  

This document describes the details of the example within the context of Anypoint Studio, Mule ESB’s graphical user interface (GUI), and includes configuration details for both the visual and XML editors.

### Example Use Case ###

This example application simulates consuming a Web service that belongs to a T-Shirt retailer. Through HTTP requests, customers can check availability of products and place purchase orders. When the consumer app receives an order request, it turns the JSON input into XML, adds an APIKey variable, then performs a request to the Web service, transforms the response into JSON and builds a final response to send back to the requester.

When the consumer app receives a list-inventory request, it forwards the request to the Web service, turns the response into JSON and builds a final response for the requester.

### Set Up and Run the Example ###

Complete the following procedure to create, then run this example in your own instance of Anypoint Studio. You can create template applications straight out of the box in Anypoint Studio and tweak the configurations to create your own customized application.

Skip ahead to the next section if you prefer to simply examine this example via code snippets.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).
2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081
3. In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!
4. Send posts to your application via a browser extension such as Postman (for Google Chrome), or the curl command-line utility.
	* Send your request to the address http://localhost:8081/orders
	* Set the 'Content-Type' header as 'application/json'
	* Append the following JSON code to it:

```
{
  "email":"info@mulesoft.com",
  "address1":"Corrientes 316",
  "address2":"EP",
  "city":"Buenos Aires",
  "country":"Argentina",
  "name":"MuleSoft Argentina",
  "postalCode":"C1043AAQ",
  "size":"L",
  "stateOrProvince":"CABA"
}
```

### How It Works ###

The Client-Side T-Shirt API example consumes a SOAP-based Web service, which accepts two different kinds of requests, each handled in a different flow, accessed through a different HTTP path.

### orderTshirt Flow

The orderTshirt flow accepts HTTP requests that are directed at its address, then turns the JSON payload into XML by using the DataWeave transformer. As the consumed Web service requires an APIKey to be passed with every request, the flow creates the XML SOAP header hardcoding the APIKey value using DataWeave transformer. With the XML envelope built, the flow then contacts the Web service via the Web Service Consumer. This flow is also responsible for returning a response to the caller to confirm that the order was processed, so it transforms the resulting response to JSON format.

### listInventory Flow

When issued a "list inventory" request, the flow directs it to the Web service via the Web service consumer, its response is then transformed into a JSON by the DataWeave transformer. Finally, the HTTP connector returns the response to the requester.

### Go Further ###

- Learn more about about the [Web Service Consumer](http://www.mulesoft.org/documentation/display/current/Web+Service+Consumer).
- Learn more about [Anypoint DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation).
- Learn more about how [Anypoint Service Registry](http://www.mulesoft.org/documentation/display/current/Anypoint+Service+Registry) can help you organize your organization's services.
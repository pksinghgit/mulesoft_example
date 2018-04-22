# OAuth2 Authorization Code using the HTTP Connector Example

Often you are faced with a requirement to handle authorization to the third party service. This example application illustrates how to execute it using Mule ESB.

### Assumptions

This document describes the details of the example within the context of Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Mule Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial).

### Example Use Case

In this example, by hitting an HTTP endpoint a user will attempt to grant the access to his data at Box Service. For this purpose, OAuth authorization will be triggered. A user will be asked to enter his user name and password. If successful, he can click a button in order to grant the access.  

### Set Up and Run the Example ###

To follow along with the steps in this example, you must have a [box.com](https://app.box.com/files) account, which you can create for free if you don't already have one.

#### Registering an App in the Box Developer Portal ####

The steps below are only needed in this particular example so that you can test your finished application. 

1. Go to Box's developer portal: [developers.box.com](https://developers.box.com/)
1. If you do not have an account, you need to create one [here](https://app.box.com/signup/personal). If you have one, click **My apps** in the upper-right corner of the [page](https://developers.box.com/).
2. Click **Create a Box Application** in the panel on the right. Give it any name, such as MyProxy, then select the **Box Content API**. 
1. Click **Configure Your Application**.
1. Look for the *client_id* and the *client_secret*. Copy these to a safe place, as you will need them later.
1. Add a *redirect_url*. For the purpose of this exercise, set it to *https://localhost:8082/redirectUrl*.

If you're using HTTPS, as the Box API requires, you must create a keystore and a trust store file to certify the communication. This can be done using the keytool provided by Java, found in the bin directory of your Java installation. Navigate to this directory on your machine using the command line (this is not needed if Java bin directory is contained in your PATH variable), then execute the following command to create a keystore file:

	keytool -genkey -alias replserver -keyalg RSA -keystore keystore.jks

You will be prompted to create two passwords. Remember these and fill them in the configuration later on (parameters: *keystore.password, keystore.keyPassword*). The command creates a .jks file in the directory called keystore.jks.

The keystore (keystore.jks) file along with its corresponding passwords can now be used. Move it into the */src/main/resources* directory in Mule Studio's Package Explorer.
If you need more help doing this, feel free to use [this resource](http://docs.continuent.com/tungsten-replicator-2.1/deployment-ssl-stores.html#deployment-ssl-stores-own).

#### Building the example in Studio ####

1. Firstly, open http-authorization-code-web.xml in Anypoint Studio and go to Global Elements. Inside TLS Context element replace the values for keystore keyPassword, keystore password  and truststore password with the corresponding data you entered while creating a keystore and a trust store using the commandline - see the previous section.
2. Then go to HTTP Request Configuration element and open the Authentication tab. Fill in the client_id and client_secret you got in the previous section.
2. Deploy your Mule Project to the embedded Mule server by right-clicking the project in the Package Explorer, then selecting **Run As... > Mule Application**.
2. In any Web browser, enter the following URL: 

		http://localhost:8081/web

3. Box will prompt you to log in with your username and password. Click **Authorize**. You can use your personal credentials or create a new test account.
4. Clicking **Grant access to Box** (or *Deny access to Box as well*) will redirect you to **http://localhost:8081/web/loginDone**.
5. Then the example will try consume a resource using the recently obtained token (in this case, search for items containing the term "mule") and display the result.
  
### How it works

#### boxUserLoginFlow

The flow contains only two blocks. First one is an inbound HTTP endpoint that performs several functionalities. It accepts incoming HTTP GET requests. When such requests arrives, it redirects the processing to *https://localhost:8082/authorization* that basically triggers the OAuth authorization. After the successful operation, the client is redirected to the **userLoginDoneFlow** flow.  

The OAuth authorization is defined by the *authorization-code-grant-type* component which is wrapped in the HTTP request config component. The request URL is set to *https://api.box.com*. As HTTPS protocol is specified, TLS context needed to be introduced. The context has two required values: a key store and a trust store.  

To start an OAuth operation you will need a *clientId*, a *clientSecret* issued by the third party service and a redirect URL that is used after the finished authorization process. To demonstrate some capabilities of this component, custom URL parameters (i.e. *box_device_id* and *box_device_name*) and a custom extractor are implemented.


#### userLoginDoneFlow

This flow simply informs a user that the authorization was successful and consumes a resource using the recently obtained token, showing the result in the browser.

### Go Further

- Learn more about the [HTTP Connector](http://www.mulesoft.org/documentation/display/current/HTTP+Connector).
- Learn more about [OAuth Authentication using the HTTP Connector](http://www.mulesoft.org/documentation/display/current/Authentication+in+HTTP+Requests#AuthenticationinHTTPRequests-code).
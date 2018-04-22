#XML-only SOAP Web Service


This application illustrates how you can use Mule to expose a SOAP Web service. In particular, it illustrates how a Mule application can accept SOAP requests, then orchestrate several Web services to meets the needs of a particular business process. Further, this example performs all of these actions using only XML – no Java. To demonstrate these capabilities, this example is based on the use case of patient admission into a hospital.

####Service Orchestration

This term applies to the activity of coordinating calls to several different Web services in order to process a single Web service request. As the name implies, an application, such as this example, can orchestrate the sequence of calls to services.  Like the conductor of an orchestra, a single Mule flow signals when to submit calls to services, ensuring that all the moving pieces work together to produce a single response.

####Content-Based Routing

Mule has the ability to intelligently route a message through different processing pathways according to its content. Using a Choice Router, Mule uses an expression to evaluate a message's properties, or part of its payload, then routes it to a specific pathway (i.e. series of message processors). Content-based routing, as this activity is called, dynamically applies routing criteria to a message at runtime.

###Assumptions

This document assumes that you are familiar with Mule ESB and the Anypoint™ Studio interface. To increase your familiarity with Studio, consider completing one or more Anypoint Studio Tutorials. Further, this example assumes you are familiar with XML coding and that you have a basic understanding of Mule flows, SOAP as a Web service paradigm, and the practice of WSDL-first Web service development. 

This document describes the details of the example within the context of Anypoint Studio, Mule ESB’s graphical user interface (GUI), and includes configuration details for both the visual and XML editors. 

 

###Example Use Case

This example demonstrates service orchestration and content-based routing within the context of a simple use case: in order to facilitate patient pre-admission into a hospital, the hospital has exposed a SOAP Web service called AdmissionService. Using this Web service, a patient's family doctor can perform two tasks:

* For a new patient, she can create a new patient record (EHR) and episode on an EHR to initiate a patient's admission into the hospital.
* For an existing patient, she can locate an existing EHR and create a new Episode.

	
      EHR (Electronic Health Record) is the electronic patient record that tracks and updates all patient data, such as name, patient ID,  billing address, etc.
      An Episode is the occurrence of an event related to a specific patient. For example, a patient's pre-admission into a hospital counts as an "episode" on the EHR."


For example, if a family doctor wishes to schedule a surgical procedure for an existing patient at the hospital, she uses her desktop software to record all the relevant data for the existing patient's upcoming surgery. The software then submits a SOAP request to the hospital's AdmissionService, which processes the request – task 2 above – locating the existing EHR for the patient then scheduling the surgical procedure. 

 

###Set Up and run the Example

Complete the following procedure to create, then run this example in your own instance of Anypoint Studio. You can create template applications straight out of the box in Anypoint Studio and tweak the configurations of the use case-based templates to create your own customized applications in Mule.


1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).

2. In your application in Studio, click the **Global Elements** tab. Double-click the HTTP Listener global element to open its **Global Element Properties** panel. Change the contents of the **port** field to required HTTP port e.g. 8081

3. In the Package Explorer pane in Studio, right-click the project name, then select Run As > Mule Application. Studio runs the application and Mule is up and kicking!

4. To simulate a request submission to the Mule application, use the soapUI interface available for free download at [www.soapui.org](www.soapui.org). This tool enables you to submit a request as though you were a family doctor in this example's use case. If you haven't already done so, download and launch soapUI.

5. In soapUI, select File > Import Project. Browse to the AnypointStudio folder on your local drive to locate the sample request file: AnypointStudio > workspace > XML-only SOAP Web Service > src > main > resources > Hospital-Admission-Example-soapui-project.xml. Click Open.

6. In the new Hospital Admission Example project in soapUI, expand the folders to reveal Request 3. Double-click Request 3 to open the request-response window.
7. Click the submit request icon (green "play" button at upper left) to submit the request to the Mule application (see below, left). soapUI displays the response from the Mule application in the response pane (see below, right).

8. Review the contents of the SOAP response, to examine the details of your processed request. Note the response contains information about the patient's new episode and details about billing.
 

###How it Works 

The application is divided into two parts, each stored on separate flow files (.xml files) in the Mule project.

The ***Hospital Admissions SOA*** file contains the heart of the hospital's SOAP proxy Web service application. It accepts SOAP requests from family doctors, then processes them to locate or create EHRs or episodes. The main flow, admitPatientService, orchestrates calls to services contained within the mocks.mflow file, then ultimately creates and returns a response to the end user.

The ***mocks*** file contains the two supporting Web services, **PatientService** and **EHRService** which, respectively, query or create new EHRs, or query and create new episodes. It is within these flows that Mule uses content-based routing to process requests submitted by the Hospital Admissions SOA. In a real world scenario, these Web services would likely be owned and maintained by separate hospital admin groups, but for practical purposes we include both of these services in the same Mule project so as to enable end-to-end functionality when you run the app.

 

#####Hospital Admissions SOA


This file contains a flow and subflows which, together, form the hospital's patient pre-admission SOAP proxy Web service. The sections below offer descriptions of the actions of the flow and each subflow as they process end user requests. Refer to the diagram below to visualize the calls between flows, subflows, and other Web services.

#####AdmitPatientService Flow

This flow is the heart of the hospital's  pre-admissions Web service. Through an HTTP endpoint, it accepts a client request (i.e. request from a family doctor) as a SOAP envelope. The body of the SOAP envelope contains the XML which forms the entirety of the message, or the processing request. Then begins the service orchestration: using two Flow Ref message processors, this flow orchestrates calls, via subflows, to other hospital Web services to find or create patient EHRs and episodes.  Finally, when all pre-admission activities are complete, this flow uses an Anypoint DataWeave transformer to map data from the responses from the other hospital Web services to a response to send to the end user, including new information about the cost estimates for the patient's stay in the hospital.


There are several important configurations to take note of in this example application.

* **The XML is in the body**. Because the XML is contained in the body of the SOAP envelope, you can simply execute actions against the SOAP body, rather than having to dissect and detach the actual message payload from the SOAP envelope. To do this, the Proxy Service CXF component in the admitPatientService flow indicates that the message payload is just the body, not the whole SOAP envelope. Refer to the Visual Editor (below, left) and XML Editor (below, right) screenshots of the CXF component's configuration.

Similarly, the XML payload is in the body of the request the Proxy Client CXF components send to the PatientService and EHRService Web services. Again, this obviates the need to dissect the SOAP request and extract the relevant payload upon receipt.


* **The application separates tasks into subflows** Mule leverages the CXF framework to expose, consume and proxy Web services. Because CXF functions best with separate subflows to perform Web services calls, this application separates each Web service call into its own, small subflow.
 
* **The AdmissionService Web services was built WSDL-first**. The Web service's SOAP component includes four important attributes configured according to the table below.


## Mocks

This file contains two flows which act as two independent SOAP Web services within the hospital's internal network. Each service uses content-based routing to intelligently process Web service requests.

#####PatientService

This SOAP Web service accepts HTTP requests, then uses an xpath expression to extract one particular piece of information from the body – the operation – and set it as a variable. The message then encounters a Choice Router which uses MEL expressions to route the message depending upon its content, specifically, the new variable called operation. If the variable's value is upsertPatient, the router pushes the message into the upsertPatient DataWeave which prepares a response for the caller with a new patient ID; if the variable's value is anything other than upsertPatient, the router pushes the message into the getPatient DataWeave which prepares a response for the caller with the existing patient ID. Essentially, the first route creates a new patient record, the second locates existing. Note that as a mock flow, this service is simplified in order to facilitate functionality of the AdmissionService Web service; in a proper service, the flow would likely include calls to databases or other internal services to locate or create new records. 

The Choice Router directs messages according to the first expression that evaluates to true. In a more complex routing structure, a router may have to choose between several routes to perform any number of actions, for example, to delete a patient record or update an existing record. The router always evaluates against the MEL expression attribute of the when child elements in the order in which they appear in the config. See the visual editor (below, left) and XML editor (below, right) incarnations of the same choice router's configuration. (Default in the visual editor maps to otherwise in the XML editor.)
 

##### EHRService

Much the same as the PatientService Web service, the EHRService accepts HTTP requests, converts the data format and uses an xpath expression to set a variable on the message. The Choice Router then uses MEL expressions to evaluate the content of the newly set variable, then direct it to its corresponding pathway in the flow. Finally, it returns a response to the caller with information about the new or existing episode. Again, this service is simplified in order to facilitate functionality of the AdmissionService Web service; in a proper service, the flow would likely include calls to databases or other internal services to locate or create new records. 


###Documentation

Anypoint Studio includes a feature that enables you to easily export all the documentation you have recorded for your project. Whenever you want to easily share your project with others outside the Studio environment, you can export the project's documentation to print, email, or share online. Studio's auto-generated documentation includes:

* a visual diagram of the flows in your application
* the XML configuration which corresponds to each flow in your application
* he text you entered in the Notes tab of any building block in your flow

Follow [the procedure](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio#ImportingandExportinginStudio-ExportingStudioDocumentation) to export auto-generated Studio documentation.

 

###Go Further

* Learn more about the [Choice Router](http://www.mulesoft.org/documentation/display/current/Choice+Flow+Control+Reference).
* Learn more about the [CXF component](http://www.mulesoft.org/documentation/display/current/CXF+Component+Reference).
* Learn more about [DataWeave](https://developer.mulesoft.com/docs/display/current/DataWeave+Reference+Documentation).

# Salesforce to MySQL DB using Batch Processing #

This application illustrates how to use batch processing to synchronize Salesforce information with a database.

### Batch 

[Batch Processing](http://www.mulesoft.org/documentation/display/current/Batch+Processing) allows you to split a payload into individual elements to process each individually. This functionality is particularly useful when working with streaming input or when engineering "near real-time" data integration between SaaS applications.

### Database Connector 

 The [Database Connector](http://www.mulesoft.org/documentation/display/current/Database+Connector) provides a standardized way to access to any JDBC relational database, consistently using one same interface for every case. This connector allows you to run diverse SQL operations on your database, including Select, Insert, Update, Delete, and even Stored Procedures.

### Message Enriching

Mule uses [Message Enrichers](http://www.mulesoft.org/documentation/display/current/Message+Enricher) to enrich message payloads with data (i.e. add to the payload), rather than changing payload contents. Mule enriches a message’s payload so that other message processors in the application can access the original payload.

### Assumptions 

This document assumes that you are familiar with Mule ESB and the [Anypoint Studio interface](http://www.mulesoft.org/documentation/display/current/Anypoint+Studio+Essentials). To increase your familiarity with Studio, consider completing one or more [Anypoint Studio Tutorials](http://www.mulesoft.org/documentation/display/current/Basic+Studio+Tutorial). Further, this example assumes you are familiar with XML coding and that you have a basic understanding of [Mule flows](http://www.mulesoft.org/documentation/display/current/Mule+Application+Architecture) and SOAP as a Web service paradigm and the practice of WSDL-first Web service development.  

This document describes the details of the example within the context of Anypoint Studio, Mule ESB’s graphical user interface (GUI), and includes configuration details for both the visual and XML editors.

### Example Use Case 

This application queries a Salesforce account for new or updated contacts at a regular interval, then processes the returned payload one record at a time. It checks to see if a contact currently exists in the database, then updates or creates a new contact accordingly. Once the process is complete for the entire batch, a success message is logged.  

Although this use case could be met without using batch processing – treating the entire list of contacts returned by Salesforce as a whole – using batch makes this process more reliable as any errors that occur in single record will not propagate beyond record level.

### Set Up and Run the Example

Complete the following procedure to create, then run this example in your own instance of **Anypoint Studio**. You can create template applications straight out of the box in Anypoint Studio and tweak the configurations of this use case-based template to create your own customized applications in Mule.

Skip ahead to the next section if you prefer to simply examine this example via code snippets.

1. Open the Example project in Anypoint Studio from [Anypoint Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint+Exchange).
1. Set up **Salesforce credentials**:
	1. Log in to your Salesforce account. From your account menu (your account is labeled with your name), select **Setup**.
	2. In the left navigation bar, under the **Personal Setup** heading, click to expand the **My Personal Information** folder. 
	3. Click **Reset My Security Token**. Salesforce resets the token and emails you the new one.
	4. Access the email that Salesforce sent and copy the new token onto your local clipboard.
	5. Go to **Global Elements** tab.
	6. Double-click the Salesforce global element to open its **Global Element Properties** panel. In the **Security Token** field, paste the new Salesforce token you copied from the email. Alternatively, configure the global element in the XML Editor.
	7. Change the contents of the **Username** and **Password** fields to your account-specific values, then click **OK** to save your changes.
1. Create a **Database** and set up **credentials**:
	1. Create a new MySQL Database
	
		If you do not have a MySQL database available for your use, you can install MySQL on your local computer. Please visit dev.MySQL.com  to download and install a free version. It is a good idea to also install the MySQL workbench. Please also configure a MySQL username and password for use with this project.
	2. The project requires the following database configuration:
		- MySQL Database Schema: company
		- One table: contact
		- Three fields: email,  first_name,  last_name
		- One or more rows of data should be inserted into the table
		- A user that must have read and write access to this data.
	3. You can execute the following SQL statement to produce this schema and populate one row of data. See the green box below for tips on executing this SQL.
		
		```
		USE comapany;
		CREATE TABLE contact (
	    ID INT(11) NOT NULL AUTO_INCREMENT,
	    email varchar(255) NOT NULL,
	    first_name varchar(255) DEFAULT NULL,
	    last_name varchar(255) DEFAULT NULL,
		PRIMARY KEY (email)
		);
		INSERT INTO contacts VALUES (NULL, "leonardmule@mulesoft.com", "Leonard", "Mule");
		```
		
		If you are using the MySQL Workbench, first create the schema **company** , and then execute the above SQL statements from within the MySQL Workbench SQL Editor.

		Alternatively, you can use the MySQL command-line tool, as follows:
		
		1.	Execute this command: *CREATE DATABASE company;*. This will create the database schema.
		1. 		Save the above SQL code-block to a text file, **SQL_for_example.sql**
		1. 		Then execute the SQL with this command: mysql *company < SQL_for_example.sql*
		
		In either case, you may need to assign a user for access to the schema as well. Please see [MySQL Documentation](http://dev.mysql.com/doc/) for more information on using the MySQL Workbench or the command-line tool.
	4. Go to **Global Elements** tab.
	5. Double-click the Database global element to open its **Global Element Properties** panel. Alternatively, configure the global element in the XML Editor.
	6. Change the contents of the **Host**, **User** and **Password** fields to your account-specific values, then click **OK** to save your changes.
1. In the **Package Explorer**, right-click the connect-with-salesforce project name, then select **Run As > Mule Application**. Studio runs the application on the embedded server. 

### How It Works ###

Unlike typical Mule projects that are organized into Flows, this project runs a [Batch Process](http://www.mulesoft.org/documentation/display/current/Batch+Processing). The process is divided into three stages where actions have different scopes:

- Input	Polls: Salesforce at regular intervals for new contacts.
- On Complete:	Logs a success message.
- Process Records:	Checks if record exists in DB, then updates/creates DB record.

The Process Records stage is divided into two separate **batch steps**: the first step checks if the record exists in the DB, the second adds/updates these in the DB. If, while processing a record, the the first step fails, the second step does not process the failed record.

### Input ###

Every 60 seconds, the Poll scope triggers a new request to the Salesforce connector. The Salesforce connector is set to perform the query below, where the **timestamp flow variable** is periodically updated to the time of the last iteration of the poll:

	SELECT Email,FirstName,LastModifiedDate,LastName FROM Contact WHERE LastModifiedDate > #[flowVars['timestamp']]
 
The response returned by the Salesforce connector is a list of contacts.

### Process Records ###

The process records stage of the batch job process the records – each representing a single contact – one at a time. If one of these records fails, the entire task will not fail with it; Mule skips the record, moving on to process the next one.

#### Batch Step 1 ####

In this step, the DataWeave first renames the fields so that they match those in the database. The Database connector issues the following query to the database:

	SELECT first_name,last_name,email FROM contact WHERE email=#[payload.email]

Because the Database connector is inside a message enricher scope, Mule does not overwrite the payload with the response from the database query, rather, it adds the response to the message as an additional variable. Thus, all of the information that had originated from Salesforce is retained and can be passed on to the next step.

The message enricher creates two **record variables**:

- dbRecord: stores the response of the database query
- exists: indicates whether a contact already exists in the database, according to the response to the query

#### Batch Step 2 ####

Mule executes the second batch step only if the first step is successful. Depending on the value the message enricher stored in the flowVar exists (true - the contact exists; false - the contact does not exist) a choice router routes the flow to one of the following processing paths:

- exists = false: the contact must be added as a new contact. The following insert query is carried out in the database:
	
		INSERT INTO contact (first_name, last_name, email) VALUES (#[payload.first_name],#[payload.last_name],#[payload.email])
- exists = true: Mule populates the recordVar dbRecord. The following update query is carried out in the database:

		UPDATE contact SET first_name=#[payload.first_name],last_name=#[payload.last_name] WHERE email = #[payload.email]

- If neither of these conditions is met, an error has occurred, so Mule logs a message to announce this error.

#### On Complete 

The On Complete stage of the batch process executes once, after all of the records have been processed, whether successful, failed or skipped. In this case, a logger announces the completion of the task.

### Go Further 

- Learn more about the [Database Connector](http://www.mulesoft.org/documentation/display/current/Database+Connector).
- Understand [Batch Processing](http://www.mulesoft.org/documentation/display/current/Batch+Processing). 
- Learn more about the [DataWeave](https://developer.mulesoft.com/docs/display/current/Weave+Reference+Documentation).
- Learn about [Anypoint Connectors](http://www.mulesoft.org/documentation/display/current/Anypoint+Connectors).
- Understand the [Poll Scope](http://www.mulesoft.org/documentation/display/current/Poll+Reference).
- Read more about the [Choice Flow Control](http://www.mulesoft.org/documentation/display/current/Choice+Flow+Control+Reference).
- Read more about the [Message Enricher](http://www.mulesoft.org/documentation/display/current/Message+Enricher).
- Learn more about [DataSense Query Language](http://www.mulesoft.org/documentation/display/current/DataSense+Query+Language) to write queries in Mule connectors which support DSQL.

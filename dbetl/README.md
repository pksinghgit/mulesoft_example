# Batch processing of orders in a MySQL database
#

This example illustrates how to poll for a bunch of order records in a MySQL DB table, transform the data by merging the order record with more details from two other tables (SKU_DETAILS and ORDER_STATUS). The resultant record is then inserted into a new table called STATUS_REPORT
It is complementary to a developer blog series that gives step by step instructions to build out this example.

###Assumptions
###
This document describes the details of the example within the context of
Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). Where
appropriate, the XML configuration accompanies the Studio interface
screenshots. This document assumes that you are familiar with Mule ESB,
the [Anypoint Studio
interface](http://www.mulesoft.org/documentation/display/current/
Anypoint+Studio+Essentials), MySQL, the [database
connector](http://www.mulesoft.org/documentation/display/current/
Database+Connector) and
[Batch processing module]
(https://docs.mulesoft.com/mule-user-guide/v/3.7/batch-processing#sts=Batch Processing).

###Example Use Case
###
In this example, we will poll a database table for new orders every 10 seconds using the watermarking and polling capabilities 
of the Anypoint platform. We will then use the batch processing module to read process each record by matching the order entry with records from two other tables SKU and Order_Status.
Once the record is transformed by joining information from the two tables, the record is then loaded into the "Status_Report" table. 
  
###Set Up and Run the Example
###

1. **[Download and setup the MySQL
driver](http://dev.mysql.com/doc/refman/5.7/en/installing.html)** in
it's default location. You can even check out some of the YouTube videos
for assistance with this step.

2. **Start the MySQL server** from System Preferences

3. Open the Example project in Anypoint Studio from [Anypoint
Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint
+Exchange). 

4. Open src/main/resources/order.sql and run it in your mysql instance. This script will create a database called "ETL" and a number of tables load them with mock data.

5. Edit the src/main/app/mule-app.properties file to put the required MySQL configuration details.

6. Run the example application in Anypoint Studio or Standalone

7. Check whether the table "STATUS_REPORT" got populated with data.


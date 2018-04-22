# Poll for a CSV file, transform and encrypt the contents, and then SFTP the file
#

This example illustrates how to poll for a CSV file (extract), transform the data in the file into a
differnt format as needed by the target (transform), encrypt the data (secure) and then transfer the file
to a remote SFTP server.
It is complementary to a developer blog series that gives step by step instructions to build out this example.

###Assumptions
###
This document describes the details of the example within the context of
Anypoint™ Studio, Mule ESB’s graphical user interface (GUI). Where
appropriate, the XML configuration accompanies the Studio interface
screenshots. This document assumes that you are familiar with Mule ESB,
the [Anypoint Studio
interface](http://www.mulesoft.org/documentation/display/current/
Anypoint+Studio+Essentials), the [file
connector](https://docs.mulesoft.com/mule-user-guide/v/3.7/file-connector), the [data weave]
(https://docs.mulesoft.com/mule-user-guide/v/3.7/dataweave-reference-documentation), the [security module]
(https://docs.mulesoft.com/mule-user-guide/v/3.7/installing-anypoint-enterprise-security) and
[SFTP connector]
(https://docs.mulesoft.com/mule-user-guide/v/3.6/sftp-connector).

###Example Use Case
###
In this example, we poll for any new order records in the ORDERS table. We then use the batch processing module to read each order record. The SKU ID's in the order are used to perform a lookup in the SKU_DETAILS table, which returns the product information for the SKU's. The OrderID in the order is used to perform a lookup in the ORDER_STATUS table. After the lookups the data needs to be then inserted into a new table STATUS_REPORT.


###Set Up and Run the Example
###

1. Open the Example project in Anypoint Studio from [Anypoint
Exchange](http://www.mulesoft.org/documentation/display/current/Anypoint
+Exchange). 

2. Install the [Anypoint security module]
(https://docs.mulesoft.com/mule-user-guide/v/3.7/installing-anypoint-enterprise-security)

3. Create a Public key ring file and Secret key ring file along with the secret passphrase to be used for PGP encryption. Copy the two .gpg files into the src/main/resources folder.
Details on how these files can be created can be found [here]
(http://blogs.mulesoft.com/dev/mule-dev/using-pgp-security-explained-from-the-top/) 

4. Get the details for the target SFTP server to be used as the destination for the file

5. Put all the details gathered in the above steps inside src/main/app/mule-app.properties

6. Run the example application in Anypoint Studio or Standalone

7. Transfer the sample.csv file inside the src/main/resources/archive folder into src/main/resources/input

8. The file will be picked up and transferred to the configured SFTP endpoint.


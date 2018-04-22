/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MySQLDbCreator {
	
	private static final Logger log = LogManager.getLogger(MySQLDbCreator.class);
	private String databaseName;
	private String databaseUrl;
	private String databaseWithNameUrl;
	
	public MySQLDbCreator(String databaseName, String pathToProperties){
		final Properties props = new Properties();
		try {
			props.load(new FileInputStream(pathToProperties));
		} catch (Exception e) {
			log.error("Error occured while reading mule.test.properties", e);
		}
		final String user = props.getProperty("database.user");
		final String password = props.getProperty("database.password");
		final String dbUrl = props.getProperty("database.url");
		
		this.databaseName = databaseName;
		this.databaseUrl = dbUrl+"?user="+user+"&password="+password;
		this.databaseWithNameUrl = dbUrl+databaseName+"?rewriteBatchedStatements=true&user="+user+"&password="+password;
	}
	
	public String getDatabaseUrlWithName(){
		return databaseWithNameUrl;
	}
	
	public void setUpDatabase() {
		
		log.info("******************************** Populate MySQL DB **************************");
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// Get a connection
			conn = DriverManager.getConnection(databaseUrl);
			Statement stmt = conn.createStatement();
			
			stmt.addBatch("CREATE DATABASE "+databaseName);
			stmt.addBatch("USE "+databaseName);
			stmt.addBatch("CREATE USER 'generatedata'@'localhost' IDENTIFIED BY 'generatedata';");
			stmt.addBatch("GRANT ALL PRIVILEGES ON company.* TO generatedata@localhost IDENTIFIED BY 'generatedata';");
			stmt.addBatch("FLUSH PRIVILEGES;");
			stmt.addBatch("CREATE TABLE employees (\nno INT NOT NULL,\ndob DATE NOT NULL,\nfirst_name VARCHAR(14) NOT NULL,\nlast_name VARCHAR(16) NOT NULL,\ngender ENUM (\'M\',\'F\') NOT NULL,\nhire_date DATE NOT NULL,\nPRIMARY KEY (no)\n);\n");
			stmt.addBatch("CREATE TABLE roles (\nid mediumint(8) unsigned NOT NULL auto_increment,\nemp_no mediumint,\nrole varchar(255) default NULL,\nPRIMARY KEY (id)\n) AUTO_INCREMENT=1;\n");
			stmt.addBatch("INSERT INTO employees (no,dob,first_name,last_name,gender,hire_date) VALUES (1011,'1985-09-02','Chava','Puckett','F','2008-10-12');");
			stmt.addBatch("INSERT INTO employees (no,dob,first_name,last_name,gender,hire_date) VALUES (1066,'1971-10-21','Quentin','Puckett','F','08-09-15');");
			stmt.addBatch("INSERT INTO employees (no,dob,first_name,last_name,gender,hire_date) VALUES (1067,'1950-09-26','Mona','Sosa','M','07-11-27');");
			stmt.executeBatch();
			log.info("Success");
			
		} catch (SQLException ex) {
		    // handle any errors
		    log.error("SQLException: " + ex.getMessage());
		    log.error("SQLState: " + ex.getSQLState());
		    log.error("VendorError: " + ex.getErrorCode());
		} catch (Exception except) {
			except.printStackTrace();
		}
	}
	
	public void tearDownDataBase() {
		log.info("******************************** Delete Tables from MySQL DB **************************");
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(databaseUrl);
		
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("DROP SCHEMA "+databaseName);
			stmt.executeUpdate("drop user 'generatedata'@'localhost'");
		} catch (Exception except) {
			except.printStackTrace();
		}
	}
}

/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples.db;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
	private String pathToSqlScript;
	
	public MySQLDbCreator(String databaseName, String pathToSqlScript, String pathToProperties){
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
		this.pathToSqlScript = pathToSqlScript;
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
			FileInputStream fstream = new FileInputStream(pathToSqlScript);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			stmt.addBatch("CREATE DATABASE IF NOT EXISTS " + databaseName);
			stmt.addBatch("USE "+databaseName);
			stmt.addBatch("DROP TABLE IF EXISTS contact ");	
			String strLine;
			StringBuffer createStatement = new StringBuffer();
			// Specify delimiter according to sql file
			while ((strLine = br.readLine()) != null) {
				if (strLine.length() > 0) {
					strLine.replace("\n", "");
					createStatement.append(strLine);
				}
			}
			stmt.addBatch(createStatement.toString());
			in.close();
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
		} catch (Exception except) {
			except.printStackTrace();
		}
	}
}

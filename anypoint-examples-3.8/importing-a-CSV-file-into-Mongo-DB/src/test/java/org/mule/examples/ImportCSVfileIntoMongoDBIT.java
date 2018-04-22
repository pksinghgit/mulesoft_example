/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.util.IOUtils;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class ImportCSVfileIntoMongoDBIT extends FunctionalTestCase
{
	private static final String MAPPINGS_FOLDER_PATH = "./mappings";
	private static String DB_NAME = "customers";
	private static DB db;
    
	private static final String MONGO_USER = "test_user";
	private static final String MONGO_PASSWORD = "test_password";
	private static final String MONGO_HOST = "localhost";
    private static final int MONGO_PORT = 27017;
    
    private MongodExecutable mongodExe;
    private MongodProcess mongod;
    private Mongo mongo;
    
    @Override
    protected String getConfigResources()
    {
        return "csv-to-mongodb.xml";
    }

	@Before
    public void setup() throws Exception {
        MongodStarter runtime = MongodStarter.getDefaultInstance();
        mongodExe = runtime.prepare(new MongodConfig(Version.V2_0_5, MONGO_PORT, Network.localhostIsIPv6()));
        mongod = mongodExe.start();
        mongo = new MongoClient(MONGO_HOST, MONGO_PORT);        
        db = mongo.getDB(DB_NAME);
        db.addUser(MONGO_USER, MONGO_PASSWORD.toCharArray());
    }
    
    @BeforeClass
	public static void init() {
    	System.setProperty("database.user", MONGO_USER);
    	System.setProperty("database.password", MONGO_PASSWORD);
    	System.setProperty("database.name", DB_NAME);
    }
    
    @After
    public void teardown() {
    	db.command("{ dropUser: \"" + MONGO_USER + "\"}");
    	
        if (mongod != null) {
        	try{
        		mongod.stop();
        	}
        	catch (Exception e){
        		e.printStackTrace();
        	}
            mongodExe.stop();
        }
    }
    
    @Test
    public void sendCSVFile() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        String fileInputPath = "file://./src/main/resources/input";
        Map<String, Object> outboundProperties = new HashMap<String, Object>();
        outboundProperties.put("outputPattern", "input.csv");
        MuleMessage message = new DefaultMuleMessage(
        		IOUtils.getResourceAsString("input.csv", this.getClass()),
        		outboundProperties,
        		muleContext);
        client.dispatch(fileInputPath, message);        

        Thread.sleep(5000);
        
        DBCollection collection = db.getCollection(DB_NAME);
        assertEquals(1, collection.getCount());
    }
  
	@Override
	protected Properties getStartUpProperties() {
		Properties properties = new Properties(super.getStartUpProperties());

		String pathToResource = MAPPINGS_FOLDER_PATH;
		File graphFile = new File(pathToResource);

		properties.put(MuleProperties.APP_HOME_DIRECTORY_PROPERTY,
				graphFile.getAbsolutePath());

		return properties;
	}
}

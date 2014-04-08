package com.ibm.sbt.services;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * BaseUnitTest class to initialize context and Endpoint. All junits should
 * extend form this class and call initializeContext() and
 * authenticateEndpoint() from the setUp()
 * 
 * @author Vineet Kanwal
 * 
 */
public abstract class BaseUnitTest {

	private Application application;
	private Context context;
	protected Properties properties = new Properties();
	
	
	@Before
	public final void setUp() {
		RuntimeFactory runtimeFactory = new RuntimeFactoryStandalone();
		application = runtimeFactory.initApplication(null);
		context = Context.init(application, null, null);
		TestEnvironment.setRequiresAuthentication(true);
		loadProperties();
	}
	
	public void loadProperties(){
		if (properties.isEmpty()) {
			try {
				properties.load(new FileInputStream(new File(
						"config/test.properties")));
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * This method can be used to remove trailing numbers (random or Timestamp in millis)
	 * to assert the equality of a string compared against mocked data
	 * @param inputString
	 * @return
	 */
	protected String unRandomize(String inputString){
		return TestEnvironment.isMockMode()?inputString.trim().replaceAll("([0-9a-f]{8,12}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{8,12}|-?[0-9])*$", ""):inputString;
	}


	@After
	public final void tearDown() {
		Context.destroy(context);
		Application.destroy(application);
	}
	

}

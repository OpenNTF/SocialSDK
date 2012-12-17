package com.ibm.sbt.services;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.sbt.services.endpoints.Endpoint;

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

	public BaseUnitTest() {
		try {
			properties.load(new FileInputStream("config\\test.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Before
	public final void setUp() {
		RuntimeFactory runtimeFactory = new RuntimeFactoryStandalone();
		application = runtimeFactory.initApplication(null);
		context = Context.init(application, null, null);
	}

	@After
	public final void tearDown() {
		Context.destroy(context);
		Application.destroy(application);
	}

	public static void authenticateEndpoint(Endpoint endpoint, String user, String password) {
		try {
			Field userField = endpoint.getClass().getSuperclass().getDeclaredField("user");
			userField.setAccessible(true);
			userField.set(endpoint, user);
			Field passwordField = endpoint.getClass().getSuperclass().getDeclaredField("password");
			passwordField.setAccessible(true);
			passwordField.set(endpoint, password);
		} catch (SecurityException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
}

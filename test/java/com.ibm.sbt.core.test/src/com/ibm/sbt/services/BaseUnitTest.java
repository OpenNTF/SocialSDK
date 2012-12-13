package com.ibm.sbt.services;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import javax.servlet.ServletException;

import org.junit.BeforeClass;

import com.ibm.commons.runtime.Context;
import com.ibm.sbt.jslibrary.servlet.LibraryServlet;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.servlet.MockHttpServletRequest;
import com.ibm.sbt.servlet.MockHttpServletResponse;
import com.ibm.sbt.servlet.MockServletConfig;

/**
 * BaseUnitTest class to initialize context and Endpoint. All junits should
 * extend form this class and call initializeContext() and
 * authenticateEndpoint() from the setUp()
 * 
 * @author Vineet Kanwal
 * 
 */
public abstract class BaseUnitTest {
	
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

	@BeforeClass
	public static void setUp() {
		try {
			LibraryServlet servlet = new LibraryServlet();
			servlet.init(new MockServletConfig());
			MockHttpServletRequest httpRequest = new MockHttpServletRequest();
			MockHttpServletResponse httpResponse = new MockHttpServletResponse();
			Context.init(servlet.getApplication(), httpRequest, httpResponse);			
		} catch (ServletException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
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

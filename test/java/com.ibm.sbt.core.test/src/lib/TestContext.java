package lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.sbt.config.MockBasicEndpoint;
import com.ibm.sbt.config.MockOAuthEndpoint;

public class TestContext extends Context {
	
	protected TestContext(Application application) {
		super(application);
	
	}
	
	public static TestContext initDefaultTestingContext() {
		
		TestContext c=  TestContext.init(new TestRuntimeFactory().getApplicationUnchecked(), null , null);
		c.addBean("connections",new MockBasicEndpoint() );
		c.addBean("smartcloud", new MockOAuthEndpoint());
		return c;
	}

	public static TestContext init(Application a, Object request, Object response) {
		return (TestContext) Context.init(a, request, response);
	}
	
	@Override
	public String getCurrentUserId() {
		  // TODO Auto-generated method stub
		Logger.getAnonymousLogger().info("Implement this" + Thread.currentThread().getStackTrace()[2]);
		
		
		return "ANONYMOUS";
	}

	@Override
	public Object getRequest() {
		
		if (req == null)  Logger.getAnonymousLogger().severe("Call Context.init(...) for this test or set request manually");
		return req;
	}

	@Override
	public Object getResponse() {
		if (resp == null)  Logger.getAnonymousLogger().severe("Call Context.init(...) for this test or set response manually");	
		return resp;
	}

	@Override
	public String getProperty(String propertyName) {
		Logger.getAnonymousLogger().info("getting " + propertyName);
		
		return propertyMap.get(propertyName);
	}

	HashMap<String, String> propertyMap = new HashMap<String, String>();
	
	@Override
	public String getProperty(String propertyName, String defaultValue) {
		Logger.getAnonymousLogger().info("getting " + propertyName);
		
		return propertyMap.containsKey(propertyName) ? defaultValue : propertyMap.get(propertyName);
	}

	@Override
	public void setProperty(String propertyName, String value) {
		propertyMap.put(propertyName, value);

	}

	HashMap<String, Object> beanMap = new HashMap<String, Object>();
	private HttpServletResponse resp;
	private HttpServletRequest req;
	
	@Override
	public Object getBean(String beanName) {
		Logger.getAnonymousLogger().info("getting bean " + beanName);
		if (beanName == null) return null;
		
		if (!beanMap.containsKey(beanName))
			Logger.getAnonymousLogger().info("PLEASE ADD "+ beanName+ " Definition (can be defined null)");
		
		return beanMap.get(beanName);
	}

	@Override
	public void sendRedirect(String redirectUrl) throws IOException {
		// TODO Auto-generated method stub
		Logger.getAnonymousLogger().info("Implement this" + Thread.currentThread().getStackTrace()[2]);
	}

	@Override
	public String encodeUrl(String url) {
		// TODO Auto-generated method stub
		Logger.getAnonymousLogger().info("Implement this" + Thread.currentThread().getStackTrace()[2]);
		return null;
	}

	@Override
	protected Map<String, Object> createSessionMap() {
		// TODO Auto-generated method stub
		Logger.getAnonymousLogger().info("Implement this" + Thread.currentThread().getStackTrace()[2]);
		return null;
	}

	@Override
	protected Map<String, Object> createRequestMap() {
		// TODO Auto-generated method stub
		Logger.getAnonymousLogger().info("Implement this" + Thread.currentThread().getStackTrace()[2]);
		return null;
	}

	@Override
	protected Map<String, Object> createRequestParameterMap() {
		// TODO Auto-generated method stub
		Logger.getAnonymousLogger().info("Implement this" + Thread.currentThread().getStackTrace()[2]);
		return null;
	}

	@Override
	protected Map<String, Object> createRequestCookieMap() {
		// TODO Auto-generated method stub
		Logger.getAnonymousLogger().info("Implement this" + Thread.currentThread().getStackTrace()[2]);
		return null;
	}

	public void addBean(String string, Object bean) {
		this.beanMap.put(string, bean);
	}

	public void setResponse(HttpServletResponse response) {
		this.resp = response;
	}
	public void setRequest(HttpServletRequest request) {
		this.req = request;
	}
	

}

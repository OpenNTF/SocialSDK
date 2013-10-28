package lib;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.ibm.commons.runtime.Application;

public class TestApplication extends Application {
	
	protected TestApplication() {
		setName("Test Application");
	}
	
	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		Logger.getAnonymousLogger().info("Implement this" + Thread.currentThread().getStackTrace()[2]);
		return null;
	}

	@Override
	public Object getApplicationContext() {
		// TODO Auto-generated method stub
		Logger.getAnonymousLogger().info("Implement this" + Thread.currentThread().getStackTrace()[2]);
		return null;
	}

	@Override
	public Map<String, Object> getScope() {
		// TODO Auto-generated method stub
		Logger.getAnonymousLogger().info("Implement this" + Thread.currentThread().getStackTrace()[2]);
		return null;
	}

	Map<String, String> properties = new HashMap<String, String>();
	
	@Override
	public String getProperty(String name) {
		if (!properties.containsKey(name)) {
			Logger.getAnonymousLogger().info("ADD PROPERTY " + name + " value (it may be null)");
		}
		
		return properties.get(name);
	}

	@Override
	public void setProperty(String name, String value) {
		properties.put(name, value);
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		// TODO Auto-generated method stub
		Logger.getAnonymousLogger().info("Implement this" + Thread.currentThread().getStackTrace()[2]);
		return null;
	}

	final Map<String, List<Object>> services = new HashMap<String, List<Object>>();
	
	@Override
	public List<Object> findServices(String serviceName) {
		if (!services.containsKey(serviceName)) {
			Logger.getAnonymousLogger().severe("PLEASE ADD "+ serviceName+ " Definition (can be defined null)");
		}
		return services.get(serviceName);
	}
	
	public void addService(String serviceName, Object service) {
		List<Object> obj = services.get(serviceName);
		if (obj==null)obj=new LinkedList<Object>();
		obj.add(service);
		services.put(serviceName, obj);
	}

	public void cleanServices() {
		Logger.getAnonymousLogger().info("REMOVING ALL APPLICATION SERVICES");
		services.clear();
	}

}

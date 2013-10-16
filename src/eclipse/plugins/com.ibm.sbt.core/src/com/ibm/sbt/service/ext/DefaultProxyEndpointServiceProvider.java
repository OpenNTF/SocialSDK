package com.ibm.sbt.service.ext;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.sbt.service.basic.ProxyEndpointService;

/**
 * 
 * @author Vineet Kanwal
 *
 */
public class DefaultProxyEndpointServiceProvider extends ProxyEndpointServiceProvider {
	
	private static final String sourceClass = DefaultProxyEndpointServiceProvider.class.getName();
    private static final Logger logger = Logger.getLogger(sourceClass);

	private Map<String, String> fileProxyMap = new HashMap<String, String>();

	public DefaultProxyEndpointServiceProvider() {
		super();
		fileProxyMap.put("connections", "com.ibm.sbt.service.basic.ConnectionsFileProxyService");		
	}

	@Override
	public ProxyEndpointService createProxyEndpointService(String serviceType) {
		ProxyEndpointService proxyEndpointService = null;
		try {
			proxyEndpointService = (ProxyEndpointService) Class.forName(fileProxyMap.get(serviceType)).newInstance();
		} catch (InstantiationException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);			
		} catch (IllegalAccessException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return proxyEndpointService;
	}

}

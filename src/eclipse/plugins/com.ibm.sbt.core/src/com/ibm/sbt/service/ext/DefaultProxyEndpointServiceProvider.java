package com.ibm.sbt.service.ext;

import java.util.HashMap;
import java.util.Map;

import com.ibm.sbt.service.basic.ConnectionsFileProxyService;
import com.ibm.sbt.service.basic.ProxyEndpointService;

/**
 * 
 * @author Vineet Kanwal
 *
 */
public class DefaultProxyEndpointServiceProvider extends ProxyEndpointServiceProvider {

	private Map<String, ProxyEndpointService> fileProxyMap = new HashMap<String, ProxyEndpointService>();

	public DefaultProxyEndpointServiceProvider() {
		super();
		fileProxyMap.put("connections", new ConnectionsFileProxyService());		
	}

	@Override
	public ProxyEndpointService createProxyEndpointService(String serviceType) {
		ProxyEndpointService proxyEndpointService = fileProxyMap.get(serviceType);
		return proxyEndpointService;
	}

}

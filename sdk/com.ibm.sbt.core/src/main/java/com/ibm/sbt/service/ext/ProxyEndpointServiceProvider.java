package com.ibm.sbt.service.ext;

import com.ibm.sbt.service.basic.ProxyEndpointService;

/**
 * 
 * @author Vineet Kanwal
 *
 */
public abstract class ProxyEndpointServiceProvider {
	
	public static final String PROXY_SERVICE_TYPE = "com.ibm.sbt.service.proxyendpointservice.provider";
	
	public abstract ProxyEndpointService createProxyEndpointService(String serviceType);

}

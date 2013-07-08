/**
 * 
 */
package com.ibm.xsp.extlib.sbt.files.proxy.provider;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.service.basic.ProxyEndpointService;
import com.ibm.sbt.service.ext.ProxyEndpointServiceProvider;

/**
 * @author doconnor
 *
 */
public class XSPProxyEndpointProvider extends ProxyEndpointServiceProvider {
	

	/* (non-Javadoc)
	 * @see com.ibm.sbt.service.ext.ProxyEndpointServiceProvider#createProxyEndpointService(java.lang.String)
	 */
	@Override
	public ProxyEndpointService createProxyEndpointService(String endpointName) {
		if(StringUtil.equals(endpointName, "dropbox")){
			return new DropboxProxyEndpointService();
		}
		if(StringUtil.equals(endpointName, "connections")){
			return new ConnectionsFilesProxyEndPointService();
		}
		
		return null;
	}

}

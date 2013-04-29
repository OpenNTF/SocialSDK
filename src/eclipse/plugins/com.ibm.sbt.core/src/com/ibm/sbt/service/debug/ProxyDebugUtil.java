package com.ibm.sbt.service.debug;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Utility for capturing SDK traffic in a Network proxy client like Fiddler
 * <p>
 * </p>
 * @author Manish Kataria
 */

public class ProxyDebugUtil {
	
	private static final String DEFAULT_HOST = "127.0.0.1";
	private static final int DEFAULT_PORT = 8888;
	
	public static DefaultHttpClient wrapHttpClient(DefaultHttpClient client){
		return wrapHttpClient(client,DEFAULT_HOST,DEFAULT_PORT);
		
	}
	
	public static DefaultHttpClient wrapHttpClient(DefaultHttpClient client, String hostname, int port){
		HttpHost proxy = new HttpHost(hostname, port);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		return client;
		
	}

}

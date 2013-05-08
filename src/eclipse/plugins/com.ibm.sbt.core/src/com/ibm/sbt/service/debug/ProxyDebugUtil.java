package com.ibm.sbt.service.debug;

import java.util.StringTokenizer;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.commons.util.StringUtil;

/**
 * Utility for capturing SDK traffic in a Network proxy client like Fiddler
 * <p>
 * </p>
 * @author Manish Kataria
 */

public class ProxyDebugUtil {
	
//	private static final String DEFAULT_HOST = "127.0.0.1";
//	private static final int DEFAULT_PORT = 8888;
	
	public static DefaultHttpClient wrapHttpClient(DefaultHttpClient client,String httpProxy){
		
		if(StringUtil.isEmpty(httpProxy))
			return client;
		
		StringTokenizer proxydetails = new StringTokenizer(httpProxy,":");
		String host = proxydetails.nextToken();
		int port = Integer.parseInt(proxydetails.nextToken());
		return wrapHttpClient(client,host,port);
		
	}
	
	public static DefaultHttpClient wrapHttpClient(DefaultHttpClient client, String hostname, int port){
		HttpHost proxy = new HttpHost(hostname, port);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		return client;
		
	}

}

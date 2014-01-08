package com.ibm.sbt.service.proxy;

/**
 * Defines Proxy prefernces
 * <p>
 * Proxy stores preferences to be used by a Proxy implementation like ProxyUrl
 * </p>
 * @author Manish Kataria
 */

public abstract class Proxy {
	String proxyUrl;
	public abstract String getProxyUrl();
	public abstract String reWriteUrl(String apiUrl);

}

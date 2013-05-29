/*
 * © Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.service.basic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.service.core.handlers.ProxyHandler;
import com.ibm.sbt.service.debug.ProxyDebugUtil;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.services.util.SSLUtil;

/**
 * Basic proxy using an endpoint.
 * <p>
 * This proxy is using an endpoint name before the actual proxied URL and ensures that
 * the endpoint is defined for the current user. It also checks that the URL parameter is
 * the one defined by the endpoint.<br>
 * Finally, this proxy also ensures that the referer points to the the current server, thus
 * preventing pages without this referer to work.
 * </p>
 * @author Philippe Riand
 */
public class ProxyEndpointService extends ProxyService {

    protected Endpoint endpoint;
    protected String requestURI;
    
    public ProxyEndpointService() {
    }
    
    public Endpoint getEndpoint() {
        return endpoint;
    }
    
    protected String getProxyUrlPath() {
    	return ProxyHandler.URL_PATH;
    }

    @Override
    protected boolean isHeaderAllowed(String headerName) throws ServletException {
    	// Don't pass the basic authorization as this must be set by the endpoint
    	if(StringUtil.equals(headerName,"Authorization")) {
    		return false;
    	}
    	
    	if(!(this.getEndpoint().isHeaderAllowed(headerName, requestURI))){
    		return false;
    	}
    	// And pass all the other headers
        return super.isHeaderAllowed(headerName);
    }

    @Override
	protected DefaultHttpClient getClient(HttpServletRequest request, int timeout) {
        DefaultHttpClient httpClient = super.getClient(request, timeout);
        if(endpoint!=null) {
            if(endpoint.isForceTrustSSLCertificate() && requestURI!=null) {
            	httpClient = SSLUtil.wrapHttpClient(httpClient);
            }
            String httpProxy = getHttpProxy(endpoint);
            if(StringUtil.isNotEmpty(httpProxy)) {
            	httpClient = ProxyDebugUtil.wrapHttpClient(httpClient, httpProxy);
            }
        }
        return httpClient;
    }
    
    @Override
    protected void checkRequestAllowed(HttpServletRequest request) throws ServletException {
        if(!isRefererAllowed(request)) {
            throw new ServletException("Invalid request referer");
        }
        super.checkRequestAllowed(request);
    }
    
    protected boolean isRefererAllowed(HttpServletRequest request) {
    	// For now...
    	return true;
//        String referer = request.getHeader("Referer");
//        if(StringUtil.isNotEmpty(referer)) {
//            StringBuilder b = new StringBuilder(128);
//            b.append(request.getScheme());
//            b.append("://");
//            b.append(request.getServerName());
//            int port = request.getServerPort();
//            b.append(":");
//            b.append(Integer.toString(port));
//            b.append(request.getContextPath());
//            b.append("/");
//            String url = b.toString();
//            if(referer.startsWith(url)) {
//                return true;
//            }
//        }
//        return false;
    }
    
    @Override
    protected boolean prepareForwardingMethod(HttpRequestBase method, HttpServletRequest request, DefaultHttpClient client) throws ServletException {
        boolean ret = super.prepareForwardingMethod(method, request, client);
        initEndpoint(method, request, client, endpoint);
        return ret;
    }

    @Override
	protected boolean forwardCookies(HttpRequestBase method, HttpServletRequest request) {
    	return false;
    }
    
    protected void initEndpoint(HttpRequestBase method, HttpServletRequest request, DefaultHttpClient client, Endpoint endpoint) throws ServletException {
        try {
            endpoint.initialize(client);
            endpoint.updateHeaders(client, method);
        } catch(ClientServicesException ex) {
            throw new ServletException(ex);
        }
    }

	@Override
	protected void initProxy(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		String pathInfo = request.getPathInfo();
		// Skip the URL_PATH of the proxy
		//     /[proxy root]/[URL_PATH]/[endpointname]/[full qualified url starting with http] 
		// or
        //     /[proxy root]/[URL_PATH]/[endpointname]/[service url]
		int startEndPoint = getProxyUrlPath().length() + 2;
		if (startEndPoint < pathInfo.length()) {
			int startProxyUrl = pathInfo.indexOf('/', startEndPoint);
			if (startProxyUrl >= 0) {
				String endPointName = pathInfo.substring(startEndPoint, startProxyUrl);
				this.endpoint = EndpointFactory.getEndpoint(endPointName);
				if (!endpoint.isAllowClientAccess()) {
					throw new ServletException(StringUtil.format(
							"Client access forbidden for the specified endpoint {0}", endPointName));
				}
				
				String url = null;
                String endpointUrl = endpoint.getUrl();
				if (pathInfo.substring(startProxyUrl+1).startsWith("http")) {
				    url = pathInfo.substring(startProxyUrl+1).replaceFirst("\\/", "://");
				    /*
	                if (!url.startsWith(endpointUrl)) {
	                    throw new ServletException(StringUtil.format(
	                            "The proxied url does not correspond to the endpoint {0}", endPointName));
	                }
	                */
				} else {
				    url = endpointUrl + pathInfo.substring(startProxyUrl);
				}
				requestURI = url;
				return;
			}
		}
		StringBuffer b = request.getRequestURL();
		String q = request.getQueryString();
		if (StringUtil.isNotEmpty(q)) {
			b.append('?');
			b.append(q);
		}
		throw new ServletException(StringUtil.format("Invalid url {0}", b.toString()));
	}
    
    @Override
    protected String getRequestURIPath(HttpServletRequest request) throws ServletException {
        return requestURI;
    }
    

    public static String getProxyUrlForEndpoint(Context context, String proxyName, String endpointName, String url) {
        StringBuilder b = new StringBuilder();
        RuntimeConstants.get().appendBaseProxyUrl(b,context);
        b.append("/");
        b.append(proxyName);
        b.append("/");
        b.append(endpointName);
        if(StringUtil.isNotEmpty(url)) {
            b.append("/");
            String newUrl = url.replaceFirst("://","\\/");
            b.append(newUrl);
        }
        String proxyUrl = b.toString();
        return proxyUrl;
    }

    @Override
	protected String getRequestURLQueryString(HttpServletRequest request) throws ServletException {
		String queryargs = request.getQueryString();
		String proxyqueryargs = endpoint.getProxyQueryArgs();
		if (proxyqueryargs != null) {
			if (queryargs == null)
				return proxyqueryargs;
			else
				return queryargs + "&" + proxyqueryargs;
		}
		return queryargs;
	}
    
    /*
     * Return the HttpProxy value if configured
     */
    private String getHttpProxy(Endpoint endpoint) {
    	String httpProxy = endpoint.getHttpProxy();
		if (StringUtil.isEmpty(httpProxy)) {
			httpProxy = Context.get().getProperty("sbt.httpProxy");
		}
		return httpProxy;
    }
    
}
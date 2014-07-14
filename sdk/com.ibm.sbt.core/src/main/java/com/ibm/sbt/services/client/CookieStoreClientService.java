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
package com.ibm.sbt.services.client;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import com.ibm.sbt.services.endpoints.Endpoint;



/**
 * Generic REST service.
 * 
 * @author mwallace
 */
@SuppressWarnings("deprecation")
public class CookieStoreClientService extends ClientService {
	
	private DefaultHttpClient httpClient;
	
	static private CookieStore cookieStore = new BasicCookieStore();

    public CookieStoreClientService() {
    }
    
    public CookieStoreClientService(Endpoint endpoint) {
        super(endpoint);
    }
    
    public CookieStoreClientService(String endpointName) {
        super(endpointName);
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.services.client.ClientService#createHttpClient(org.apache.http.client.methods.HttpRequestBase, com.ibm.sbt.services.client.ClientService.Args)
     */
    @Override
    public synchronized DefaultHttpClient createHttpClient(HttpRequestBase httpRequestBase, Args args) throws ClientServicesException {
    	if (httpClient == null) {
    		httpClient = super.createHttpClient(httpRequestBase, args);
    	}
    	return httpClient;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.services.client.ClientService#initialize(org.apache.http.impl.client.DefaultHttpClient)
     */
    @Override
    protected void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
        super.initialize(httpClient);

        httpClient.addRequestInterceptor(new CookieInterceptor(), 1);
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.services.client.ClientService#execRequest(org.apache.http.client.methods.HttpRequestBase, com.ibm.sbt.services.client.ClientService.Args, java.lang.Object)
     */
    @Override
    protected Response execRequest(HttpRequestBase httpRequestBase, Args args, Object content) throws ClientServicesException {
    	Response response = super.execRequest(httpRequestBase, args, content);

    	CookieStore cookieStore = httpClient.getCookieStore();
    	List<Cookie> cookies = cookieStore.getCookies();
    	//System.out.println(hashCode() +" < "+cookies.size());
    	for (Cookie cookie : cookies) {
    		//System.out.println(cookie.getName()+"="+cookie.getValue());
    		CookieStoreClientService.cookieStore.addCookie(cookie);
    	}
    	
    	return response;
    }
    
    private class CookieInterceptor implements HttpRequestInterceptor {

		/* (non-Javadoc)
		 * @see org.apache.http.HttpRequestInterceptor#process(org.apache.http.HttpRequest, org.apache.http.protocol.HttpContext)
		 */
		@Override
		public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
			//System.out.println(CookieStoreClientService.this.hashCode() +" > "+cookieStore.getCookies().size());
			if (!cookieStore.getCookies().isEmpty()) {
				httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
				//CookieStore cookieStore = (CookieStore)httpContext.getAttribute(ClientContext.COOKIE_STORE);
		    	//List<Cookie> cookies = cookieStore.getCookies();
		    	//System.out.println("> "+cookies.size());
		    	//for (Cookie cookie : cookies) {
		    	//	System.out.println("> "+cookie.getName()+"="+cookie.getValue());
		    	//}
			}
		}
    	
    }
    
}

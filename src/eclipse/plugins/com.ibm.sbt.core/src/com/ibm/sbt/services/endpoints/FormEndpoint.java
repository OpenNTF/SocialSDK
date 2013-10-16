/*
 * © Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.endpoints;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.service.core.handlers.AuthCredsHandler;
import com.ibm.sbt.service.core.servlet.ServiceServlet;
import com.ibm.sbt.service.debug.ProxyDebugUtil;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.js.JSReference;
import com.ibm.sbt.services.util.SSLUtil;

/**
 * Bean that provides a Form based authentication.
 * @author Manish Kataria
 */

public abstract class FormEndpoint extends AbstractEndpoint {

	private String authenticationPage;
	private String user;
	private String password;
	
	// provides form page url where login should be performed
	public  String loginFormUrl;
	// For already authenticated users use this cookie cache, these cookies are persisted after initial form based authentication
	private String cookieCache;

	public String getCookieCache() {
		return cookieCache;
	}
	public String getLoginFormUrl() {
		return loginFormUrl;
	}

	public void setLoginFormUrl(String loginFormUrl) {
		this.loginFormUrl = loginFormUrl;
	}

	/*
	 * Converts list of cookies to string which can be added to request header
	 */
	public void setCookieCache(List<Cookie> cookies) {
		StringBuilder cookieBuilder = new StringBuilder();
		if(cookies !=null){
			for (Iterator<Cookie> iterator = cookies.iterator(); iterator.hasNext();) {
				Cookie cookie = iterator.next();
				cookieBuilder.append(cookie.getName() + "=" + cookie.getValue());
				cookieBuilder.append(";");
			}
		}
		cookieCache = cookieBuilder.toString();
	}
	
	/*
	 * Helper method for passing cookies directly as String
	 */
	public void setCookieCache(String cookies) {
		cookieCache = cookies;
	}

	@Override
	public boolean isAuthenticated() throws ClientServicesException {
		if (StringUtil.isEmpty(getCookieCache())) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void initialize(DefaultHttpClient httpClient)throws ClientServicesException {
		if (StringUtil.isNotEmpty(getCookieCache())) {
			HttpRequestInterceptor basicInterceptor = new CookieInterceptor(getCookieCache());
			httpClient.addRequestInterceptor(basicInterceptor, 0);
		}
	}

	private static class CookieInterceptor implements HttpRequestInterceptor {
		private String cookieCache;

		public CookieInterceptor(String cookieCache) {
			this.cookieCache = cookieCache;
		}

		@Override
		public void process(HttpRequest request, HttpContext context)
				throws HttpException, IOException {
			request.setHeader("Cookie", cookieCache);
		}
	}

	public boolean login(String user, String password)throws AuthenticationException {
		boolean validAuthentication = false;
		String requestUrl = getUrl();
		setUser(user);
		setPassword(password);

		try {
			if(!(getLoginFormUrl().startsWith("/"))){
				requestUrl = requestUrl.concat("/");
			}
			requestUrl = requestUrl.concat(getLoginFormUrl());
			BasicCookieStore cookieStore = new BasicCookieStore();
			DefaultHttpClient httpClient = new DefaultHttpClient();
			
			if(isForceTrustSSLCertificate()){
				httpClient = SSLUtil.wrapHttpClient(httpClient); // Configure httpclient to accept all SSL certificates
			}
			
			if (StringUtil.isNotEmpty(getHttpProxy())) {
				httpClient = ProxyDebugUtil.wrapHttpClient(httpClient, getHttpProxy()); // Configure httpclient to direct all traffic through proxy clients
			}

			httpClient.setCookieStore(cookieStore);
			HttpPost httpost = new HttpPost(requestUrl);
			List<NameValuePair> formParams = getLoginFormParameters(); // retrieve platform specific login parameters
			httpost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			HttpResponse resp = httpClient.execute(httpost);
			int code = resp.getStatusLine().getStatusCode();
			if (code == HttpStatus.SC_OK) {
				validAuthentication = true;
			}
			List<Cookie> cookies = cookieStore.getCookies();
			setCookieCache(cookies);

		} catch (IOException e) {
			throw new AuthenticationException(e,"FormEndpoint failed to authenticate");
		}
		return validAuthentication;

	}


    @Override
    /*
     * This method is exactly same as authenticate method from Basic endpoint, we need a login page here as well.
     */
	public void authenticate(boolean force) throws ClientServicesException {
        if(force || !isAuthenticated()) {
            String authPage = getAuthenticationPage();
         	Context context = Context.get();
            if(StringUtil.isNotEmpty(authPage)) {
            	try{
            		if(!UrlUtil.isAbsoluteUrl(authPage)){
            			authPage = UrlUtil.makeUrlAbsolute((HttpServletRequest)context.getHttpRequest(), authPage);
                	}

            		String redirectUrl = UrlUtil.getRequestUrl((HttpServletRequest)context.getHttpRequest());// change needed to handle portlethttprequest
            		
            	   	String endPointName = authPage.substring(authPage.indexOf("=")+1, authPage.length());
                	String baseUrl = UrlUtil.getBaseUrl(((HttpServletRequest)context.getHttpRequest()));
                	String servletPath = ServiceServlet.getServletPath();
                	String basicProxyUrl = AuthCredsHandler.URL_PATH;
                    
                	//constructing proxy action url
                	String postToProxy = PathUtil.concat(baseUrl, servletPath, '/');
                	postToProxy = PathUtil.concat(postToProxy,basicProxyUrl, '/');
                	postToProxy = PathUtil.concat(postToProxy,endPointName, '/');
                	postToProxy = PathUtil.concat(postToProxy,"JavaApp", '/');
                	
                	// encode URL's
		           	postToProxy = URLEncoder.encode(postToProxy,"UTF-8");
		         	redirectUrl = URLEncoder.encode(redirectUrl,"UTF-8");
                	
                	// passing proxy action url as a parameter to the authentication page
                	authPage = PathUtil.concat(authPage,"proxyPath",'&');
                	authPage = PathUtil.concat(authPage,postToProxy,'=');
                	// passing redirectURL as a parameter to the authentication page
                	authPage = PathUtil.concat(authPage,"redirectURL",'&');
                	authPage = PathUtil.concat(authPage,redirectUrl,'=');
                	context.sendRedirect(authPage);
                       
                    
            	} catch (IOException e) {
            		throw new ClientServicesException(e,"Authentication page not found. Could not redirect to login page");
            	}
            } else {
            	throw new ClientServicesException(null,"Authentication page is empty in the basic authentication bean");
            }
        }
    }
    
	@Override
	public void logout() throws AuthenticationException {
		// Clear out the cookie string
		setCookieCache("");
	}
	

	/*
	 * Should be overriden by specific endpoints like Smartcloud form endpoint or Connections form endpoint etc.
	 * Implementation should provide specific parameters which need to be passed along with authentication request.
	 */
	public abstract List<NameValuePair> getLoginFormParameters();
	
	/*
	 * Should be overriden by specific endpoints like Smartcloudformendpoint or Connectionsformendpoint etc.
	 * Implementation should provide Url where the form needs to be submitted.
	 */
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthenticationPage() {
		return authenticationPage;
	}

	public void setAuthenticationPage(String authenticationPage) {
		this.authenticationPage = authenticationPage;
	}
	
	@Override
	public String getHttpProxy() {
		String proxyinfo = super.getHttpProxy();
		if (StringUtil.isEmpty(proxyinfo)) {
			Context context = Context.getUnchecked();
			if (context != null) {
				proxyinfo = Context.get().getProperty("sbt.httpProxy");
			}
		}
		return proxyinfo;
	}
	
    @Override
    public JSReference getAuthenticator(String endpointName, String sbtUrl) {
    	JSReference reference = new JSReference("sbt/authenticator/Basic");
    	reference.getProperties().put("url", sbtUrl);
    	
    	return reference;
    }
    
    @Override
    public String getAuthType() {
     	return "form";
    }
}

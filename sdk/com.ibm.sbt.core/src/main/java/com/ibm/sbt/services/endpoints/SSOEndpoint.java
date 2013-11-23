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

package com.ibm.sbt.services.endpoints;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.protocol.HttpContext;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.service.core.handlers.AuthCredsHandler;
import com.ibm.sbt.service.core.servlet.ServiceServlet;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.js.JSReference;

/**
 * Bean that provides a authentication using an LTPA token.
 * 
 * @author Philippe Riand
 * @author Niklas Heidloff
 */
public class SSOEndpoint extends AbstractEndpoint {

    private static final long serialVersionUID = 1L;
    
    private String authenticationPage = null;
    

    public SSOEndpoint() {
    }

    @Override
	public boolean isAuthenticated() throws ClientServicesException {
        return true;
    }

    @Override
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
              		throw new ClientServicesException(null,"LTPA token expired or invalid. Cannot refresh: authentication page is not set");
              	}
              } else {
              	throw new ClientServicesException(null,"LTPA token expired or invalid. Cannot refresh: authentication page is not set");
              }
          }
    }

    @Override
    public JSReference getAuthenticator(String endpointName, String sbtUrl) {
    	JSReference reference = new JSReference("sbt/authenticator/SSO");
    	reference.getProperties().put("url", sbtUrl);
    	return reference;
    }
    
    public void redirect() {
    }

    @Override
	public void initialize(DefaultHttpClient httpClient) {
        HttpRequestInterceptor ltpaInterceptor = new LtpaInterceptor(getUrl());
        httpClient.addRequestInterceptor(ltpaInterceptor, 0);
    }

    private static class LtpaInterceptor implements HttpRequestInterceptor {

        String _domain;

        public LtpaInterceptor(String url) {
        	try {
        		URL u = new URL(url);
        		_domain = u.getHost();
        		//handles ipv6 hosts
        		if (_domain.startsWith("[") && _domain.endsWith("]")) {
        			_domain = _domain.substring(0, _domain.length()-1);
        		}
        		
        	} catch (Exception e ) {
	        	//fall back for incomplete urls
	            _domain = url.substring(url.indexOf("//")+2);
	            if(_domain.indexOf(":")!=-1) {
	                _domain = _domain.substring(0, _domain.indexOf(":"));
	            }
        	}
        }
        
        
        public String getRawCookieValue(javax.servlet.http.Cookie cookie, HttpServletRequest request) {
        	String co = request.getHeader("cookie");
        	StringTokenizer st = new StringTokenizer(co, "; ");
        	while (st.hasMoreTokens()) {
        		String cs = st.nextToken();
        		if (cs.substring(0, cs.indexOf('=')).equals(cookie.getName())) {
        			String value =  cs.substring(cs.indexOf('=')+1);
        		return value;
        		}
    		}
        	return cookie.getValue();
        }

        @Override
		@SuppressWarnings("unchecked")
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {

            CookieStore cookieStore;
            cookieStore = new BasicCookieStore();

            Context ctx = Context.get();

            java.util.Map<java.lang.String, java.lang.Object> cookieMap = ctx.getRequestCookieMap();
            if(cookieMap.containsKey("LtpaToken")) {
                javax.servlet.http.Cookie cookie = (javax.servlet.http.Cookie) cookieMap.get("LtpaToken");
                BasicClientCookie2 cookie1 = new BasicClientCookie2(cookie.getName(), getRawCookieValue(cookie, ctx.getHttpRequest()));
                if(cookie.getDomain()!=null) {
                    cookie1.setDomain(cookie.getDomain());
                }
                else {
                    cookie1.setDomain(_domain);
                }
                if(cookie.getPath()!=null) {
                    cookie1.setPath(cookie.getPath());
                }
                else {
                    cookie1.setPath("/");
                }
                cookieStore.addCookie(cookie1);
            }

            if(cookieMap.containsKey("LtpaToken2")) {
                javax.servlet.http.Cookie cookie = (javax.servlet.http.Cookie) cookieMap.get("LtpaToken2");
                BasicClientCookie2 cookie2 = new BasicClientCookie2(cookie.getName(), getRawCookieValue(cookie, ctx.getHttpRequest()));
                if(cookie.getDomain()!=null) {
                    cookie2.setDomain(cookie.getDomain());
                }
                else {
                    cookie2.setDomain(_domain);
                }
                if(cookie.getPath()!=null) {
                    cookie2.setPath(cookie.getPath());
                }
                else {
                    cookie2.setPath("/");
                }
                cookieStore.addCookie(cookie2);
            }

            context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        }
    }

	@Override
	public void logout() throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}
	
	public String getAuthenticationPage() {
		return authenticationPage;
	}
	
	public void setAuthenticationPage(String authenticationPage) {
		this.authenticationPage = authenticationPage;
	}
	
}
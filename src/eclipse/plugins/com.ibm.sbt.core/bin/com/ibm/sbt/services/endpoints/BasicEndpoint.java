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
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HttpContext;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.password.PasswordException;
import com.ibm.sbt.security.authentication.password.consumer.UserPassword;
import com.ibm.sbt.security.authentication.password.consumer.store.PasswordStore;
import com.ibm.sbt.security.authentication.password.consumer.store.PasswordStoreFactory;
import com.ibm.sbt.service.core.handlers.BasicAuthCredsHandler;
import com.ibm.sbt.service.core.servlet.ServiceServlet;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.js.JSReference;
import com.ibm.sbt.services.util.AnonymousCredentialStore;


/**
 * Bean that provides a basic authentication.
 * <p>
 * </p>
 * @author Philippe Riand
 */
public class BasicEndpoint extends AbstractEndpoint {

    // Key used by the bean when it needs to redirect
    public static final String REDIRECT_PAGE_KEY    = "xsp.endpoint.redirectpage";
    
    private String user;
    private String password;
    private String authenticationPage;
    private String passwordStore;
    private boolean storeAlreadyTried;
    
    public BasicEndpoint() {
    }
    public BasicEndpoint(String user, String password, String authenticationPage) {
        this.user = user;
        this.password = password;
        this.authenticationPage = authenticationPage;
    }

    @Override
    public JSReference getAuthenticator(String endpointName) {
    	JSReference reference = new JSReference("sbt/authenticator/Basic");
    	return reference;
    }

    @Override
    public String getUserIdentity() throws ClientServicesException {
        try {
            String u = getUser();
            if(StringUtil.isEmpty(u)) {
                readFromStore();
                u = getUser();
            }
            return u;
        } catch(PasswordException ex) {
            throw new ClientServicesException(ex);
        }
    }

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
    public String getAuthType() {
     	return "basic";
    }
    
    @Override
	public boolean isAuthenticated() throws ClientServicesException {
        try {
            String u = getUser();
            if(StringUtil.isEmpty(u)) {
                return readFromStore();
            }
            return true;
        } catch(PasswordException ex) {
            throw new ClientServicesException(ex);
        }
    }
    

    public String getPasswordStore() {
        return passwordStore;
    }
    public void setPasswordStore(String passwordStore) {
        this.passwordStore = passwordStore;
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
                	String basicProxyUrl = BasicAuthCredsHandler.URL_PATH;
                    
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
            		throw new ClientServicesException(null,"Authentication page not found. Could not redirect to login page");
			}
            }
            else
            	throw new ClientServicesException(null,"Authentication page is empty in the basic authentication bean");
        }
    }

    public boolean readFromStore() throws PasswordException {
        if(!storeAlreadyTried) {
            synchronized (this) {
            	Context context = Context.get();
                UserPassword u = null;
            	if(!context.isCurrentUserAnonymous()) {
	                PasswordStore pwd = PasswordStoreFactory.getPasswordStore(getPasswordStore());
	                if(pwd!=null) {
	                    u = pwd.loadUserPassword(getUrl());
	                }
            	} else {
            		u = (UserPassword)AnonymousCredentialStore.loadCredentials(context,getUrl(),null);
            	}
                if(u!=null) {
                    this.user = u.getUser();
                    this.password = u.getPassword();
                    return true;
                }
                storeAlreadyTried = true;
            }
        }
        return false;
    }

    public boolean writeToStore() throws PasswordException {
    	Context context = Context.get();
    	if(!context.isCurrentUserAnonymous()) {
	        PasswordStore pwd = PasswordStoreFactory.getPasswordStore(getPasswordStore());
	        if(pwd!=null) {
	            pwd.saveUserPassword(getUrl(), getUser(), getPassword());
	            return true;
	        }
    	} else {
    		AnonymousCredentialStore.storeCredentials(context,new UserPassword(getUrl(),getUser(),getPassword()),getUrl(),null);
    	}
        return false;
    }

    public boolean clearFromStore() throws PasswordException {
    	Context context = Context.get();
    	if(!context.isCurrentUserAnonymous()) {
	        PasswordStore pwd = PasswordStoreFactory.getPasswordStore(getPasswordStore());
	        if(pwd!=null) {
	            pwd.deleteUserPassword(getUrl());
	            return true;
	        }
    	} else {
    		AnonymousCredentialStore.deleteCredentials(context,getUrl(),null);
    	}
        return false;
    }
    
    public boolean login(String user, String password) throws PasswordException {
        return login(user,password,false);
    }

    public boolean login(String user, String password, boolean writeToStore) throws PasswordException {
        setUser(user);
        setPassword(password);
        if(!isAuthenticationValid()) {
            return false;
        }
        if(writeToStore) {
            writeToStore();
        } else {
            clearFromStore();
        }
        // How can we verify the user is properly authenticated?
        return true;
    }

    public void logout() throws PasswordException {
        setUser(null);
        setPassword(null);
        clearFromStore();
    }
    
    public void redirect() throws PasswordException {
        Context context = Context.get();

        String nextPage = (String)context.getSessionMap().get(REDIRECT_PAGE_KEY);
        if(StringUtil.isNotEmpty(nextPage)) {
        	// TODO
//            context.getExternalContext().getSessionMap().remove(REDIRECT_PAGE_KEY);
//            XSPContext ctx = XSPContext.getXSPContext(FacesContext.getCurrentInstance());
//            ctx.redirectToPage(nextPage, true);
        }
    }
    
    @Override
	public void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
        String usr = getUserIdentity();
        if(StringUtil.isNotEmpty(usr)) {
            String pwd = getPassword();
            
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(usr,pwd);
            
            HttpRequestInterceptor basicInterceptor = new BasicInterceptor(creds);
            httpClient.addRequestInterceptor(basicInterceptor, 0);
        }
    }
    

    private static class BasicInterceptor implements HttpRequestInterceptor {
        
        private UsernamePasswordCredentials credentials;

        public BasicInterceptor(UsernamePasswordCredentials credentials) {
            this.credentials = credentials;
        }

        @Override
		public void process(HttpRequest request, HttpContext context)throws HttpException, IOException {
            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
            if (authState != null && authState.getAuthScheme() == null) {
                AuthScheme scheme = new BasicSchemeFactory().newInstance(new BasicHttpParams());
                authState.setAuthScheme(scheme);
                authState.setCredentials(credentials);
            }
        }
    }    
}

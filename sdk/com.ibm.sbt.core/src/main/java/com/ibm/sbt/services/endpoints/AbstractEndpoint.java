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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.service.core.handlers.ProxyHandler;
import com.ibm.sbt.service.proxy.Proxy;
import com.ibm.sbt.service.proxy.ProxyConfigException;
import com.ibm.sbt.service.proxy.ProxyFactory;
import com.ibm.sbt.services.client.AuthenticationService;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.ClientServiceListener;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.GenericService;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.endpoints.js.JSReference;
import com.ibm.sbt.util.SBTException;


/**
 * Abstract server end point.
 * <p>
 * </p>
 * @author Philippe Riand
 */
public abstract class AbstractEndpoint implements Endpoint, Cloneable {

    private String appKey;

    private Map<String, String> serviceMappings = new HashMap<String, String>();
    private String url;
    private String name;
    private String label;
    private String dialogLoginPage;
    private String loginPage;
    private String loginUi;
    private String autoAuthenticate;
    private String authenticationService;
    private String clientServiceClass;
    private String apiVersion;
    private String defaultApiVersion = "4.0";
    private String credentialStore;
    private boolean requiresAuthentication;
    private boolean forceTrustSSLCertificate;
    private boolean forceDisableExpectedContinue;
    private String httpProxy;
    private String proxyConfig;
    private boolean allowClientAccess = true;
    private boolean useProxy = true;
    private ClientServiceListener listener;
    
    protected Map<String, Object> clientParams = new HashMap<String, Object>();
    
    private int authenticationErrorCode = 401;
    private CookieStore cookieStore = new BasicCookieStore();
    
    private boolean enableCookies = false;
    
    protected static final String PLATFORM_CONNECTIONS = "connections";
    protected static final String PLATFORM_SMARTCLOUD = "smartcloud";
    protected static final String PLATFORM_DOMINO = "domino";
    protected static final String PLATFORM_SAMETIME = "sametime";
    protected static final String PLATFORM_DROPBOX = "dropbox";
    protected static final String PLATFORM_TWITTER = "twitter";
	
	//Adds support for the API Management feature 
	private static final String HEADER_APIM_SESSION_ID = "apim-session-id";
	private String apiSessionId = "";
	
	//Adds support for the X-LConn-RunAs header
	public static final String HEADER_X_LCONN_RUNAS = "X-LConn-RunAs";
	prviate static final int AVG_HEADER_COUNT = 2;
	private java.util.HashMap<String,String> headers = new java.util.HashMap<String,String>(AVG_HEADER_COUNT);
    
    public AbstractEndpoint() {
    }
	
	/**
	 * clears the headers which are used to make requests for the endpoint
	 */
	public void clearHeaders(){
		headers.clear();
	}
	
	/**
	 * the map with the headers
	 * 
	 * @return {java.util.HashMap<String,String>} header 
	 */
	public java.util.HashMap<String,String> getHeaders(){
		return headers;
	}
	
	/**
	 * add the header with the name:value to the headers map
	 */
	 public void addHeader(String headerName, String headerValue){
		 headers.put(headerName,headerValue);
	 }
	
	/**
	 * removes the header with the given name
	 */
	public void remove(String headerName){
		headers.remove(headerName);
	}
	
	/** 
	 * sets the api session id 
	 */
	public void setSessionId(String sessionId){
		this.apiSessionId = sessionId;
	}
	
	/**
	 * gets the session id for the given endpoint and the user
	 * @return {String} the session id for the given user
	 */
	 public String getSessionId(){
		 return apiSessionId;
	 }
	 /**
	  * checks for a given api-session-id on a response, and updates the endpoint with the given id. 
	  * @return {Response} the Response from the API call, and updates the Endpoint
	  */
	 public Response processSessionId(Response response){
		 String val = response.getResponseHeader(HEADER_APIM_SESSION_ID);
		 if(val != null){
			 this.setSessionId(val);
		 }
		 return response;
	 }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.services.endpoints.Endpoint#setListener(com.ibm.sbt.services.client.ClientServiceListener)
     */
    @Override
    public void setListener(ClientServiceListener listener) {
    	this.listener = listener;
    }
    
    @Override
	public void checkValid() throws SBTException {
    	if(StringUtil.isEmpty(getUrl())) {
    		throw new SBTException(null,"The Endpoint url is empty for {0}",getClass());
    	}
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.services.endpoints.Endpoint#getClientParams()
     */
    @Override
    public Map<String, Object> getClientParams() {
    	return clientParams;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.services.endpoints.Endpoint#getPlatform()
     */
    @Override
    public String getPlatform() {
    	return null;
    }
    
    @Override
    public Endpoint clone() throws CloneNotSupportedException {
       return (Endpoint)super.clone();
    }

    @Override
	public String getUserIdentity() throws ClientServicesException {
        return null;
    }

    @Override
	public boolean isRequiresAuthentication() throws ClientServicesException {
        return requiresAuthentication;
    }
    
    @Override
	public boolean isHeaderAllowed(String headerName, String serviceUrl){
    	return true;
    }
	@Override
	public int getAuthenticationErrorCode(){
    	return authenticationErrorCode;
    }


	public void setAuthenticationErrorCode(int code){
    	authenticationErrorCode = code;
    }
    public void setRequiresAuthentication(boolean requiresAuthentication) throws ClientServicesException {
        this.requiresAuthentication = requiresAuthentication;
    }

    @Override
	public boolean isAuthenticationValid() {
        try {
            if(isAuthenticated()) {
                String authSvc = getAuthenticationService();
                if(StringUtil.isNotEmpty(authSvc)) {
                    // Emit a request to this URL and get the return code
                    AuthenticationService as = new AuthenticationService(this);
                    return as.isValidAuthentication(authSvc);
                }
                return true;
            }
        } catch (ClientServicesException e) {
        }
        return false;
    }
    
    @Override
	public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
	
	/**
	 * returns the appkey from the managed-bean or endpoint. 
	 * @return {String} 
	 */
	public String getAppKey(){
		return appKey;
	}
	
	/**
	 * sets the given appKey for the Endpoint 
	 * @param appKey the given appKey that is to be used in the ClientService
	 */
	public void setAppKey(String appKey){
		this.appKey = appKey;
	}
	
    /**
     * returns the Endpoint bean name
     * 
     */
	public String getName() {
        return name;
    }
	 /**
     * sets the Endpoint beanName, used in EndPointFactory class to set the name of Endpoint bean
     * 
     */
    public void setName(String name) {
        this.name = name;
    }
    @Override
	public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.services.endpoints.Endpoint#getApiVersion()
     */
    @Override
    public String getApiVersion() {
        return StringUtil.isNotEmpty(apiVersion)?this.apiVersion:this.defaultApiVersion;
    }
    
    /**
     * @param apiVersion the apiVersion to set
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.services.endpoints.Endpoint#isAllowClientAccess()
     */
    @Override
    public boolean isAllowClientAccess() {
    	return allowClientAccess;
    }
    
    /**
	 * @param allowClientAccess the allowClientAccess to set
	 */
	public void setAllowClientAccess(boolean allowClientAccess) {
		this.allowClientAccess = allowClientAccess;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.services.endpoints.Endpoint#isUseProxy()
	 */
	@Override
	public boolean isUseProxy() {
		return useProxy;
	}
	
	/**
	 * Set to true if this endpoint should use the proxy.
	 * @param useProxy
	 */
	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}
	
	public String getProxyConfig() {
		return proxyConfig;
	}

	public void setProxyConfig(String proxyConfig) {
		this.proxyConfig = proxyConfig;
	}
    
    @Override
	public String getDialogLoginPage() {
        return dialogLoginPage;
    }
    public void setDialogLoginPage(String dialogLoginPage) {
        this.dialogLoginPage = dialogLoginPage;
    }
    
    @Override
	public String getLoginPage() {
        return loginPage;
    }
    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }
    
    @Override
    public String getLoginUi() {
        return loginUi;
    }
    public void setLoginUi(String loginUi) {
        this.loginUi = loginUi;
    }
    
    @Override
    public String getAutoAuthenticate() {
        return autoAuthenticate;
    }
    public void setAutoAuthenticate(String autoAuthenticate) {
        this.autoAuthenticate = autoAuthenticate;
    }

	public String getClientServiceClass() {
        return clientServiceClass;
    }
    public void setClientServiceClass(String clientServiceClass) {
        this.clientServiceClass = clientServiceClass;
    }
    
	public String getCredentialStore() {
        return credentialStore;
    }
    public void setCredentialStore(String credentialStore) {
        this.credentialStore = credentialStore;
    }

    @Override
    public JSReference getAuthenticator(String endpointName, String sbtUrl) {
    	return null;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.services.endpoints.Endpoint#getTransport(java.lang.String, java.lang.String)
     */
    @Override
    public JSReference getTransport(String endpointName, String moduleId) {
    	return new JSReference(moduleId);
    }
    
    /* (non-Javadoc)
     * Returns the proxy's module id from the proxyconfig of this endpoint, if it exists.
     * 
     * Returns moduleId if the endpoint has no proxyconfig.
     * 
     * @see com.ibm.sbt.services.endpoints.Endpoint#getProxy(java.lang.String, java.lang.String)
     */
    @Override
    public JSReference getProxy(String endpointName, String moduleId) {
    	String proxyModuleId = moduleId;
    	Proxy proxy = null;
    	
    	if(this.getProxyConfig() != null){
    		try {
    			proxy = ProxyFactory.getProxyConfig(this.getProxyConfig());
    			if(proxy != null && proxy.getProxyModule() != null){
    				proxyModuleId = proxy.getProxyModule();
    			}
    		} catch (ProxyConfigException ex) {
    			Logger logger = Logger.getLogger(AbstractEndpoint.class.getName());
    			if (logger.isLoggable(Level.SEVERE)) {
    				logger.severe(ex.getMessage());
    			}
    		}
    	}
    	
    	return new JSReference(proxyModuleId);
    }

    @Override
    public String getProxyPath(String endpointName) {
    	return endpointName;
    }
    
    @Override
	public String getAuthType(){
    	return null; 
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.services.endpoints.Endpoint#getProxyHandlerPath()
     */
    @Override
    public String getProxyHandlerPath() {
    	return ProxyHandler.URL_PATH;
    }
    
    public String getAuthenticationService() {
        return authenticationService;
    }
    public void setAuthenticationService(String authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
	public boolean isForceTrustSSLCertificate() {
        return forceTrustSSLCertificate;
    }
    public void setForceTrustSSLCertificate(boolean forceTrustSSLCertificate) {
        this.forceTrustSSLCertificate = forceTrustSSLCertificate;
    }
        


    @Override
	public boolean isForceDisableExpectedContinue() {
        return forceDisableExpectedContinue;
    }
    public void setForceDisableExpectedContinue(boolean forceDisableExpectedContinue) {
        this.forceDisableExpectedContinue = forceDisableExpectedContinue;
    }
    
    @Override
	public String getHttpProxy(){
		return httpProxy;
	}

	public void setHttpProxy(String httpProxy) {
		this.httpProxy = httpProxy;
	}
    
    //
    // Client service access
    //
    @Override
	public Response xhr(String method, ClientService.Args args, Object content) throws ClientServicesException {
    	ClientService srv = getClientService();
		Response res = srv.xhr(method,args,content);
    	return processSessionId(res);
    }
    @Override
	public ClientService getClientService() throws ClientServicesException {
    	// If the client service class is defined, then we instanciate it
    	String cls = getClientServiceClass();
    	if(StringUtil.isNotEmpty(cls)) {
    		try {
    			ClientService clientService = null;
    			// order of precedence for the classloader to use 
    			Context ctx = Context.getUnchecked();
    			Application app = Application.getUnchecked();
    			ClassLoader cl = Thread.currentThread().getContextClassLoader();
    			if (ctx!=null) {
    				clientService = (ClientService)ctx.getClassLoader().loadClass(cls).newInstance();
    			} else if(app!=null) {
    				clientService = (ClientService)app.getClassLoader().loadClass(cls).newInstance();
    			} else if(cl!=null) {
    				clientService = (ClientService)cl.loadClass(cls).newInstance();
    			} else {
    				clientService = (ClientService)Class.forName(cls).newInstance();
    			}
    			// set endpoint
    			clientService.setEndpoint(this);
    			clientService.setListener(this.listener);
    			return clientService;
   		} catch(Exception ex) {
    			throw new ClientServicesException(ex,"Cannot create ClientService class {0}",cls);
    		}
    	}
    	ClientService clientService = new GenericService(this);
    	clientService.setListener(this.listener);
		return clientService;
    }

    @Override
	public Response xhrGet(String serviceUrl) throws ClientServicesException {
		Response res = getClientService().get(serviceUrl); 
    	return processSessionId(res);
    }
    @Override
	public Response xhrGet(String serviceUrl, Map<String, String> parameters) throws ClientServicesException {
		Response res = getClientService().get(serviceUrl, parameters);
    	return processSessionId(res);
    }
    @Override
	public Response xhrGet(String serviceUrl, Map<String, String> parameters, Handler format) throws ClientServicesException {
		Response res = getClientService().get(serviceUrl, parameters, format);
    	return processSessionId(res);
    }
    @Override
    public Response xhrGet(Args args) throws ClientServicesException {
		Response res = getClientService().get(args);
    	return processSessionId(res);
    }
    
    @Override
	public Response xhrPost(String serviceUrl, Object content) throws ClientServicesException {
		Response res = getClientService().post(serviceUrl, content);
    	return processSessionId(res);
    }
    @Override
	public Response xhrPost(String serviceUrl, Map<String, String> parameters, Object content) throws ClientServicesException {
    	Response res = getClientService().post(serviceUrl, parameters, content);
		return processSessionId(res);
    }
    @Override
	public Response xhrPost(String serviceUrl, Map<String, String> parameters, Object content, Handler format) throws ClientServicesException {
		Response res = getClientService().post(serviceUrl, parameters, content, format);
    	return processSessionId(res);
    }
    @Override
    public Response xhrPost(Args args, Object content) throws ClientServicesException {
		Response res = getClientService().post(args, content);
    	return processSessionId(res);
    }

    @Override
	public Response xhrPut(String serviceUrl, Object content) throws ClientServicesException {
    	Response res = getClientService().put(serviceUrl, content);
		return processSessionId(res);
    }
    @Override
	public Response xhrPut(String serviceUrl, Map<String, String> parameters, Object content) throws ClientServicesException {
		Response res = getClientService().put(serviceUrl, parameters, content);
    	return processSessionId(res);
    }
    @Override
	public Response xhrPut(String serviceUrl, Map<String, String> parameters, Object content, Handler format) throws ClientServicesException {
    	Response res = getClientService().put(serviceUrl, parameters, content, format);
		return processSessionId(res);
    }
    @Override
    public Response xhrPut(Args args, Object content) throws ClientServicesException {
    	Response res = getClientService().put(args, content);
		return processSessionId(res);
    }

    @Override
	public Response xhrDelete(String serviceUrl) throws ClientServicesException {
    	Response res = getClientService().delete(serviceUrl);
		return processSessionId(res);
    }
    @Override
	public Response xhrDelete(String serviceUrl, Map<String, String> parameters) throws ClientServicesException {
    	Response res = getClientService().delete(serviceUrl, parameters);
		return processSessionId(res);
    }
    @Override
	public Response xhrDelete(String serviceUrl, Map<String, String> parameters, Handler format) throws ClientServicesException {
    	Response res = getClientService().delete(serviceUrl, parameters, format);
		return processSessionId(res);
    }
    @Override
    public Response xhrDelete(Args args) throws ClientServicesException {
		Response res = getClientService().delete(args);
    	return processSessionId(res);
    }
    @Override
    public String getProxyQueryArgs() {
    	return null;
    }
    @Override
    public void updateHeaders(DefaultHttpClient client, HttpRequestBase method) {
    }
    @Override
    public void updateUrl(DefaultHttpClient client, String url) {
    }
    @Override
    public void handleAuthenticationError() {
    }
    
    /**
     * @return the serviceMappings
     */
    @Override
    public Map<String, String> getServiceMappings() {
        return this.serviceMappings;
    }

    /**
     * @param serviceMappings the serviceMappings to set. Stored as a Map.
     * @throws Exception 
     */
    public void setServiceMappings(Map<String, String> serviceMappings) {
    	this.serviceMappings.putAll(serviceMappings);
    }

    /**
     * <p>enable/disable the management of cookies by the Endpoint</p>
     * <p>when enabled, the endpoint store the connection cookies so the server doesn't create
     * a new session for every connection made increasing response performance for single requests.</p>
     * <p>enable only when endpoint are maintained in a session</p>
     * @param enableCookies
     */
    public void enableStatefulCookies(boolean enableCookies) {
        this.enableCookies = enableCookies;
    }
    
    @Override
    public CookieStore getCookies() {
        return enableCookies ? this.cookieStore : new BasicCookieStore();
    }
}

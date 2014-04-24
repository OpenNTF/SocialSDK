/*
 * � Copyright IBM Corp. 2012
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

import org.apache.http.client.methods.HttpRequestBase;
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
	private boolean useClientRequestURLForResponses;
    
    protected Map<String, Object> clientParams = new HashMap<String, Object>();

    
    private int authenticationErrorCode = 401;
    
    protected static final String PLATFORM_CONNECTIONS = "connections";
    protected static final String PLATFORM_SMARTCLOUD = "smartcloud";
    protected static final String PLATFORM_DOMINO = "domino";
    protected static final String PLATFORM_SAMETIME = "sametime";
    protected static final String PLATFORM_DROPBOX = "dropbox";
    protected static final String PLATFORM_TWITTER = "twitter";
    
    public AbstractEndpoint() {
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
	public boolean isUseRequestUrl() {
        return forceDisableExpectedContinue;
    }
    public void useClientRequestURLForResponses(boolean useClientRequestURLForResponses) {
        this.useClientRequestURLForResponses = useClientRequestURLForResponses;
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
    	return srv.xhr(method,args,content);
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

    			return clientService;
   		} catch(Exception ex) {
    			throw new ClientServicesException(ex,"Cannot create ClientService class {0}",cls);
    		}
    	}
    	return new GenericService(this);
    }

    @Override
	public Response xhrGet(String serviceUrl) throws ClientServicesException {
    	return getClientService().get(serviceUrl);
    }
    @Override
	public Response xhrGet(String serviceUrl, Map<String, String> parameters) throws ClientServicesException {
    	return getClientService().get(serviceUrl, parameters);
    }
    @Override
	public Response xhrGet(String serviceUrl, Map<String, String> parameters, Handler format) throws ClientServicesException {
    	return getClientService().get(serviceUrl, parameters, format);
    }
    @Override
    public Response xhrGet(Args args) throws ClientServicesException {
    	return getClientService().get(args);
    }
    
    @Override
	public Response xhrPost(String serviceUrl, Object content) throws ClientServicesException {
    	return getClientService().post(serviceUrl, content);
    }
    @Override
	public Response xhrPost(String serviceUrl, Map<String, String> parameters, Object content) throws ClientServicesException {
    	return getClientService().post(serviceUrl, parameters, content);
    }
    @Override
	public Response xhrPost(String serviceUrl, Map<String, String> parameters, Object content, Handler format) throws ClientServicesException {
    	return getClientService().post(serviceUrl, parameters, content, format);
    }
    @Override
    public Response xhrPost(Args args, Object content) throws ClientServicesException {
    	return getClientService().post(args, content);
    }

    @Override
	public Response xhrPut(String serviceUrl, Object content) throws ClientServicesException {
    	return getClientService().put(serviceUrl, content);
    }
    @Override
	public Response xhrPut(String serviceUrl, Map<String, String> parameters, Object content) throws ClientServicesException {
    	return getClientService().put(serviceUrl, parameters, content);
    }
    @Override
	public Response xhrPut(String serviceUrl, Map<String, String> parameters, Object content, Handler format) throws ClientServicesException {
    	return getClientService().put(serviceUrl, parameters, content, format);
    }
    @Override
    public Response xhrPut(Args args, Object content) throws ClientServicesException {
    	return getClientService().put(args, content);
    }

    @Override
	public Response xhrDelete(String serviceUrl) throws ClientServicesException {
    	return getClientService().delete(serviceUrl);
    }
    @Override
	public Response xhrDelete(String serviceUrl, Map<String, String> parameters) throws ClientServicesException {
    	return getClientService().delete(serviceUrl, parameters);
    }
    @Override
	public Response xhrDelete(String serviceUrl, Map<String, String> parameters, Handler format) throws ClientServicesException {
    	return getClientService().delete(serviceUrl, parameters, format);
    }
    @Override
    public Response xhrDelete(Args args) throws ClientServicesException {
    	return getClientService().delete(args);
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

}

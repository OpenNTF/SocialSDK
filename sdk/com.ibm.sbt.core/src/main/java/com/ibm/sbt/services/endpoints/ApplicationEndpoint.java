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

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.endpoints.js.JSReference;
import com.ibm.sbt.util.SBTException;


/**
 * Endpoint that points to the current application.
 * <p>
 * </p>
 * @author Philippe Riand
 */
public class ApplicationEndpoint implements Endpoint {

    protected Map<String, Object> clientParams = new HashMap<String, Object>();
    	
	private static final int authenticationErrorCode = 401;
	
    public ApplicationEndpoint() {
    }
    
    protected IllegalStateException newNotApplicableException() {
    	throw new IllegalStateException("This function is not application for an ApplicationEndpoint");
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
	public String getUrl() {
    	Context ctx = Context.get();
    	// Adapt this for portal?
    	HttpServletRequest req = ctx.getHttpRequest();
    	return UrlUtil.getRequestUrl(req, UrlUtil.URL_CONTEXTPATH);
    }

    @Override
	public String getUserIdentity() throws ClientServicesException {
    	Context ctx = Context.getUnchecked();
    	if(ctx!=null) {
    		return ctx.getCurrentUserId();
    	}
    	return null;
    }
    
    @Override
	public String getProxyPath(String endpointName) {
    	return null;
    }
    
    @Override
	public String getProxyHandlerPath() {
    	return null;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.services.endpoints.Endpoint#isUseProxy()
     */
    @Override
    public boolean isUseProxy() {
    	return false;
    }
    
    @Override
	public JSReference getAuthenticator(String endpointName, String sbtUrl) {
    	return null;
    }
    
    @Override
	public JSReference getTransport(String endpointName, String moduleId) {
    	return null;
    }

    @Override
	public String getLabel() {
    	return "Current Application Endpoint";
    }
    
    @Override
	public boolean isAllowClientAccess() {
    	return true;
    }
    
    @Override
	public String getLoginPage() {
    	return null;
    }
    
    @Override
	public String getDialogLoginPage() {
    	return null;
    }
    
    @Override
	public String getLoginUi() {
    	return null;
    }
    
    @Override
	public String getAutoAuthenticate() {
    	return null;
    }
    
    @Override
	public String getAuthType() {
    	return "application";
    }
    
    @Override
    public String getApiVersion() {
        return null;
    }
    
    @Override
	public boolean isHeaderAllowed(String headerName, String serviceUrl) {
    	throw newNotApplicableException();
    }
    
    @Override
	public int getAuthenticationErrorCode(){
    	return authenticationErrorCode;
    }
    
    @Override
	public boolean isRequiresAuthentication() throws ClientServicesException {
    	return false;
    }
    
    @Override
	public boolean isAuthenticated() throws ClientServicesException {
    	Context ctx = Context.getUnchecked();
    	if(ctx!=null) {
    		return !ctx.isCurrentUserAnonymous();
    	}
    	return false;
    }

    @Override
	public boolean isAuthenticationValid() throws ClientServicesException {
    	return isAuthenticated();
    }
    
    @Override
	public void authenticate(boolean force) throws ClientServicesException {
    	throw new UnsupportedOperationException("Not Yet Implemented");
    }
    
    @Override
	public void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
    	throw newNotApplicableException();
    }

    @Override
	public void checkValid() throws SBTException {
    	
    }
       
    @Override
	public Response xhr(String method, ClientService.Args args, Object content) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrGet(String serviceUrl) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrGet(String serviceUrl, Map<String, String> parameters) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrGet(String serviceUrl, Map<String, String> parameters, Handler format) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrGet(Args args) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrPost(String serviceUrl, Object content) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrPost(String serviceUrl, Map<String, String> parameters, Object content) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrPost(String serviceUrl, Map<String, String> parameters, Object content, Handler format) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrPost(Args args, Object content) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrPut(String serviceUrl, Object content) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrPut(String serviceUrl, Map<String, String> parameters, Object content) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrPut(String serviceUrl, Map<String, String> parameters, Object content, Handler format) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrPut(Args args, Object content) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrDelete(String serviceUrl) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrDelete(String serviceUrl, Map<String, String> parameters) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrDelete(String serviceUrl, Map<String, String> parameters, Handler format) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public Response xhrDelete(Args args) throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public ClientService getClientService() throws ClientServicesException {
    	throw newNotApplicableException();
    }
    @Override
	public boolean isForceTrustSSLCertificate() {
    	throw newNotApplicableException();
    }
	@Override
	public void logout() throws AuthenticationException {
		// TODO Auto-generated method stub
	}
    @Override
	public String getHttpProxy(){
		throw newNotApplicableException();
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

    @Override
    public Map<String, String> getServiceMappings() {
        return null;
    }
}

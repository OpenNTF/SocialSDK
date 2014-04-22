/* ***************************************************************** */
/*
 * � Copyright IBM Corp. 2013
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


import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;

import com.ibm.commons.Platform;
import com.ibm.commons.extension.ExtensionManager;
import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.impl.PropertiesFactory;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.smartcloud.SmartCloudService;
/**
 * Bean that provides a Smartcloud Form based authentication.
 * @author Manish Kataria
 */

public class SmartCloudFormEndpoint extends FormEndpoint {
	
	private static final String SC_FORM_PAGE = "pkmslogin.form";
	String federationInfoUrl = "/manage/resource/getDCInfo";
	
	
	/**
	 * this API returns the URL for authenticating the user in case
	 * the user is part of a federated SmartCloud setup
	 * 
	 * default is /manage/resource/getDCInfo
	 * 
	 * @return the url where to POST the username to get the actual authentication page
	 */
	public String getFederationInfoUrl() {
		return federationInfoUrl;
	}

	public void setFederationUrlInfo(String federationUrlInfo) {
		this.federationInfoUrl = federationUrlInfo;
	}

	public SmartCloudFormEndpoint() {
    	setAuthenticationErrorCode(403);
	}	
	
	@Override
	public List<NameValuePair> getLoginFormParameters() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		// smartcloud platform needs these three parameters
		// to fetch these for a new platform, look at source of form page/login page
		params.add(new BasicNameValuePair("username", getUser()));
		params.add(new BasicNameValuePair("password", getPassword()));
		params.add(new BasicNameValuePair("login-form-type", "pwd"));
		return params;
	}
	
	@Override
	public String getLoginFormUrl(){
		if(StringUtil.isEmpty(super.getLoginFormUrl())){
			return SC_FORM_PAGE;
		}else{
			return super.getLoginFormUrl();
		}
	}
	
    @Override
	public ClientService getClientService() throws ClientServicesException {
    	return new SmartCloudService(this);
    }
    
    
    @Override
    public boolean login(String user, String password)
    		throws AuthenticationException {
		try {
			BasicCookieStore cookieStore = new BasicCookieStore();
			HttpClient c = createClient(cookieStore);

			String entryUrl = getUrl();

			
			HttpPost getDCInfo = new HttpPost(entryUrl
					+ getFederationInfoUrl());
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
			
			parameters.add(new BasicNameValuePair("loginName", user));
			parameters.add(new BasicNameValuePair("special", "true"));
			
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters);
			getDCInfo.setEntity(entity);
			
			String authenticationPage = IOUtils.toString(c.execute(getDCInfo)
					.getEntity().getContent());
			authenticationPage = URLDecoder.decode(authenticationPage);
			
			if (authenticationPage.contains(",NON_FEDERATED,"))
	    	return super.login(user, password);
			
			authenticationPage = extractFederatedPage(authenticationPage);
			
			
			FormLoginHandler h = getHandler(authenticationPage);
			boolean authenticated =  h.login(user, password, c, cookieStore, authenticationPage);
			
			if (authenticated) {
				setCookieCache(cookieStore.getCookies());
				return true;
			}else return false;
			
		} catch (Exception e) {
			throw new AuthenticationException(e);
		}
    }
    

	Map<String,String> formScriptMap = new HashMap<String, String>();
    
    public void setFormScripts(Map<String, String> formScript) {
		this.formScriptMap = formScript;
	}
    
    public Map<String, String> getFormScripts() {
		return formScriptMap;
	}

    public FormLoginHandler getHandler(String page) throws Exception {

    	List<FormLoginHandler> l = (List)ExtensionManager.findApplicationServices(Application.get().getClassLoader(),FormLoginHandler.class.getName().toLowerCase());
    	for (FormLoginHandler h : l) {
    		if (h.accept(page)) return h;
    	}
    	throw new AuthenticationException(new IllegalArgumentException("No handler definition for " + page));
    }
    
	private String extractFederatedPage(String authenticationPage) {
		String up[] = authenticationPage.split(",");
		authenticationPage = up[up.length-1];
		return authenticationPage;
	}
    
    @Override
    public String getAuthType() {
     	return "form"; 
    }

}



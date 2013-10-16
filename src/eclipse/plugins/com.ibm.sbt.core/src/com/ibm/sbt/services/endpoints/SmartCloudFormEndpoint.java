/* ***************************************************************** */
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.smartcloud.SmartCloudService;
/**
 * Bean that provides a Smartcloud Form based authentication.
 * @author Manish Kataria
 */

public class SmartCloudFormEndpoint extends FormEndpoint {
	
	private static final String SC_FORM_PAGE = "pkmslogin.form";
	
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
    public String getAuthType() {
     	return "form"; 
    }
}

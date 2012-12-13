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
package com.ibm.sbt.services.client.smartcloud.profiles;

import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.smartcloud.SmartcloudService;
import com.ibm.sbt.services.endpoints.Endpoint;
/**
 * @author Vimal Dhupar
 */
public class Subscriber {
	private JsonObject data;
	
	public Subscriber() {
		//TODO
	}
	private void load(Endpoint endpoint)
	{
		SmartcloudService svc  = new SmartcloudService(endpoint);
		Map<String, String> parameters = new HashMap<String,String>();
		Object result = null;
		try 
		{
			result = svc.get(ProfilesAPIMap.GETUSERIDENTITY.getUrl(), parameters, ClientService.FORMAT_JSON);
		} 
		catch (ClientServicesException e) 
		{
		}
		this.data = (JsonObject) result;
	}
	public String getSubscriberId(Endpoint endpoint)
	{
		load(endpoint); 
		if(null != data)
			return data.getJsonProperty("subscriberid").toString();
		return null;
	}
}

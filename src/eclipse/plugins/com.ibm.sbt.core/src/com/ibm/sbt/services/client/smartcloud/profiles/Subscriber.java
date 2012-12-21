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
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.smartcloud.SmartCloudService;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * This File represents the Subscriber Class. This is used to fetch the Subscriber Id of the logged in user.
 * This ID is then used to fetch the Profile of the User.
 * <p>
 * 
 * @author Vimal Dhupar
 */
public class Subscriber {
	private JsonObject			data;

	private static final String	sourceClass	= ProfileService.class.getName();
	private static final Logger	logger		= Logger.getLogger(sourceClass);

	public Subscriber() {
		// TODO
	}

	private void load(Endpoint endpoint) throws ProfileServiceException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "load");
		}
		SmartCloudService svc = new SmartCloudService(endpoint);
		Map<String, String> parameters = new HashMap<String, String>();
		Object result = null;
		try {
			result = svc.get(ProfilesAPIMap.GETUSERIDENTITY.getUrl(), parameters, ClientService.FORMAT_JSON);
		} catch (ClientServicesException e) {
			throw new ProfileServiceException(e);
		}
		this.data = (JsonObject) result;
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "load");
		}
	}

	/**
	 * getSubscriberId - returns the subscriber id
	 * 
	 * @param endpoint
	 * @return String
	 * @throws ProfileServiceException 
	 */
	public String getSubscriberId(Endpoint endpoint) throws ProfileServiceException {
		load(endpoint);
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "getSubscriberId");
		}
		if (null != data) {
			return data.getJsonProperty("subscriberid").toString();
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "getSubscriberId");
		}
		return null;
	}
}

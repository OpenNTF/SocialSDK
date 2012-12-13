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

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.connections.files.utils.Messages;
import com.ibm.sbt.util.DataNavigator;

/**
 * @Represents Smartcloud Profile
 * @author Vimal Dhupar This File does not have set/update/create/remove methods, as these are not supported
 *         by Smartcloud
 */
public class Profile implements Serializable {

	private static final String	sourceClass			= Profile.class.getName();
	private static final Logger	logger				= Logger.getLogger(sourceClass);
	private static final long	serialVersionUID	= 2500126733627058535L;
	private JsonObject			data;
	private String				reqId;
	private ProfileService		service;

	public Profile() {
	}

	public Profile(String reqId, ProfileService service) {
		this.reqId = reqId;
		this.service = service;
	}

	/**
	 * @return Service
	 */
	public ProfileService getService() {
		return service;
	}

	/**
	 * @set Service
	 */
	@SuppressWarnings("unused")
	private void setService(ProfileService service) {
		this.service = service;
	}

	/**
	 * getData
	 * 
	 * @return Data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * setData
	 * 
	 * @param data
	 *            Method sets the response from the API call to data method for locally referring the data in
	 *            future.
	 */
	public void setData(JsonObject data) {
		this.data = data;
	}

	/**
	 * load
	 * 
	 * @throws SBTServiceException
	 */
	public void load() throws SBTServiceException {
		service.load(this);
	}

	/**
	 * getThumbnailUrl
	 * 
	 * @return ThumbnailUrl
	 */
	public String getThumbnailUrl() {
		return get("thumbnailUrl");
	}

	/**
	 * getUniqueId
	 * 
	 * @return id
	 */
	public String getUniqueId() {
		return get("id");
	}

	/**
	 * getReqId
	 * 
	 * @return ReqId
	 */
	public String getReqId() {
		return reqId;
	}

	/**
	 * getDisplayName
	 * 
	 * @return name
	 */
	public String getDisplayName() {
		return get("displayName");
	}

	/**
	 * getId
	 * 
	 * @return id
	 */
	public String getId() {
		return get("id");
	}

	/**
	 * getEmail
	 * 
	 * @return email
	 */
	public String getEmail() {
		return get("email");
	}

	/**
	 * getAddress
	 * 
	 * @return
	 */
	public String getAddress() {
		return get("address");
	}

	/**
	 * getDepartment
	 * 
	 * @return
	 */
	public String getDepartment() {
		return get("department");
	}

	/**
	 * getTitle
	 * 
	 * @return
	 */
	public String getTitle() {
		return get("title");
	}

	/**
	 * getProfileUrl
	 * 
	 * @return
	 */
	public String getProfileUrl() {
		return get("profileUrl");
	}

	/**
	 * getPhoneNumber
	 * 
	 * @return
	 */
	public String getPhoneNumber() {
		return get("phoneNumbers");
	}

	/**
	 * getCountry
	 * 
	 * @return
	 */
	public String getCountry() {
		return get("country");
	}

	/**
	 * getAboutMe
	 * 
	 * @return
	 */
	public String getAboutMe() {
		return get("aboutMe");
	}

	/**
	 * @return value for specified field. Field names follow IBM Connections naming convention
	 */

	public String get(String fieldName) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "get", new Object[] { fieldName });
		}
		String result = null;
		if (null == this.data) {
			logger.log(Level.SEVERE, Messages.ProfileError_1);
			return null;
		}
		DataNavigator.Json nav = new DataNavigator.Json(this.data); // this.data has the response feed.
		DataNavigator entry = nav.get("entry");
		if (fieldName.equals("thumbnailUrl")) {
			String photo = entry.stringValue("photo");
			if (!StringUtil.isEmpty(photo)) {
				result = this.service.getEndpoint().getUrl() + "/contacts/img/photos/" + photo;
			}
		} else if (fieldName.equals("email")) {
			result = entry.stringValue("emailAddress");
		} else if (fieldName.equals("department")) {
			if (entry.get("org") != null) {
				result = entry.get("org").stringValue("name");
			}
		} else if (fieldName.equals("title")) {
			result = entry.stringValue("jobtitle");
		} else if (fieldName.equals("phoneNumbers")) {
			result = entry.stringValue("telephone");
		} else {
			result = entry.stringValue(fieldName);
		}

		return result;
	}

	@Override
	public String toString() {
		return this.toString();
	}
}
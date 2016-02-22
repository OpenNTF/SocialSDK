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

package com.ibm.sbt.services.client.connections.profiles;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.profiles.model.ProfileXPath;

/**
 * IBM Connections Profiles - Profile Abstraction
 *
 */
public class Profile extends AtomEntity {

	static final String 			sourceClass 	= Profile.class.getName();
	static final Logger 			logger			= Logger.getLogger(sourceClass);

	/**
	 * Constructor
	 *  
	 * @param profileService
	 * @param id
	 */
	public Profile(ProfileService profileService, String id) {
		setService(profileService);
		setAsString(ProfileXPath.uid, id);
	}

	public Profile(){}

	/**
	 * 
	 * @param svc
	 * @param handler
	 */
	public Profile(BaseService svc, XmlDataHandler handler) {
		super(svc,handler);
	}
	
	/**
	 * 
	 * @return {Profile}
	 * @throws ProfileServiceException
	 * @throws ClientServicesException 
	 */
    public Profile load() throws ClientServicesException {
    	return getService().getProfile(getUserid());
    }
    
    /**
     * 
     * @param service
     * @param node
     * @param namespaceCtx
     * @param xpathExpression
     */
	public Profile(BaseService service, Node node, NamespaceContext namespaceCtx, 
			XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}

	/**
	 * 
	 * @throws ProfileServiceException
	 */
    public void update() throws ClientServicesException {
    	getService().updateProfile(this);
    }
    
    /**
     * 
     * @return {EntityList<Profile>}
     * @throws ProfileServiceException
     * @throws ClientServicesException 
     */
    public EntityList<Profile> getColleagues() throws ClientServicesException {
    	return getService().getColleagues(getUserid());
    }
    
	/**
	 * Returns a mapping containing the extended attributes for the entry.<bt/>
	 * This method execute a xhr call to the back end for every attribute.
	 * 
	 * @return a map containing the id of the attribute as key and the attribute value as value
	 * @throws ProfileServiceException
	 */
	public Map<String,Object> getExtendedAttributes() throws ClientServicesException {
		return getService().getExtendedAttributes(this);
	}
 
	/**
	 * 
	 * @return {String}
	 */
    public String getUserid() {
    	return getAsString(ProfileXPath.uid);
    }

	/**
     * @return the name
     */
    public String getName() {
    	return getAsString(ProfileXPath.name);
    }
   
    /**
     * @return the email
     */
    public String getEmail() {
    	return getAsString(ProfileXPath.email);
    }
    
    public String getGroupwareEmail() {
    	return getAsString(ProfileXPath.groupwareMail);
    }
    /**
     * @return the Title
     */
    public String getJobTitle() {
    	return getAsString(ProfileXPath.jobTitle);
    }
    /**
     * @return the telephone number
     */
    
    public String getTelephoneNumber() {
    	return getAsString(ProfileXPath.telephoneNumber);
    }
    /**
     * @return the department
     */
    
    public String getDepartment() {
    	return getAsString(ProfileXPath.organizationUnit);
	}
    
    /**
     * @return the thumbNailUrl
     */
    public String getThumbnailUrl() {
    	return getAsString(ProfileXPath.photo);
    }
    
    /**
     * @return the  profile Url
     */
    public String getProfileUrl() {
    	return getAsString(ProfileXPath.fnUrl);
    }
    
    /**
     * @return the pronunciation url
     */
    
    public String getPronunciationUrl() {
    	return getAsString(ProfileXPath.soundUrl);
    }
    
    /**
     * @return the summary
     */
    public String  getSummary() {
    	return getAsString(ProfileXPath.summary);
    }
    /**
     * @return Building Id
     */
    public String getBuilding() {
        return getAsString(ProfileXPath.building);
    }
    /**
     * @return floor
     */    
    public String getFloor() {
    	return getAsString(ProfileXPath.floor);
    }
    
    /**
     * @return the address
     */
    public Object getAddress() {
    	 HashMap<String, String> AddressMap = new HashMap<String, String>();
    	 AddressMap.put("countryName", getAsString(ProfileXPath.countryName));
    	 AddressMap.put("locality", getAsString(ProfileXPath.locality));
    	 AddressMap.put("postalCode", getAsString(ProfileXPath.postalCode));
    	 AddressMap.put("region", getAsString(ProfileXPath.region));
     	 AddressMap.put("streetAddress", getAsString(ProfileXPath.streetAddress));
    	 AddressMap.put("extendedAddress", getAsString(ProfileXPath.extendedAddress));
    	 return AddressMap;
    }
    
    public void setAddress(Map<String, String> addressMap) {
    	
    	Iterator<Map.Entry<String,String>> address = addressMap.entrySet().iterator();
		while(address.hasNext()){
			Map.Entry<String,String> addressPairs = (Map.Entry<String,String>)address.next();
			if(addressPairs.getKey().equalsIgnoreCase("countryName")) {
				setAsString(ProfileXPath.countryName,addressPairs.getValue());
			}
			if(addressPairs.getKey().equalsIgnoreCase("locality")) {
				setAsString(ProfileXPath.locality,addressPairs.getValue());
			}
			if(addressPairs.getKey().equalsIgnoreCase("postalCode")) {
				setAsString(ProfileXPath.postalCode,addressPairs.getValue());
			}
			if(addressPairs.getKey().equalsIgnoreCase("region")) {
				setAsString(ProfileXPath.region,addressPairs.getValue());
			}
			if(addressPairs.getKey().equalsIgnoreCase("streetAddress")) {
				setAsString(ProfileXPath.streetAddress,addressPairs.getValue());
			}
			if(addressPairs.getKey().equalsIgnoreCase("extendedAddress")) {
				setAsString(ProfileXPath.extendedAddress,addressPairs.getValue());
			}
		}
   }
    
    public void setTelephoneNumber(String telephoneNumber){
    	setAsString(ProfileXPath.telephoneNumber, telephoneNumber);
    }
    
    public void setJobTitle(String title){
    	setAsString(ProfileXPath.jobTitle, title);
    }
    
    public void setEmail(String email){
    	setAsString(ProfileXPath.email, email);
    }
    
    public void setPhotoLocation(String imgLoc){
    	setAsString(ProfileXPath.photo, imgLoc);
	}

	@Override
	public ProfileService getService(){
		return (ProfileService)super.getService();
	}

}

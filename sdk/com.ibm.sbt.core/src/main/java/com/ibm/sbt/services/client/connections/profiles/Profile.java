/*
o * © Copyright IBM Corp. 2013
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
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.profiles.model.ProfileXPath;

/**
 * @Represents Connections Profile
 *
 */
public class Profile extends BaseEntity{

	static final String 			sourceClass 	= Profile.class.getName();
	static final Logger 			logger			= Logger.getLogger(sourceClass);

	/**
	 * Constructor
	 *  
	 * @param communityService
	 * @param communityUuid
	 */
	public Profile(ProfileService profileService, String id) {
		setService(profileService);
		setAsString(ProfileXPath.uid, id);
	}

	public Profile(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
    public Profile load() throws ProfileServiceException
    {
    	return getService().getProfile(getUserid());
    }
    
    public void update() throws ProfileServiceException
    {
    	getService().updateProfile(this);
    }
    
    public ProfileList getColleagues() throws ProfileServiceException
    {
    	return getService().getColleagues(getUserid());
    }
    
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
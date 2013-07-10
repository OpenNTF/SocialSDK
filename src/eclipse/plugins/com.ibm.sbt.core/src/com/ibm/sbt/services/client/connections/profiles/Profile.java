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
	
    public void load() throws ProfileServiceException
    {
    	getService().getProfile(getReqId());
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
    public String getDisplayName() {
    	return getAsString(ProfileXPath.name);
    }
   
    /**
     * @return the email
     */
    public String getEmail() {
    	return getAsString(ProfileXPath.email);
    }
    /**
     * @return the Title
     */
      
    public String getTitle() {
    	return getAsString(ProfileXPath.title);
    }
    /**
     * @return the telephone number
     */
    
    public String getPhoneNumber() {
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
     * @return the about me
     */
    public String  getAbout() {
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
//    	 AddressMap.put("country",get("countryName"));
//    	 AddressMap.put("locality",get("locality"));
//    	 AddressMap.put("postalCode",get("postalCode"));
//    	 AddressMap.put("region",get("region"));
//     	 AddressMap.put("streetAddress",StringUtil.format(",", get("streetAddress"), get("extendedAddress")));
    	 AddressMap.put("building",getAsString(ProfileXPath.building));
    	 AddressMap.put("floor",getAsString(ProfileXPath.floor));
    	 return AddressMap;
    }
  
     /**
     * @return the id
     */
    public String getUniqueId() {
    	return getAsString(ProfileXPath.uid);
    }

   
    public String getReqId() {
    	return fields.get(ProfileXPath.uid).toString();
    }
   
    public void setReqId(String id) {
    	fields.put(ProfileXPath.uid.getName(), id);
    }
      
    public void setBuilding(String building) {
        setAsString(ProfileXPath.building, building);
    }
    
    
    public void setFloor(String floor) {
        setAsString(ProfileXPath.floor, floor);
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
		}
   }
    
    public void setPhoneNumber(String telephoneNumber){
    	setAsString(ProfileXPath.telephoneNumber, telephoneNumber);
    }
    
    public void setTitle(String title){
    	setAsString(ProfileXPath.title, title);
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
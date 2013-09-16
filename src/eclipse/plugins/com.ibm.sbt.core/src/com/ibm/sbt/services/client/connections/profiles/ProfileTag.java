package com.ibm.sbt.services.client.connections.profiles;




import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

/**
 * tag Entry Class - representing a tag associated with a Profile.
 * 
 * @author Swati Singh
 */
public class ProfileTag extends BaseEntity{


	public ProfileTag(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	public String getTerm() {
    	return getAsString(ProfileXPath.term);
    }
   
    public void setTerm(String term) {
    	setAsString(ProfileXPath.term,term);
	}
    
    public int getFrequency(){
    	return getAsInt(ProfileXPath.frequency);
	}
    public int getIntensity() {
    	return getAsInt(ProfileXPath.intensity);
    }
    
    public int getVisibility() {
        return getAsInt(ProfileXPath.visibility);
    }
	
}

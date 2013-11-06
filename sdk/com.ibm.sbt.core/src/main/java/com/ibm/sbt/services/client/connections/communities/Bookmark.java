package com.ibm.sbt.services.client.connections.communities;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;

/**
 * This File represents Community Bookmark

 * @author Swati Singh
 */

public class Bookmark extends BaseEntity{


	public Bookmark(CommunityService communityService, String id) {
		setService(communityService);
		setAsString(CommunityXPath.id, id);
	}
	
	public Bookmark(CommunityService svc, DataHandler<?> handler)
	{
		super(svc,handler);
	}
	
	
	/**
	 * getId
	 * 
	 * @return id
	 */	
	public String getId() {
		return getAsString(CommunityXPath.id);
	}
	
	/**
	 * getTitle
	 * 
	 * @return title
	 */	
	public String getTitle() {
		return getAsString(CommunityXPath.title);
	}

	/**
	 * getSummary
	 * 
	 * @return summary
	 */	
	public String getSummary() {
		return getAsString(CommunityXPath.summary);
	}

	/**
	 * Method sets the bookmark title
	 */	
	public void setTitle(String title) {
		setAsString(CommunityXPath.title, title);
	}

}

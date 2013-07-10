package com.ibm.sbt.services.client.connections.communities;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

/**
 * This File represents Community Bookmark

 * @author Swati Singh
 */

public class Bookmark extends BaseEntity{


	public Bookmark(CommunityService communityService, String id) {
		setService(communityService);
		setAsString(BookmarkXPath.id, id);
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
		return getAsString(BookmarkXPath.id);
	}
	
	/**
	 * getTitle
	 * 
	 * @return title
	 */	
	public String getTitle() {
		return getAsString(BookmarkXPath.title);
	}

	/**
	 * getSummary
	 * 
	 * @return summary
	 */	
	public String getSummary() {
		return getAsString(BookmarkXPath.summary);
	}

	/**
	 * Method sets the bookmark title
	 */	
	public void setTitle(String title) {
		setAsString(BookmarkXPath.title, title);
	}

}

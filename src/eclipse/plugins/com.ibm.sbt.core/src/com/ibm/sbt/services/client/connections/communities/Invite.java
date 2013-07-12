package com.ibm.sbt.services.client.connections.communities;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

/**
 * This File represents Community Invites

 * @author Swati Singh
 */

public class Invite extends BaseEntity{


	public Invite(CommunityService communityService, String id) {
		setService(communityService);
		setAsString(InviteXPath.id, id);
	}
	
	public Invite(CommunityService svc, DataHandler<?> handler)
	{
		super(svc,handler);
	}
	
	
	/**
	 * getId
	 * 
	 * @return id
	 */	
	public String getId() {
		return getAsString(InviteXPath.id);
	}
	
	/**
	 * getTitle
	 * 
	 * @return title
	 */	
	public String getTitle() {
		return getAsString(InviteXPath.title);
	}

	/**
	 * getSummary
	 * 
	 * @return summary
	 */	
	public String getSummary() {
		return getAsString(InviteXPath.summary);
	}

	/**
	 * Method sets the Invite title
	 */	
	public void setTitle(String title) {
		setAsString(InviteXPath.title, title);
	}
	
	/**
	 * @return the author
	 */
	public Member getAuthor(){
		Member author = new Member(
					getService(),
					getAsString(InviteXPath.authorUid), 
					getAsString(InviteXPath.authorName), 
					getAsString(InviteXPath.authorEmail));
		return author;
	}
	
	/**
	 * @return the Contributor
	 */
	public Member getContributor(){
		Member contributor = new Member(
					getService(),
					getAsString(InviteXPath.contributorUid),
					getAsString(InviteXPath.contributorName),
					getAsString(InviteXPath.contributorEmail));
		return contributor;
	}

	@Override
	public CommunityService getService(){
		return (CommunityService)super.getService();
	}
}

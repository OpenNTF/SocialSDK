package com.ibm.sbt.services.client.connections.communities;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;

/**
 * This File represents Community Invites

 * @author Swati Singh
 */

public class Invite extends BaseEntity{

	private String communityUuid;


	public Invite(CommunityService communityService, String id) {
		setService(communityService);
		setAsString(CommunityXPath.id, id);
	}
	
	public Invite(CommunityService svc, DataHandler<?> handler)
	{
		super(svc,handler);
	}
	
	public String getCommunityUuid(){
    	String communityId = "";
    	try {
    		communityId = getAsString(CommunityXPath.inviteCommunityUrl);
		} catch (Exception e) {}
    	
    	if(StringUtil.isEmpty(communityId)){
    		communityId = communityUuid;
    	}
    	//extract the community id from /communities/service/atom/community?communityUuid=33320ce4-058b-4066-95de-efbb44825773
    	communityId = communityId.substring(communityId.indexOf("=")+1,communityId.length());
    	return communityId;
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
	 * Returns the content
	 * 
	 * @return content
	 */
	public String getContent() {
		return getAsString(CommunityXPath.content);
	}

	/**
	 * @sets the content
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		setAsString(CommunityXPath.content, content);
	}
	
	/**
	 * Method sets the Invite title
	 */	
	public void setTitle(String title) {
		setAsString(CommunityXPath.title, title);
	}
	
	public String getInviteUrl(){
		return getAsString(CommunityXPath.inviteUrl);
	}
	
	/**
	 * @return the authorUid
	 */
	public Member getAuthor(){
		Member author = new Member(getService(), getAsString(CommunityXPath.authorUid));
		author.setName(getAsString(CommunityXPath.authorName));
		author.setEmail(getAsString(CommunityXPath.authorEmail));
		return author;
	}
	
	/**
	 * @return the ContributorId
	 */
	public Member getContributor(){
		Member contributor = new Member(getService(), getAsString(CommunityXPath.contributorUid));
		contributor.setName(getAsString(CommunityXPath.contributorName));
		contributor.setEmail(getAsString(CommunityXPath.contributorEmail));
		return contributor;
	}
	

	@Override
	public CommunityService getService(){
		return (CommunityService)super.getService();
	}
}

package com.ibm.sbt.services.client.connections.communities;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;

/**
 * This File represents Community Invites

 * @author Swati Singh
 */

public class Invite extends AtomEntity{
	 /**
     * The UUID of the community associated with this Invite
     */
	private String communityUuid;
	/**
     * The UUID if the invitee associated with this Invite
     */
	private String inviteeUuid;
	/**
     * Constructor
     * @param communityService
     */
	public Invite(CommunityService communityService) {
		super(communityService);
		setService(communityService);
	}
	/**
     * Constructor
     * @param communityService
     * @param id
     */
	public Invite(CommunityService communityService, String id) {
		super(communityService, id);
		setService(communityService);
		setAsString(CommunityXPath.id, id);
	}
	/**
     * Constructor
     * @param svc
     * @param handler
     */
	public Invite(CommunityService svc, XmlDataHandler handler)
	{
		super(svc,handler);
	}
	/**
     * Constructor
     * @param BaseService
     * @param Node
     * @param NamespaceContext
     * @param XPathExpression
     */
	public Invite(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	/**
     * Return the value of IBM Connections invite ID from invite ATOM
     * entry document.
     * 
     * @method getInviteUuid
     * @return {String} Invite ID of the invite
     */
    public String getInviteUuid() {
        String id = getAsString(CommunityXPath.id);
        if(StringUtil.isNotEmpty(id)){
        	return extractInviteUuid(id);
        }
        return id;
    }
    /**
     * Sets id of IBM Connections invite.
     * 
     * @method setInviteUuid
     * @param {String} inviteUuid Id of the invite
     */
    public void setInviteUuid(String inviteUuid) {
        setAsString(CommunityXPath.id, inviteUuid);
    }
	/**
     * Return the community UUID.
     * 
     * @method getCommunityUuid
     * @return {String} communityUuid
     */
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
     * Set the community UUID.
     * 
     * @method setCommunityUuid
     */
    public void setCommunityUuid(String communityUuid) {
		this.communityUuid = communityUuid;
    }
	/**
	 * @return the inviteeUuid
	 */
	public String getInviteeUuid() {
        	String inviteUuid = this.getInviteUuid();
        	this.inviteeUuid = extractInviteeUuid(inviteUuid, this.getCommunityUuid());
        	return this.inviteeUuid;
	
	}

	/**
	 * @param inviteeUuid the inviteeUuid to set
	 */
	public void setInviteeUuid(String inviteeUuid) {
		this.inviteeUuid = inviteeUuid;
	}
	 /**
     * Set the user id of the invitee.
     * 
     * @method setUserid
     * @return {String} userid
     */
    public void setUserid(String userid) {
        setAsString(CommunityXPath.contributorUserid, userid);
    }
    
    /**
     * Return the user id of the invitee.
     * 
     * @method getUserid
     * @return {String} userid
     */
    public String getUserid () {
    	return getAsString(CommunityXPath.contributorUserid);
    
    }
    
    /**
     * Set the email of the invitee.
     * 
     * @method setEmail
     * @return {String} email
     */
    public void setEmail(String email) {
    	setAsString(CommunityXPath.contributorEmail, email);
    }
    
    /**
     * Return the email of the invitee.
     * 
     * @method getEmail
     * @return {String} email
     */
    public String getEmail() {
		return getAsString(CommunityXPath.contributorEmail);
    }
	
	 /**
     * Returns the invite Url.
     * 
     * @method getInviteUrl
     * @return {String} inviteUrl
     */
	public String getInviteUrl(){
		return getAsString(CommunityXPath.inviteUrl);
	}
	 /**
     * Returns the community Url.
     * 
     * @method getCommunityUrl
     * @return {String} communityUrl
     */
	public String getCommunityUrl(){
		return getAsString(CommunityXPath.inviteCommunityUrl);
	}
    /**
     * Save this invite
     * 
     * @method remove
     * @param {Object} [args] Argument object
     */
    public Invite save() throws CommunityServiceException{
		return getService().createInvite(this);
    }        
	/**
	 * This method removes or revokes the invite on the server
	 * 
	 * @return
	 * @throws CommunityServiceException
	 */

	public void remove() throws CommunityServiceException {
	   	getService().declineInvite(communityUuid, inviteeUuid);
	}
	/**
	 * @method getAuthor
	 * @return Author
	 */
	@Override
	public Member getAuthor(){
		Member author = new Member(getService(), getAsString(CommunityXPath.authorUserid));
		author.setName(getAsString(CommunityXPath.authorName));
		author.setEmail(getAsString(CommunityXPath.authorEmail));
		return author;
	}
	
	/**
	 * @method getContributor
	 * @return Contributor
	 */
	@Override
	public Member getContributor(){
		Member contributor = new Member(getService(), getAsString(CommunityXPath.contributorUserid));
		contributor.setName(getAsString(CommunityXPath.contributorName));
		contributor.setEmail(getAsString(CommunityXPath.contributorEmail));
		return contributor;
	}
	

	@Override
	public CommunityService getService(){
		return (CommunityService)super.getService();
	}
	
	 /*
     * Method used to extract the invite uuid for an id url.
     */
	private String extractInviteUuid(String uid) {
		if (StringUtil.isNotEmpty(uid) && StringUtil.startsWithIgnoreCase(uid, "urn:lsid:ibm.com:communities:invite-")) {
			return uid.substring("urn:lsid:ibm.com:communities:invite-".length());
		} else {
			return uid;
		}
	}
	
	 /*
     * Method used to extract the invitee uuid for an id url.
     */
    private String extractInviteeUuid(String uid, String communityUuid) {
    	if (StringUtil.isNotEmpty(uid) && uid.indexOf(communityUuid) == 0) {
    		
    	return uid.substring(communityUuid.length()+1);
    	}
    	else{
    		return uid;
    	}
    }
}

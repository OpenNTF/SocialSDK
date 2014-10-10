package com.ibm.sbt.services.client.connections.communities;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;

/**
 */
public class RemoteApplication extends AtomEntity {

	private String communityUuid;
	
	/**
	 * Constructor
	 * 
	 * @param communityService
	 */
	public RemoteApplication(CommunityService communityService) {
		setService(communityService);
	}
	
	/**
	 * Constructor
	 * 
	 * @param communityService
	 * @param id
	 */
	public RemoteApplication(CommunityService communityService, String id) {
		setService(communityService);
		setAsString(CommunityXPath.id, id);
	}
	
	/**
     * 
     * @param service
     * @param node
     * @param namespaceCtx
     * @param xpathExpression
     */
	public RemoteApplication(BaseService service, Node node, NamespaceContext namespaceCtx, 
			XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
	/**
	 * Constructor
	 * @param svc
	 * @param handler
	 */
	public RemoteApplication(CommunityService svc, XmlDataHandler handler)
	{
		super(svc,handler);
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

}

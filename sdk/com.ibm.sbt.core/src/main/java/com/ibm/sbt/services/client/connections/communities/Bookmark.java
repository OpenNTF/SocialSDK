package com.ibm.sbt.services.client.connections.communities;

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;

/**
 * This File represents Community Bookmark

 * @author Swati Singh
 */

public class Bookmark extends AtomEntity{

	/**
     * Constructor
     * @param communityService
     * @param id
     */
	public Bookmark(CommunityService communityService, String id) {
		super(communityService, id);
		setService(communityService);
		setAsString(CommunityXPath.id, id);
	}
	/**
     * Constructor
     * @param svc
     * @param handler
     */
	public Bookmark(CommunityService svc, XmlDataHandler handler)
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
	public Bookmark(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	

}

package com.ibm.sbt.services.client.connections.follow;

/*
 * Entity object used by Follow service
 * @author Manish Kataria 
 */

import org.w3c.dom.Node;

import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;

public class FollowedResource extends AtomEntity {
	
	private static final String FOLLRESOURCEID = "urn:lsid:ibm.com:follow:resource-";
	
	public FollowedResource(){
		super();
	}
	
	public FollowedResource(BaseService service, Node node,
			NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	
    /**
     * Return the value of id from resource entry
     * 
     * @method getId
     * @return {String} ID of the resource entry
     */
	@Override
    public String getId() {
        String id = getAsString(AtomXPath.id);
        return extractFollowedResourceUuid(id);
    }
	
	/**
	 * Return the value of IBM Connections followed resource's service name
	 * 
	 * @method getSource
	 * @return String
	 */
	public String getSource() {
        return getAsString(FollowXPath.Source);
    }
	
	/**
	 * Sets the value of IBM Connections followed resource's service name
	 * 
	 * @method setSource
	 */
	public void setSource(String source) {
		setAsString(FollowXPath.Source, source);
    }
	
	
	/**
	 * Return the value of IBM Connections category identifier for the entry.
	 * 
	 * @method getType
	 * @return {String}
	 */
	public String getType(){
		return this.getAsString(FollowXPath.Type);
	}
	
	/**
	 * Sets the value of IBM Connections category identifier for the entry.
	 * 
	 * @method setType
	 */
	public void setType(String type) {
		setAsString(FollowXPath.Type, type);
    }
	
	/**
	 * Return the value of IBM Connections followed resource's type
	 * 
	 * @method getType
	 * @return {String}
	 */
	public String getResourceType(){
		return this.getAsString(FollowXPath.ResourceType);
	}
	
	
	/**
	 * Sets the value of IBM Connections followed resource's type
	 * 
	 * @method setResourceType
	 */
	public void setResourceType(String type) {
		setAsString(FollowXPath.ResourceType, type);
    }
	
	
	/**
	 * Return the value of IBM Connections followed resource's resourceId
	 * 
	 * @method getResourceId
	 * @return String
	 */
	public String getResourceId(){
		return this.getAsString(FollowXPath.ResourceId);
	}
	
	/**
	 * Sets the value of IBM Connections followed resource's resourceId
	 * 
	 * @method setResourceId
	 */
	public void setResourceId(String id) {
		setAsString(FollowXPath.ResourceId, id);
    }
	
	
	
	private String extractFollowedResourceUuid(String resourceId) {
		if (resourceId != null) {
			if (resourceId.startsWith(FOLLRESOURCEID)) {
				resourceId = resourceId.substring(FOLLRESOURCEID.length(),resourceId.length());
			}
		}
		return resourceId;
	}
	

	/* This method allows authenticated user to start following a resource
	 * Ensure source,type and resourceId are in FollowedResource object before calling this.
	 * @return FollowedResource
	 * @throws FollowServiceException
	 */
	
	public FollowedResource startFollowing() throws FollowServiceException{
		FollowService service = (FollowService)getService();
		return service.startFollowing(getSource(), getType(), getResourceId());
	}
	
	/* This method allows authenticated user to stop following a resource
	 * Ensure source,type and resourceId are in FollowedResource object before calling this.
	 * @return boolean
	 * @throws FollowServiceException
	 */
	public boolean stopFollowing() throws FollowServiceException{
		FollowService service = (FollowService)getService();
		return service.stopFollowing(getSource(), getType(), getResourceId());
	}
	

}

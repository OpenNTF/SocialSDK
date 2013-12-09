package com.ibm.sbt.services.client.connections.follow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.follow.transformer.FollowTransformer;
import com.ibm.sbt.services.client.connections.forums.ForumServiceException;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.util.AuthUtil;

/*
 * Use this Service to keep track of IBM® Connections resources that interest you, such as a person or community, a blog or a particular file.
 * @author Manish Kataria 
 * 
 * http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_the_followed_resources_ic45&content=pdcontent
 */

public class FollowService extends BaseService{
	
	public FollowService() {
		super();
	}
	
	public FollowService(String endpoint) {
		super(endpoint);
	}
	
	public FollowService(Endpoint endpoint) {
		super(endpoint);
	}
	
	/*
	 * This method returns resources that are being followed by the authenticated user.
	 * @param source
	 * @param type
	 * @return EntityList<FollowedResource>
	 * @throws FollowServiceException
	 */
	public EntityList<FollowedResource> getFollowedResources(String source, String type) throws FollowServiceException{
		return getFollowedResources(source,type,null);
	}
	
	/*
	 * This method returns resources that are being followed by the authenticated user.
	 * @param source
	 * @param type
	 * @param parameters
	 * @return EntityList<FollowedResource>
	 * @throws FollowServiceException
	 */
	public EntityList<FollowedResource> getFollowedResources(String source, String type, Map<String, String> parameters)  throws FollowServiceException{
		try {
			return getResources(resolveUrl(source, type),parameters);
		} catch (ClientServicesException e) {
			throw new FollowServiceException(e, "Problem occured in getFollowedResources");
		}
		
	}
	
	
	/*
	 * This method returns specific resource that is being followed by the authenticated user.
	 * @param source
	 * @param type
	 * @param resourceId
	 * @return FollowedResource
	 * @throws FollowServiceException
	 */
	public FollowedResource getFollowedResource(String source, String type, String resourceId)  throws FollowServiceException{
		try {
			return getResource(resolveUrl(source, type, resourceId),null);
		} catch (ClientServicesException e) {
			throw new FollowServiceException(e, "Problem occured in getFollowedResource");
		}
		
	}
	
	/* This method allows authenticated user to start following a resource
	 * @param source
	 * @param type
	 * @param resourceId
	 * @return FollowedResource
	 * @throws FollowServiceException
	 */
	public FollowedResource startFollowing(String source, String type, String resourceId)  throws FollowServiceException{
		Response result = null;
		try {
			FollowedResource resource = new FollowedResource();
			resource.setResourceId(resourceId);
			resource.setSource(source);
			resource.setResourceType(type);
			FollowTransformer transformer = new FollowTransformer();
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			String atomPayload = transformer.transform(resource.getFieldsMap());
			String url = resolveUrl(source, type);
			result = createData(url, null, headers, atomPayload);
			return getFollowFeedHandler().createEntity(result);

		} catch (TransformerException e) {
			throw new FollowServiceException(e, "Problem occured in startFollowing");
		} catch (ClientServicesException e) {
			throw new FollowServiceException(e, "Problem occured in startFollowing");
		} catch (IOException e) {
			throw new FollowServiceException(e, "Problem occured in startFollowing");
		}
	}
	
	/* This method allows authenticated user to stop following a resource
	 * @param source
	 * @param type
	 * @param resourceId
	 * @return boolean
	 * @throws FollowServiceException
	 */
	public boolean stopFollowing(String source,String type,String resourceId) throws FollowServiceException{
		String stopResourceUrl = resolveUrl(source, type, resourceId);
		try {
			Map parameters = new HashMap<String,String>();
			deleteData(stopResourceUrl+"&resource="+resourceId, parameters, resourceId);
			return true;
		} catch (Exception e) {
			throw new FollowServiceException(e, "Problem occured in stopFollowing");
		}
		
	}
	
	private EntityList<FollowedResource> getResources(String apiUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return (EntityList<FollowedResource>)getEntities(apiUrl, parameters, getFollowFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}

	}
		
		
	private FollowedResource getResource(String apiUrl,Map<String, String> parameters) throws ClientServicesException {

		try {
			EntityList resources = (EntityList<FollowedResource>)getEntities(apiUrl, parameters, getFollowFeedHandler());
			if(resources!=null && resources.size()>0){
				return (FollowedResource) resources.get(0);
			}else{
				return null;
			}
			
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	private IFeedHandler<FollowedResource> getFollowFeedHandler() {
		return new AtomFeedHandler<FollowedResource>(this) {
			@Override
			protected FollowedResource newEntity(BaseService service, Node node) {
				XPathExpression xpath = (node instanceof Document) ? (XPathExpression)AtomXPath.singleEntry.getPath() : null;
				return new FollowedResource(service, node, ConnectionsConstants.nameSpaceCtx, xpath);
			}
		};
	}
	
	
	private String resolveUrl(String source, String type){
		return resolveUrl(source, type, null);
	}
	
	private String resolveUrl(String source, String type, String resourceId){
		
		StringBuilder apiUrl = new StringBuilder(getService(source));
		apiUrl.append("/follow");
		
		if (AuthUtil.INSTANCE.getAuthValue(endpoint).equalsIgnoreCase(ConnectionsConstants.OAUTH)) {
			apiUrl.append("/oauth");
		}
		
		apiUrl.append("/atom/resources");
		
		if(StringUtil.isNotEmpty(resourceId)){
			apiUrl.append("/"+resourceId);
		}
		
		apiUrl.append("?source=").append(source);
		apiUrl.append("&type=").append(type);
		
		if(StringUtil.isNotEmpty(resourceId)){
			apiUrl.append("&resource="+resourceId);
		}
		
		return apiUrl.toString();
		
	}
	
	private String getService(String source){
		String service = "";
		if(StringUtil.equals(source, "activities")){
			service = ContextRootMap.ACTIVITIES;
		}else if (StringUtil.equals(source, "blogs")) {
			service = ContextRootMap.BLOGS;
		}else if (StringUtil.equals(source, "communities")) {
			service = ContextRootMap.COMMUNITIES;
		}else if (StringUtil.equals(source, "forums")) {
			service = ContextRootMap.FORUMS;
		}else if (StringUtil.equals(source, "profiles")) {
			service = ContextRootMap.PROFILES;
		}else if (StringUtil.equals(source, "wikis")) {
			service = ContextRootMap.WIKIS;
		}else if (StringUtil.equals(source, "tags")) {
			service = ContextRootMap.TAGS;
		}
	
		return service;
		
	}

	

}

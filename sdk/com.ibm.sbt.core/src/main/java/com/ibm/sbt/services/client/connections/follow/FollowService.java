/*
 * ��� Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
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
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.follow.transformer.FollowTransformer;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * Use this Service to keep track of IBM� Connections resources that interest you, such as a person or community, a blog or a particular file.
 * @author Manish Kataria 
 * @author Carlos Manias
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

	@Override
	public NamedUrlPart getAuthType(){
		NamedUrlPart part = super.getAuthType();
		if (!part.getValue().equalsIgnoreCase("oauth")) {
			part = new NamedUrlPart("authType","");
		}
		return part;
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
			String url = FollowUrls.FOLLOW_URL.format(getApiVersion(), FollowServiceType.getByName(source), getAuthType());
			return getResources(url, generateParams(parameters, source, type, null));
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
			String url = FollowUrls.FOLLOW_URL.format(getApiVersion(), FollowServiceType.getByName(source), getAuthType(), Resource.get(resourceId));
			return getResource(url, generateParams(null, source, type, resourceId));
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
			String url = FollowUrls.FOLLOW_URL.format(getApiVersion(), FollowServiceType.getByName(source), getAuthType());
			result = createData(url, generateParams(null, source, type, null), headers, atomPayload);
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
		try {
			String stopResourceUrl = FollowUrls.FOLLOW_URL.format(getApiVersion(), FollowServiceType.getByName(source), getAuthType(), Resource.get(resourceId));
			deleteData(stopResourceUrl, generateParams(null, source, type, resourceId), resourceId);
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
	
	private Map<String, String> generateParams(Map<String, String> params, String source, String type, String resourceId){
		params = params == null? new HashMap<String, String>():params;
		if (!StringUtil.isNotEmpty(source)){
			params.put("source", source);
		}
		if (!StringUtil.isNotEmpty(type)){
			params.put("type", type);
		}
		if (!StringUtil.isNotEmpty(resourceId)){
			params.put("resource", resourceId);
		}
		return params;
	}

}

/*
 * © Copyright IBM Corp. 2013
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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.CommonConstants.HTTPCode;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.follow.serializers.FollowSerializer;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * Use this Service to keep track of IBM® Connections resources that interest you, 
 * such as a person or community, a blog or a particular file.
 * 
 * @see
 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation#action=openDocument&res_title=Following_API_ic45&content=pdcontent&sa=true">
 *			Forums API</a>
 * 
 * @author Manish Kataria 
 * @author Carlos Manias
 * 
 * http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_the_followed_resources_ic45&content=pdcontent
 */

public class FollowService extends ConnectionsService {
	
	private static final long serialVersionUID = 8450637561663717438L;

	/**
	 * Create FollowService instance with default endpoint.
	 */
	public FollowService() {
		super();
	}
	
	/**
	 * Create FollowService instance with specified endpoint.
	 * 
	 * @param endpoint
	 */
	public FollowService(String endpoint) {
		super(endpoint);
	}
	
	/**
	 * Create FollowService instance with specified endpoint.
	 * 
	 * @param endpoint
	 */
	public FollowService(Endpoint endpoint) {
		super(endpoint);
	}

	@Override
	protected void initServiceMappingKeys(){
		serviceMappingKeys = new String[]{""};
	}

	@Override
	public NamedUrlPart getAuthType(){
		NamedUrlPart part = super.getAuthType();
		if (!part.getValue().equalsIgnoreCase("oauth")) {
			part = new NamedUrlPart("authType","");
		}
		return part;
    }

	/***************************************************************
	 * Getting a feed of the followed resources
	 ****************************************************************/
	
	/**
	 * You can use this Atom API request to get a feed that lists the resources
	 * that are being followed by the authenticated user. 
	 * You must provide the values of the source and type as parameters to this request
	 * 
	 * @param source
	 * @param type
	 * @return EntityList<FollowedResource>
	 * @throws ClientServicesException
	 */
	public EntityList<FollowedResource> getFollowedResources(String source, String type) throws ClientServicesException {
		return getFollowedResources(source,type,null);
	}
	
	/**
	 * You can use this Atom API request to get a feed that lists the resources
	 * that are being followed by the authenticated user. 
	 * You must provide the values of the source and type as parameters to this request
	 * 
	 * @param source
	 * @param type
	 * @param parameters
	 * @return EntityList<FollowedResource>
	 * @throws ClientServicesException
	 */
	public EntityList<FollowedResource> getFollowedResources(String source, String type, Map<String, String> parameters)  throws ClientServicesException {
		String url = FollowUrls.format(source, this, Resource.get(""));
		return getResources(url, generateParams(parameters, source, type, null));
	}
	
	
	/**
	 * You can use this Atom API request to get a feed that lists the resources
	 * that are being followed by the authenticated user. 
	 * You must provide the values of the source and type as parameters to this request
	 * 
	 * @param source
	 * @param type
	 * @param resourceId
	 * @return FollowedResource
	 * @throws ClientServicesException
	 */
	public FollowedResource getFollowedResource(String source, String type, String resourceId)  throws ClientServicesException{
		String url = FollowUrls.format(source, this, Resource.get(resourceId));
		return getResource(url, generateParams(null, source, type, resourceId));
	}
	
	/***************************************************************
	 * Start following a resource
	 ****************************************************************/

	/** 
	 * 
	 * To start following a resource, send an Atom entry document 
	 * containing information about the resource to the followed resources feed. <br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param source
	 * @param type
	 * @param resourceId
	 * @return FollowedResource
	 * @throws ClientServicesException
	 */
	public FollowedResource startFollowing(String source, String type, String resourceId)  throws ClientServicesException {
		FollowedResource resource = new FollowedResource();
		resource.setResourceId(resourceId);
		resource.setSource(source);
		resource.setResourceType(type);
		FollowSerializer serializer = new FollowSerializer(resource);
		String atomPayload = serializer.startFollowingPayload();
		String url = FollowUrls.format(source, this, Resource.get(resourceId));
		Response response = createData(url, generateParams(null, source, type, null), getAtomHeaders(), atomPayload);
		//Returns 200 but should be 201
		checkResponseCode(response, HTTPCode.OK);
		return getFollowFeedHandler().createEntity(response);
	}

	/***************************************************************
	 * Stop following a resource
	 ****************************************************************/
	
	/** 
	 * To stop following a resource, use the HTTP DELETE method. <br>
	 * See Authenticating requests for information about how to authenticate the request.
	 * 
	 * @param source
	 * @param type
	 * @param resourceId
	 * @return boolean
	 * @throws ClientServicesException
	 */
	public boolean stopFollowing(String source,String type,String resourceId) throws ClientServicesException{
		String stopResourceUrl = FollowUrls.format(source, this, Resource.get(resourceId));
		Response response = deleteData(stopResourceUrl, generateParams(null, source, type, resourceId), resourceId);
		//Returns 202 but should be 204
		checkResponseCode(response, HTTPCode.ACCEPTED);
		return true;
	}

	/***************************************************************
	 * Factory methods
	 ****************************************************************/
	
	protected EntityList<FollowedResource> getResources(String apiUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(apiUrl, parameters, getFollowFeedHandler());
	}
		
	protected FollowedResource getResource(String apiUrl,Map<String, String> parameters) throws ClientServicesException {
		EntityList<FollowedResource> resources = (EntityList<FollowedResource>)getEntities(apiUrl, parameters, getFollowFeedHandler());
		if(resources!=null && resources.size()>0){
			return resources.get(0);
		}else{
			return null;
		}
	}

	/***************************************************************
	 * Handler Factory methods
	 ****************************************************************/
	
	protected IFeedHandler<FollowedResource> getFollowFeedHandler() {
		return new AtomFeedHandler<FollowedResource>(this, false) {
			@Override
			protected FollowedResource entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new FollowedResource(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/***************************************************************
	 * Utility methods
	 ****************************************************************/
	
	private Map<String, String> generateParams(Map<String, String> params, String source, String type, String resourceId){
		params = params == null? new HashMap<String, String>():params;
		if (StringUtil.isNotEmpty(source)){
			params.put("source", source);
		}
		if (StringUtil.isNotEmpty(type)){
			params.put("type", type);
		}
		if (StringUtil.isNotEmpty(resourceId)){
			params.put("resource", resourceId);
		}
		return params;
	}
}

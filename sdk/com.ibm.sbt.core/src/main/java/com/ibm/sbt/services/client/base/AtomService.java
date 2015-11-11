/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.base;

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * 
 * @author mwallace
 *
 */
abstract public class AtomService extends BaseService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public AtomService() {
	}

	/**
	 * Constructor
	 * 
	 * @param endpointName
	 */
	public AtomService(String endpointName) {
		super(endpointName);
	}

	/**
     * Constructor
     * 
     * @param endpointName
     * @param cacheSize
     */
    public AtomService(String endpointName, int cacheSize) {
       super(endpointName, cacheSize);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     */
    public AtomService(Endpoint endpoint) {
        super(endpoint);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public AtomService(Endpoint endpoint, int cacheSize) {
    	super(endpoint, cacheSize);
    }
    
	protected AtomEntity getAtomEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		try {
			return getEntity(requestUrl, parameters, getAtomFeedHandler(false));
		} catch (ClientServicesException e) {
			throw e;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}
    
	protected EntityList<AtomEntity> getAtomEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (EntityList<AtomEntity>)getEntities(requestUrl, getParameters(parameters), getAtomFeedHandler(true));
	}
	
	protected IFeedHandler<AtomEntity> getAtomFeedHandler(boolean isFeed) {
		return new AtomFeedHandler<AtomEntity>(this, isFeed) {

			@Override
			protected AtomEntity entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new AtomEntity(service, node, nameSpaceCtx, xpath);
			}
		};
	}
	
	protected Map<String, String> getParameters(Map<String, String> parameters) {
		if(parameters == null) return new HashMap<String, String>();
		else return parameters;
	}
	
}

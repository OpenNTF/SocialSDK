/*
 * � Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.wikis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.wikis.serializers.WikiPageSerializer;
import com.ibm.sbt.services.client.connections.wikis.serializers.WikiSerializer;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * @author Mario Duarte
 *
 */
public class WikiService extends BaseService {
	
	public WikiService() {
		super();
	}
	
	public WikiService(String endpoint) {
		super(endpoint);
	}
	
	public WikiService(Endpoint endpoint) {
		super(endpoint);
	}
	
	/**
	 * This returns a list of wikis to which the authenticated user has access. 
	 * @param parameters 
	 * @return 
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getAllWikis(Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.ALL_WIKIS.format(getApiVersion(), getAuthTypePart());
		return getWikiEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the 
	 * Wikis application has access. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getPublicWikis(Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.PUBLIC_WIKIS.format(getApiVersion(), getAuthTypePart());
		return getWikiEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis of which the authenticated user is a member. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getMyWikis(Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.MY_WIKIS.format(getApiVersion(), getAuthTypePart());
		return getWikiEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis 
	 * application has access. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getWikisWithMostComments(Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.MOST_COMMENTED_WIKIS.format(getApiVersion(), getAuthTypePart());
		return getWikiEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis 
	 * application has access. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getWikisWithMostRecommendations(Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.MOST_RECOMMENDED_WIKIS.format(getApiVersion(), getAuthTypePart());
		return getWikiEntityList(requestUrl, parameters);
	}
	
	/**
	 * This returns a list of wikis to which everyone who can log into the Wikis 
	 * application has access. 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getMostVisitedWikis(Map<String, String> parameters)  
			throws ClientServicesException {
		String requestUrl = WikiUrls.MOST_VISITED_WIKIS.format(getApiVersion(), getAuthTypePart());
		return getWikiEntityList(requestUrl, parameters);
	}

	/**
	 * Get a feed that lists all of the pages in a specific wiki. 
	 * @param wikiLabel
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getWikiPages(String wikiLabel) 
			throws ClientServicesException {
		return getWikiPages(wikiLabel, new HashMap<String, String>());
	}
	
	/**
	 * Get a feed that lists all of the pages in a specific wiki. 
	 * @param wikiLabel
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getWikiPages(String wikiLabel, Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGES.format(getApiVersion(), getAuthTypePart(), wikiLabel);
		return getWikiPagesEntityList(requestUrl, parameters);
	}
	
	/**
	 * Get a feed that lists all of the pages in a specific wiki that have been added 
	 * or edited by the authenticated user.
	 * @param wikiLabel
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getMyWikiPages(String wikiLabel, Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_MYPAGES.format(getApiVersion(), getAuthTypePart(), wikiLabel);
		return getWikiPagesEntityList(requestUrl, parameters);
	}
	
	/**
	 * Get a feed that lists the pages that have been deleted from wikis and are currently 
	 * stored in the trash.
	 * @param wikiLabelOrId
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getWikiPagesInTrash(String wikiLabelOrId, 
			Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGES_TRASH.format(getApiVersion(), getAuthTypePart(), wikiLabelOrId);
		return getWikiPagesEntityList(requestUrl, parameters);
	}
	
	
	/***************************************************************
	 * Working with wikis 
	 ****************************************************************/
	
	/**
	 * Retrieves a wiki
	 * @param wikiLabel
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public Wiki getWiki(String wikiLabel, Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_AUTH.format(getApiVersion(), getAuthTypePart(), wikiLabel); 
		return getWikiEntity(requestUrl, parameters);
	}
	
	/**
	 * Create a wiki programmatically
	 * @param wiki
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public Wiki createWiki(Wiki wiki, Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.ALL_WIKIS.format(getApiVersion(), getAuthTypePart());
		Response response = createWiki(requestUrl, wiki, parameters);
		return getWikiFeedHandler().createEntity(response);
	}
	
	/**
	 * Update an existing wiki
	 * @param wikiLabel
	 * @param wiki
	 * @param parameters
	 * @throws ClientServicesException
	 */
	public void updateWiki(Wiki wiki, Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_AUTH.format(getApiVersion(), getAuthTypePart(), wiki.getLabel());
		updateWikiAux(requestUrl, wiki, parameters);
	}
	
	/**
	 * Delete a wiki
	 * @param wikiLabel
	 * @throws ClientServicesException
	 */
	public void deleteWiki(String wikiLabel) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_AUTH.format(getApiVersion(), getAuthTypePart(), wikiLabel);
		deleteData(requestUrl);
	}
	
	
	/***************************************************************
	 * Working with wiki pages 
	 ****************************************************************/
	/**
	 * Retrieves a wiki page.
	 * @param wikiLabel
	 * @param pageLabel
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public WikiPage getWikiPage(String wikiLabel, String pageLabel, 
			Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGE_AUTH.format(getApiVersion(), getAuthTypePart(), wikiLabel, pageLabel);
		return getWikiPageEntity(requestUrl, parameters);
	}
	
	/**
	 * Create a wiki page programmatically.
	 * @param wikiPage
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public WikiPage createWikiPage(String wikiLabel, WikiPage wikiPage, Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGES_AUTH.format(getApiVersion(), getAuthTypePart(), wikiLabel);
		Response response = createWikiPageAux(requestUrl, wikiPage, parameters);
		return getWikiPageFeedHandler().createEntity(response);
	}
	
	/**
	 * Update an existing wiki page.
	 * @param wikiLabel
	 * @param wikPage
	 * @param parameters
	 * @throws ClientServicesException
	 */
	public void updateWikiPage(String wikiLabel, WikiPage wikiPage, 
			Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGE_AUTH.format(getApiVersion(), getAuthTypePart(), wikiLabel, wikiPage.getLabel());
		updateWikiPageAux(requestUrl, wikiPage, parameters);
	}
	
	/**
	 * Delete a wiki page.
	 * @param wikiLabel
	 * @param wikiPageLable
	 * @throws ClientServicesException
	 */
	public void deleteWikiPage(String wikiLabel, String wikiPageLable) 
			 throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGE_AUTH.format(getApiVersion(), getAuthTypePart(), wikiLabel, wikiPageLable);
		deleteData(requestUrl);
	}
	


	/***************************************************************
	 * Utility methods
	 ****************************************************************/
	
	private Response createWiki(String requestUrl, Wiki wiki,  
			Map<String, String> parameters) throws ClientServicesException {
		try {
			Map<String,String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			WikiSerializer serializer = new WikiSerializer(wiki);
			serializer.generateCreate();
			return createData(requestUrl, parameters, headers, serializer.serializeToString());
		}
		catch(ClientServicesException e) {
			throw e;
		}
		catch(Exception e) {
			throw new ClientServicesException(e);
		}
	}
	
	private Response updateWikiAux(String requestUrl, Wiki wiki,  
			Map<String, String> parameters) throws ClientServicesException {
		try {
			Map<String,String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			WikiSerializer serializer = new WikiSerializer(wiki);
			serializer.generateUpdate();
			return updateData(requestUrl, parameters, headers, serializer.serializeToString(), null);
		}
		catch(ClientServicesException e) {
			throw e;
		}
		catch(Exception e) {
			throw new ClientServicesException(e);
		}
	}
	
	private Response createWikiPageAux(String requestUrl, WikiPage wikiPage,  
			Map<String, String> parameters) throws ClientServicesException {
		try {
			Map<String,String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			WikiPageSerializer serializer = new WikiPageSerializer(wikiPage);
			serializer.generateCreate();
			return createData(requestUrl, parameters, headers, serializer.serializeToString());
		}
		catch(ClientServicesException e) {
			throw e;
		}
		catch(Exception e) {
			throw new ClientServicesException(e);
		}
	}
	
	private Response updateWikiPageAux(String requestUrl, WikiPage wikiPage,
			Map<String, String> parameters) throws ClientServicesException {
		try {
			Map<String,String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			WikiPageSerializer serializer = new WikiPageSerializer(wikiPage);
			serializer.generateUpdate();
			return updateData(requestUrl, parameters, headers, serializer.serializeToString(), null);
		}
		catch(ClientServicesException e) {
			throw e;
		}
		catch(Exception e) {
			throw new ClientServicesException(e);
		}
	}
	
	private void deleteData(String requestUrl) throws ClientServicesException {
		getClientService().delete(requestUrl, null, null, new ClientService.HandlerRaw());
	}
	
	private EntityList<Wiki> getWikiEntityList(String requestUrl, 
			Map<String, String> parameters) throws ClientServicesException {
		try {
			return (EntityList<Wiki>)getEntities(requestUrl, getParameters(parameters), 
					getWikiFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	private Wiki getWikiEntity(String requestUrl, Map<String, String> parameters) 
			throws ClientServicesException {
		try {
			return (Wiki)getEntity(requestUrl, parameters, getWikiFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	private EntityList<WikiPage> getWikiPagesEntityList(String requestUrl, 
			Map<String, String> parameters) throws ClientServicesException {
		try {
			return (EntityList<WikiPage>)getEntities(requestUrl, 
					getParameters(parameters), getWikiPageFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	private WikiPage getWikiPageEntity(String requestUrl, Map<String, String> parameters) 
			throws ClientServicesException {
		try {
			return (WikiPage)getEntity(requestUrl, parameters, getWikiPageFeedHandler());
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}
	}
	
	public IFeedHandler<Wiki> getWikiFeedHandler() {
		return new AtomFeedHandler<Wiki>(this) {
			@Override
			protected Wiki newEntity(BaseService service, Node node) {
				XPathExpression xpath = (node instanceof Document) ? (XPathExpression)AtomXPath.singleEntry.getPath() : null;
				return new Wiki(service, node, ConnectionsConstants.nameSpaceCtx, xpath);
			}
		};
	}
	
	public IFeedHandler<WikiPage> getWikiPageFeedHandler() {
		return new AtomFeedHandler<WikiPage>(this) {
			@Override
			protected WikiPage newEntity(BaseService service, Node node) {
				XPathExpression xpath = (node instanceof Document) ? (XPathExpression)AtomXPath.singleEntry.getPath() : null;
				return new WikiPage(service, node, ConnectionsConstants.nameSpaceCtx, xpath);
			}
		};
	}
	
	private Map<String, String> getParameters(Map<String, String> parameters) {
		if(parameters == null) return new HashMap<String, String>();
		else return parameters;
	}
}
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

package com.ibm.sbt.services.client.connections.wikis;

import static com.ibm.sbt.services.client.base.CommonConstants.APPLICATION_ATOM_XML;
import static com.ibm.sbt.services.client.base.CommonConstants.CONTENT_TYPE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.wikis.serializers.WikiPageSerializer;
import com.ibm.sbt.services.client.connections.wikis.serializers.WikiSerializer;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * The Wikis application of IBM® Connections enables teams to create a shared repository of information.
 * The Wikis API allows application programs to create new wikis, and to read and modify existing wikis. 
 * 
 * @see
 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.5+API+Documentation#action=openDocument&res_title=Wikis_API_ic45&content=pdcontent">
 *			Wikis API</a>
 * 
 * @author Mario Duarte
 * @author Carlos Manias
 *
 */
public class WikiService extends ConnectionsService {
	
	private static final long serialVersionUID = -1677227570229926652L;

	/**
	 * Create WikiService instance with default endpoint.
	 */
	public WikiService() {
		super();
	}
	
	/**
	 * Create WikiService instance with specified endpoint.
	 * 
	 * @param endpoint
	 */
	public WikiService(String endpoint) {
		super(endpoint);
	}
	
	/**
	 * Create WikiService instance with specified endpoint.
	 * 
	 * @param endpoint
	 */
	public WikiService(Endpoint endpoint) {
		super(endpoint);
	}

	/**
	 * Return mapping key for this service
	 */
	@Override
	public String getServiceMappingKey() {
		return "wikis";
	}

	/***************************************************************
	 * Getting Wiki feeds
	 ****************************************************************/

	/**
	 * Get a feed that lists all of the wikis.
	 * This returns a feed of wikis to which the authenticated user has access.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_all_wikis_ic40a&content=pdcontent">
	 *			Getting a feed of all wikis</a>
	 * 
	 * @param parameters 
	 * @return EntityList&lt;Wiki&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getAllWikis() throws ClientServicesException {
		return getAllWikis(null);
	}

	/**
	 * Get a feed that lists all of the wikis.
	 * This returns a feed of wikis to which the authenticated user has access.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_all_wikis_ic40a&content=pdcontent">
	 *			Getting a feed of all wikis</a>
	 * 
	 * @param parameters 
	 * @return EntityList&lt;Wiki&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getAllWikis(Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.ALL_WIKIS.format(this);
		return getWikiEntityList(requestUrl, parameters);
	}

	/**
	 * Get a feed that lists all of the public wikis.
	 * This returns a feed of wikis to which everyone who can log into the Wikis application has access.
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_public_wikis_ic40a&content=pdcontent">
	 *			Getting a feed of public wikis</a>
	 *
	 * @param parameters
	 * @return EntityList&lt;Wiki&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getPublicWikis() throws ClientServicesException {
		return getPublicWikis(null);
	}

	
	/**
	 * Get a feed that lists all of the public wikis.
	 * This returns a feed of wikis to which everyone who can log into the Wikis application has access.
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_public_wikis_ic40a&content=pdcontent">
	 *			Getting a feed of public wikis</a>
	 *
	 * @param parameters
	 * @return EntityList&lt;Wiki&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getPublicWikis(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.PUBLIC_WIKIS.format(this);
		return getWikiEntityList(requestUrl, parameters);
	}

	/**
	 * Get a feed that lists all of the wikis of which the authenticated user is a member. 
	 * This returns a list of wikis of which the authenticated user is a member. 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_a_persons_wikis_ic40a&content=pdcontent">
	 *			Getting a feed of a person's wikis</a>
	 * 
	 * @return EntityList&lt;Wiki&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getMyWikis() throws ClientServicesException {
		return getMyWikis(null);
	}
	
	/**
	 * Get a feed that lists all of the wikis of which the authenticated user is a member. 
	 * This returns a list of wikis of which the authenticated user is a member. 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_a_persons_wikis_ic40a&content=pdcontent">
	 *			Getting a feed of a person's wikis</a>
	 * 
	 * @param parameters
	 * @return EntityList&lt;Wiki&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getMyWikis(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.MY_WIKIS.format(this);
		return getWikiEntityList(requestUrl, parameters);
	}

	/**
	 * Get a feed that returns all wikis sorted by wikis with the most comments first. 
	 * This returns a feed of wikis to which everyone who can log into the Wikis application has access. 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_the_wikis_with_the_most_comments_ic40a&content=pdcontent">
	 *			Getting a feed of the wikis with the most comments</a>
	 *
	 * @return EntityList&lt;Wiki&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getWikisWithMostComments() throws ClientServicesException {
		return getWikisWithMostComments(null); 
	}
	
	/**
	 * Get a feed that returns all wikis sorted by wikis with the most comments first. 
	 * This returns a feed of wikis to which everyone who can log into the Wikis application has access. 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_the_wikis_with_the_most_comments_ic40a&content=pdcontent">
	 *			Getting a feed of the wikis with the most comments</a>
	 *
	 * @param parameters
	 * @return EntityList&lt;Wiki&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getWikisWithMostComments(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.MOST_COMMENTED_WIKIS.format(this);
		return getWikiEntityList(requestUrl, parameters);
	}

	/**
	 * Get a feed that returns all wikis sorted by most recommended. 
	 * This returns a feed of wikis to which everyone who can log into the Wikis application has access.  
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_the_wikis_with_the_most_comments_ic40a&content=pdcontent">
	 *			Getting a feed of the wikis with the most recommendations</a>
	 *
	 * @return EntityList&lt;Wiki&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getWikisWithMostRecommendations() throws ClientServicesException {
		return getWikisWithMostRecommendations(null);
	}
	
	/**
	 * Get a feed that returns all wikis sorted by most recommended. 
	 * This returns a feed of wikis to which everyone who can log into the Wikis application has access.  
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_the_wikis_with_the_most_comments_ic40a&content=pdcontent">
	 *			Getting a feed of the wikis with the most recommendations</a>
	 *
	 * @param parameters
	 * @return EntityList&lt;Wiki&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getWikisWithMostRecommendations(Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.MOST_RECOMMENDED_WIKIS.format(this);
		return getWikiEntityList(requestUrl, parameters);
	}

	/**
	 * Get a feed that returns all wikis sorted by most visited. 
	 * This returns a feed of wikis to which everyone who can log into the Wikis application has access.
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_the_wikis_with_the_most_comments_ic40a&content=pdcontent">
	 *			Getting a feed of the most visited wikis</a>
	 *
	 * @return EntityList&lt;Wiki&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getMostVisitedWikis()  throws ClientServicesException {
		return getMostVisitedWikis(null);
	}
	
	/**
	 * Get a feed that returns all wikis sorted by most visited. 
	 * This returns a feed of wikis to which everyone who can log into the Wikis application has access.
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_the_wikis_with_the_most_comments_ic40a&content=pdcontent">
	 *			Getting a feed of the most visited wikis</a>
	 *
	 * @param parameters
	 * @return EntityList&lt;Wiki&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<Wiki> getMostVisitedWikis(Map<String, String> parameters)  throws ClientServicesException {
		String requestUrl = WikiUrls.MOST_VISITED_WIKIS.format(this);
		return getWikiEntityList(requestUrl, parameters);
	}

	/**
	 * Get a feed that lists all of the pages in a specific wiki.
	 * This returns a feed of the pages in a given wiki. 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_wiki_pages_ic40a&content=pdcontent">
	 *			Getting a feed of the wiki pages</a>
	 *
	 * @param wikiLabel
	 * @return EntityList&lt;WikiPage&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getWikiPages(String wikiLabel) throws ClientServicesException {
		return getWikiPages(wikiLabel, null);
	}
	
	/**
	 * Get a feed that lists all of the pages in a specific wiki.
	 * This returns a feed of the pages in a given wiki. 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_wiki_pages_ic40a&content=pdcontent">
	 *			Getting a feed of the wiki pages</a>
	 *
	 * @param wikiLabel
	 * @param parameters
	 * @return EntityList&lt;WikiPage&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getWikiPages(String wikiLabel, Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGES.format(this, WikiUrls.getWikiLabel(wikiLabel));
		return getWikiPagesEntityList(requestUrl, parameters);
	}
	
	/**
	 * Get a feed that lists all of the pages in a specific wiki that have been added 
	 * or edited by the authenticated user.
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_wiki_pages_edited_by_someone_ic40a&content=pdcontent">
	 *			Getting a feed of the wiki pages edited by someone</a>
	 *
	 * @param wikiLabel
	 * @return EntityList&lt;WikiPage&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getMyWikiPages(String wikiLabel) throws ClientServicesException {
		return getMyWikiPages(wikiLabel, null);
	}

	/**
	 * Get a feed that lists all of the pages in a specific wiki that have been added 
	 * or edited by the authenticated user.
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_wiki_pages_edited_by_someone_ic40a&content=pdcontent">
	 *			Getting a feed of the wiki pages edited by someone</a>
	 *
	 * @param wikiLabel
	 * @param parameters
	 * @return EntityList&lt;WikiPage&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getMyWikiPages(String wikiLabel, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_MYPAGES.format(this, WikiUrls.getWikiLabel(wikiLabel));
		return getWikiPagesEntityList(requestUrl, parameters);
	}

	/**
	 * Get a feed that lists the pages that have been deleted from wikis and are currently 
	 * stored in the trash.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_wiki_pages_in_the_trash_ic40a&content=pdcontent">
	 *			Getting a feed of the wiki pages in the trash</a>
	 *
	 * @param wikiLabelOrId
	 * @return EntityList&lt;WikiPage&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getWikiPagesInTrash(String wikiLabelOrId) throws ClientServicesException {
		return getWikiPagesInTrash(wikiLabelOrId, null);
	}
	
	/**
	 * Get a feed that lists the pages that have been deleted from wikis and are currently 
	 * stored in the trash.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Getting_a_feed_of_wiki_pages_in_the_trash_ic40a&content=pdcontent">
	 *			Getting a feed of the wiki pages in the trash</a>
	 *
	 * @param wikiLabelOrId
	 * @param parameters
	 * @return EntityList&lt;WikiPage&gt;
	 * @throws ClientServicesException
	 */
	public EntityList<WikiPage> getWikiPagesInTrash(String wikiLabelOrId, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGES_TRASH.format(this, WikiUrls.getWikiLabel(wikiLabelOrId));
		return getWikiPagesEntityList(requestUrl, parameters);
	}
	
	/***************************************************************
	 * Working with wikis 
	 ****************************************************************/

	/**
	 * Retrieve an Atom document of a wiki.<br>
	 * This method returns the Atom entry of a wiki as opposed to a feed of the wiki. 
	 * If you want to retrieve a feed, see Getting Wikis feeds.<br>
	 * You do not need to authenticate with the server to send a request to retrieve public resources.<br>
	 * If authentication is provided, the user must have permission to view the specified resource.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Retrieving_a_wiki_ic40a&content=pdcontent">
	 *			Retrieving a wiki</a>
	 *
	 * @param wikiLabel
	 * @return Wiki
	 * @throws ClientServicesException
	 */
	public Wiki getWiki(String wikiLabel) throws ClientServicesException {
		return getWiki(wikiLabel);

	}
	
	/**
	 * Retrieve an Atom document of a wiki.<br>
	 * This method returns the Atom entry of a wiki as opposed to a feed of the wiki. 
	 * If you want to retrieve a feed, see Getting Wikis feeds.<br>
	 * You do not need to authenticate with the server to send a request to retrieve public resources.<br>
	 * If authentication is provided, the user must have permission to view the specified resource.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Retrieving_a_wiki_ic40a&content=pdcontent">
	 *			Retrieving a wiki</a>
	 *
	 * @param wikiLabel
	 * @param parameters
	 * @return Wiki
	 * @throws ClientServicesException
	 */
	public Wiki getWiki(String wikiLabel, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_AUTH.format(this, WikiUrls.getWikiLabel(wikiLabel)); 
		return getWikiEntity(requestUrl, parameters);
	}

	/**
	 * Use the API to create a wiki programmatically.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Creating_a_wiki_ic40a&content=pdcontent">
	 *			Creating a wiki</a>
	 *
	 * @param wiki
	 * @return Wiki
	 * @throws ClientServicesException
	 */
	public Wiki createWiki(Wiki wiki) throws ClientServicesException {
		return createWiki(wiki);
	}
	
	/**
	 * Use the API to create a wiki programmatically.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Creating_a_wiki_ic40a&content=pdcontent">
	 *			Creating a wiki</a>
	 *
	 * @param wiki
	 * @param parameters
	 * @return Wiki
	 * @throws ClientServicesException
	 */
	public Wiki createWiki(Wiki wiki, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.ALL_WIKIS.format(this);
		Response response = createWiki(requestUrl, wiki, parameters);
		return getWikiFeedHandler().createEntity(response);
	}

	/**
	 * To update a wiki, send a replacement wiki definition entry document in Atom format to the existing wiki's edit web address.<br>
	 * All existing wiki information will be replaced with the new data. 
	 * To avoid deleting all existing data, retrieve any data you want to retain first, and send it back with this request.<br>
	 * For example, if you want to add a new tag to a wiki definition entry, retrieve the existing tags, 
	 * and send them all back with the new tag in the update request. 
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Updating_a_wiki_ic40a&content=pdcontent">
	 *			Updating a wiki</a>
	 *
	 * @param wikiLabel
	 * @param wiki
	 * @throws ClientServicesException
	 */
	public void updateWiki(Wiki wiki) throws ClientServicesException {
		updateWiki(wiki);
	}
	
	/**
	 * To update a wiki, send a replacement wiki definition entry document in Atom format to the existing wiki's edit web address.<br>
	 * All existing wiki information will be replaced with the new data. 
	 * To avoid deleting all existing data, retrieve any data you want to retain first, and send it back with this request.<br>
	 * For example, if you want to add a new tag to a wiki definition entry, retrieve the existing tags, 
	 * and send them all back with the new tag in the update request. 
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Updating_a_wiki_ic40a&content=pdcontent">
	 *			Updating a wiki</a>
	 *
	 * @param wikiLabel
	 * @param wiki
	 * @param parameters
	 * @throws ClientServicesException
	 */
	public void updateWiki(Wiki wiki, Map<String, String> parameters) 
			throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_AUTH.format(this, WikiUrls.getWikiLabel(wiki.getLabel()));
		updateWikiAux(requestUrl, wiki, parameters);
	}
	
	/**
	 * Delete a wiki.
	 * Only the owner of a wiki can delete it. Deleted wikis cannot be restored.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Deleting_wikis_ic40a&content=pdcontent">
	 *			Deleting a wiki</a>
	 *
	 * @param wikiLabel
	 * @throws ClientServicesException
	 */
	public void deleteWiki(String wikiLabel) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_AUTH.format(this, WikiUrls.getWikiLabel(wikiLabel));
		deleteData(requestUrl);
	}
	
	/***************************************************************
	 * Working with wiki pages 
	 ****************************************************************/

	/**
	 * Retrieve an Atom document of a wiki page. 
	 * This method returns the Atom entry of a wiki as opposed to a feed of the wiki. <br>
	 * If you want to retrieve a feed, see Getting Wikis feeds.
	 * You do not need to authenticate with the server to send a request to retrieve public resources. <br>
	 * If authentication is provided, the user must have permission to view the specified resource.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Retrieving_a_wiki_page_ic40a&content=pdcontent">
	 *			Retrieving a wiki page</a>
	 *
	 * @param wikiLabel
	 * @param pageLabel
	 * @return WikiPage
	 * @throws ClientServicesException
	 */
	public WikiPage getWikiPage(String wikiLabel, String pageLabel) throws ClientServicesException {
		return getWikiPage(wikiLabel, pageLabel);
	}

	/**
	 * Retrieve an Atom document of a wiki page. 
	 * This method returns the Atom entry of a wiki as opposed to a feed of the wiki. <br>
	 * If you want to retrieve a feed, see Getting Wikis feeds.
	 * You do not need to authenticate with the server to send a request to retrieve public resources. <br>
	 * If authentication is provided, the user must have permission to view the specified resource.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Retrieving_a_wiki_page_ic40a&content=pdcontent">
	 *			Retrieving a wiki page</a>
	 *
	 * @param wikiLabel
	 * @param pageLabel
	 * @param parameters
	 * @return WikiPage
	 * @throws ClientServicesException
	 */
	public WikiPage getWikiPage(String wikiLabel, String pageLabel, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGE_AUTH.format(this, WikiUrls.getWikiLabel(wikiLabel), WikiUrls.getWikiPage(pageLabel));
		return getWikiPageEntity(requestUrl, parameters);
	}

	/**
	 * Create a wiki page programmatically.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Creating_a_wiki_page_programmatically_ic40a&content=pdcontent">
	 *			Creating a wiki page</a>
	 *
	 * @param wikiPage
	 * @return WikiPage
	 * @throws ClientServicesException
	 */
	public WikiPage createWikiPage(String wikiLabel, WikiPage wikiPage) throws ClientServicesException {
		return createWikiPage(wikiLabel, wikiPage);
	}
	
	/**
	 * Create a wiki page programmatically.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Creating_a_wiki_page_programmatically_ic40a&content=pdcontent">
	 *			Creating a wiki page</a>
	 *
	 * @param wikiPage
	 * @param parameters
	 * @return WikiPage
	 * @throws ClientServicesException
	 */
	public WikiPage createWikiPage(String wikiLabel, WikiPage wikiPage, Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGES_AUTH.format(this, WikiUrls.getWikiLabel(wikiLabel));
		Response response = createWikiPageAux(requestUrl, wikiPage, parameters);
		return getWikiPageFeedHandler().createEntity(response);
	}

	/**
	 * Update a wiki page programmatically
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Updating_a_wiki_page_ic40a&content=pdcontent">
	 *			Updating a wiki page</a>
	 *
	 * @param wikiLabel
	 * @param wikiPage
	 * @throws ClientServicesException
	 */
	public void updateWikiPage(String wikiLabel, WikiPage wikiPage) throws ClientServicesException {
		updateWikiPage(wikiLabel, wikiPage);
	}
	
	/**
	 * Update a wiki page programmatically
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Updating_a_wiki_page_ic40a&content=pdcontent">
	 *			Updating a wiki page</a>
	 *
	 * @param wikiLabel
	 * @param wikiPage
	 * @param parameters
	 * @throws ClientServicesException
	 */
	public void updateWikiPage(String wikiLabel, WikiPage wikiPage, 
			Map<String, String> parameters) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGE_AUTH.format(this, WikiUrls.getWikiLabel(wikiLabel), WikiUrls.getWikiPage(wikiPage.getLabel()));
		updateWikiPageAux(requestUrl, wikiPage, parameters);
	}
	
	/**
	 * Delete a wiki page.
	 * Only the owner of a wiki page can delete it. Deleted wiki pages cannot be restored.
	 * 
	 * @see
	 *		<a href="http://www-10.lotus.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Deleting_wiki_pages_ic40a&content=pdcontent">
	 *			Delete a wiki page</a>
	 *
	 * @param wikiLabel
	 * @param wikiPageLable
	 * @throws ClientServicesException
	 */
	public void deleteWikiPage(String wikiLabel, String wikiPageLabel) throws ClientServicesException {
		String requestUrl = WikiUrls.WIKI_PAGE_AUTH.format(this, WikiUrls.getWikiLabel(wikiLabel), WikiUrls.getWikiPage(wikiPageLabel));
		deleteData(requestUrl);
	}

	/***************************************************************
	 * Handler factory methods
	 ****************************************************************/
	
	/**
	 * Returns a WikiFeedHandler
	 * @return IFeedHandler&lt;Wiki&gt;
	 */
	public IFeedHandler<Wiki> getWikiFeedHandler() {
		return new AtomFeedHandler<Wiki>(this, false) {
			@Override
			protected Wiki entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Wiki(service, node, nameSpaceCtx, xpath);
			}
		};
	}
	
	/**
	 * Returns a WikiPageFeedHandler
	 * @return IFeedHandler&lt;WikiPage&gt;
	 */
	public IFeedHandler<WikiPage> getWikiPageFeedHandler() {
		return new AtomFeedHandler<WikiPage>(this, false) {
			@Override
			protected WikiPage entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new WikiPage(service, node, nameSpaceCtx, xpath);
			}
		};
	}
	
	/***************************************************************
	 * Factory methods
	 ****************************************************************/
	
	protected EntityList<Wiki> getWikiEntityList(String requestUrl, 
			Map<String, String> parameters) throws ClientServicesException {
		return (EntityList<Wiki>)getEntities(requestUrl, parameters, 
				getWikiFeedHandler());
	}
	
	protected Wiki getWikiEntity(String requestUrl, Map<String, String> parameters) 
			throws ClientServicesException {
		return (Wiki)getEntity(requestUrl, parameters, getWikiFeedHandler());
	}
	
	protected EntityList<WikiPage> getWikiPagesEntityList(String requestUrl, 
			Map<String, String> parameters) throws ClientServicesException {
		return (EntityList<WikiPage>)getEntities(requestUrl, 
				parameters, getWikiPageFeedHandler());
	}
	
	protected WikiPage getWikiPageEntity(String requestUrl, Map<String, String> parameters) 
			throws ClientServicesException {
		return (WikiPage)getEntity(requestUrl, parameters, getWikiPageFeedHandler());
	}
	
	/***************************************************************
	 * Utility methods
	 ****************************************************************/

	private Response createWiki(String requestUrl, Wiki wiki,  
			Map<String, String> parameters) throws ClientServicesException {
		try {
			Map<String,String> headers = new HashMap<String, String>();
			headers.put(CONTENT_TYPE, APPLICATION_ATOM_XML);
			WikiSerializer serializer = new WikiSerializer(wiki);
			return createData(requestUrl, parameters, headers, serializer.createPayload());
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
			headers.put(CONTENT_TYPE, APPLICATION_ATOM_XML);
			WikiSerializer serializer = new WikiSerializer(wiki);
			return updateData(requestUrl, parameters, headers, serializer.updatePayload(), null);
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
			headers.put(CONTENT_TYPE, APPLICATION_ATOM_XML);
			WikiPageSerializer serializer = new WikiPageSerializer(wikiPage);
			return createData(requestUrl, parameters, headers, serializer.createPayload());
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
			headers.put(CONTENT_TYPE, APPLICATION_ATOM_XML);
			WikiPageSerializer serializer = new WikiPageSerializer(wikiPage);
			return updateData(requestUrl, parameters, headers, serializer.updatePayload(), null);
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
}

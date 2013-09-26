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

/**
 * Use the Search API to perform searches across the installed Connections applications.
 * 
 * Returns a list of results with the specified text in the title, description, or content. Encode the strings. By default, spaces are treated as an AND operator. The following operators are supported:
 *
 *  AND or &&: Searches for items that contain both words. For example: query=red%20AND%20test returns items that contain both the word red and the word test. AND is the default operator.
 *  NOT or !: Excludes the word that follows the operator from the search. For example: query=test%20NOT%20red returns items that contain the word test, but not the word red.
 *  OR: Searches for items that contain either of the words. For example: query=test%20OR%20red
 *  To search for a phrase, enclose the phrase in quotation marks (" ").
 *  +: The plus sign indicates that the word must be present in the result. For example: query=+test%20red returns only items that contain the word test and many that also contain red, but none that contain only the word red.
 *  ?: Use a question mark to match individual characters. For example: query=te%3Ft returns items that contain the words test, text, tent, and others that begin with te.
 *  -: The dash prohibits the return of a given word. This operator is similar to NOT. For example: query=test%20-red returns items that contains the word test, but not the word red.
 *
 * Note: Wildcard searches are permitted, but wildcard only searches (*) are not.
 * For more details about supported operators, see Advanced search options in the Using section of the product documentation.
 * 
 * @author Manish Kataria
 */


package com.ibm.sbt.services.client.connections.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.util.EntityUtil;
import com.ibm.sbt.services.client.connections.forums.ForumList;
import com.ibm.sbt.services.client.connections.forums.ForumServiceException;
import com.ibm.sbt.services.client.connections.forums.ForumType;
import com.ibm.sbt.services.client.connections.forums.feedhandler.ForumsFeedHandler;
import com.ibm.sbt.services.client.connections.search.feedhandler.ScopeFeedHandler;
import com.ibm.sbt.services.client.connections.search.feedhandler.SearchFeedHandler;
import com.ibm.sbt.services.util.AuthUtil;

public class SearchService extends BaseService {
	
	/**
	 * Used in constructing REST APIs
	 */
	private static final String	_baseUrl				= "/search/";
	private static final String _basicUrl				= "atom/";
	public static final String _oauthUrl				= "oauth/atom/";
	
	
	/**
	 * Default Constructor
	 */
	
	public SearchService() {
		this(DEFAULT_ENDPOINT_NAME);
	}
	
	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates SearchService Object with specified values of endpoint
	 */
	public SearchService(String endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}
	
    /**
     * Search IBM Connection for public information.
     * 
     * @param query Text to search for
     * @return ResultList
     * @throws SearchServiceException
     */
	public ResultList getResults(String query) throws SearchServiceException{
		return getResults(query, null);
	}
	
	
	/**
	 * Search IBM Connection for public information.
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getResults(String query,Map<String, String> parameters) throws SearchServiceException{
		ResultList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		
		try {
			String searchQry = resolveUrl(SearchType.PUBLIC);
			searchResults = (ResultList) getEntities(searchQry, parameters, new SearchFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new SearchServiceException(e);
		} catch (IOException e) {
			throw new SearchServiceException(e);
		}
		return searchResults;
		
	}
	
	
	/**
	 * Search IBM Connections for both public information and private
	 * information that you have access to. You must provide authentication
	 * information in the request to retrieve this resource.
	 * 
	 * @param query
	 *            Text to search for
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getMyResults(String query) throws SearchServiceException{
		return getMyResults(query, null);
	}
	
	
	/**
	 * Search IBM Connections for both public information and private
	 * information that you have access to. You must provide authentication
	 * information in the request to retrieve this resource.
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getMyResults(String query,Map<String, String> parameters) throws SearchServiceException{
		ResultList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		
		try {
			String searchQry = resolveUrl(SearchType.MY);
			searchResults = (ResultList) getEntities(searchQry, parameters, new SearchFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new SearchServiceException(e);
		} catch (IOException e) {
			throw new SearchServiceException(e);
		}
		return searchResults;
		
	}
	
	
	/**
	 * Search IBM Connection for public information, and then return the
	 * people associated with the results.
	 * 
	 * @param query
	 *            Text to search for
	 */
	public ResultList getPeople(String query) throws SearchServiceException{
		return getPeople(query, null);
	}
	
	
	/**
	 * Search IBM Connection for public information, and then return the
	 * people associated with the results.
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getPeople(String query,Map<String, String> parameters) throws SearchServiceException{
		ResultList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		
		try {
			String searchQry = resolveUrl(SearchType.PEOPLESEARCH);
			searchResults = (ResultList) getEntities(searchQry, parameters, new SearchFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new SearchServiceException(e);
		} catch (IOException e) {
			throw new SearchServiceException(e);
		}
		return searchResults;
		
	}
	
	
	/**
	 * Search IBM Connections for both public information and private
	 * information that you have access to. You must provide authentication
	 * information in the request to retrieve this resource.
	 * 
	 * @param query
	 *            Text to search for
	 */
	public ResultList getMyPeople(String query) throws SearchServiceException{
		return getMyPeople(query, null);
	}
	
	
	/**
	 * Search IBM Connections for both public information and private
	 * information that you have access to. You must provide authentication
	 * information in the request to retrieve this resource.
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getMyPeople(String query,Map<String, String> parameters) throws SearchServiceException{
		ResultList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		
		try {
			String searchQry = resolveUrl(SearchType.MYPEOPLESEARCH);
			searchResults = (ResultList) getEntities(searchQry, parameters, new SearchFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new SearchServiceException(e);
		} catch (IOException e) {
			throw new SearchServiceException(e);
		}
		return searchResults;
		
	}
	
	/**
	 * Search IBM Connection for public information, and then return the
	 * tags associated with the results.
	 * 
	 * @param query
	 *            Text to search for
	 * @return ResultList
	 * @throws SearchServiceException            
	 */
	public ResultList getTags(String query) throws SearchServiceException{
		return getTags(query, null);
	}
	
	
    /**
     * Search IBM Connection for public information, and then 
     * return the tags associated with the results.
     * 
     * @param query Text to search for
     * @param requestArgs
	 * @return ResultList
	 * @throws SearchServiceException
     */
	public ResultList getTags(String query,Map<String, String> parameters) throws SearchServiceException{
		ResultList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		
		try {
			String searchQry = resolveUrl(SearchType.TAGSSEARCH);
			searchResults = (ResultList) getEntities(searchQry, parameters, new SearchFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new SearchServiceException(e);
		} catch (IOException e) {
			throw new SearchServiceException(e);
		}
		return searchResults;
		
	}
	
	
    /**
     * Search IBM Connections for both public information and private 
     * information that you have access to, and then return the tags associated 
     * with the results. You must provide authentication information in the 
     * request to retrieve this resource.
	 * 
	 * @param query
	 *            Text to search for
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getMyTags(String query) throws SearchServiceException{
		return getMyTags(query, null);
	}
	
	
    /**
     * Search IBM Connections for both public information and private 
     * information that you have access to, and then return the tags associated 
     * with the results. You must provide authentication information in the 
     * request to retrieve this resource.
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getMyTags(String query,Map<String, String> parameters) throws SearchServiceException{
		ResultList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		
		try {
			String searchQry = resolveUrl(SearchType.MYTAGSSEARCH);
			searchResults = (ResultList) getEntities(searchQry, parameters, new SearchFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new SearchServiceException(e);
		} catch (IOException e) {
			throw new SearchServiceException(e);
		}
		return searchResults;
		
	}
	
	
	/**
	 * Search IBM Connection for public information, and then return the
	 * applications associated with the results and identify how many results
	 * were found per application.
	 * 
	 * @param query
	 *            Text to search for
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getApplications(String query) throws SearchServiceException{
		return getApplications(query, null);
	}
	
	
	/**
	 * Search IBM Connection for public information, and then return the
	 * applications associated with the results and identify how many results
	 * were found per application.
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getApplications(String query,Map<String, String> parameters) throws SearchServiceException{
		ResultList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		
		try {
			String searchQry = resolveUrl(SearchType.APPLICATIONS);
			searchResults = (ResultList) getEntities(searchQry, parameters, new SearchFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new SearchServiceException(e);
		} catch (IOException e) {
			throw new SearchServiceException(e);
		}
		return searchResults;
		
	}
	
	
	
	/**
	 * Search IBM Connections for both public information and private
	 * information that you have access to, and then return the applications
	 * associated with the results and identify how many results were found per
	 * application. You must provide authentication information in the request
	 * to retrieve this resource.
	 * 
	 * @param query
	 *            Text to search for
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getMyApplications(String query) throws SearchServiceException{
		return getMyApplications(query, null);
	}
	
	
	/**
	 * Search IBM Connections for both public information and private
	 * information that you have access to, and then return the applications
	 * associated with the results and identify how many results were found per
	 * application. You must provide authentication information in the request
	 * to retrieve this resource.
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getMyApplications(String query,Map<String, String> parameters) throws SearchServiceException{
		ResultList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		
		try {
			String searchQry = resolveUrl(SearchType.APPLICATIONS);
			searchResults = (ResultList) getEntities(searchQry, parameters, new SearchFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new SearchServiceException(e);
		} catch (IOException e) {
			throw new SearchServiceException(e);
		}
		return searchResults;
		
	}
	
	
	/**
	 * Search IBM Connection for public information, and then return the dates
	 * associated with the results.
	 * 
	 * @param query
	 *            Text to search for
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getDates(String query) throws SearchServiceException{
		return getDates(query, null);
	}
	
	
	/**
	 * Search IBM Connection for public information, and then return the dates
	 * associated with the results.
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getDates(String query,Map<String, String> parameters) throws SearchServiceException{
		ResultList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		
		try {
			String searchQry = resolveUrl(SearchType.DATE);
			searchResults = (ResultList) getEntities(searchQry, parameters, new SearchFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new SearchServiceException(e);
		} catch (IOException e) {
			throw new SearchServiceException(e);
		}
		return searchResults;
		
	}
	
	
	/**
	 * Search IBM Connections for both public information and private
	 * information that you have access to, and then return the dates associated
	 * with the results. You must provide authentication information in the
	 * request to retrieve this resource.
	 * 
	 * @param query
	 *            Text to search for
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getMyDates(String query) throws SearchServiceException{
		return getMyDates(query, null);
	}
	
	
	/**
	 * Search IBM Connections for both public information and private
	 * information that you have access to, and then return the dates associated
	 * with the results. You must provide authentication information in the
	 * request to retrieve this resource.
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getMyDates(String query,Map<String, String> parameters) throws SearchServiceException{
		ResultList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		
		try {
			String searchQry = resolveUrl(SearchType.MYDATE);
			searchResults = (ResultList) getEntities(searchQry, parameters, new SearchFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new SearchServiceException(e);
		} catch (IOException e) {
			throw new SearchServiceException(e);
		}
		return searchResults;
		
	}
	
	
	/**
     * Search IBM Connection for available scopes ( Applications in which search can be executed )
     * 
     * @method getScopes
     * @return ScopeList
	 * @throws SearchServiceException 
     */
     public ScopeList getScopes() throws SearchServiceException {
    	ScopeList scopes;
    	Map params = new HashMap<String,String>();
 		try {
			String searchQry = resolveUrl(SearchType.SCOPE);
			scopes = (ScopeList) getEntities(searchQry, params, new ScopeFeedHandler(this));
		} catch (ClientServicesException e) {
			throw new SearchServiceException(e);
		} catch (IOException e) {
			throw new SearchServiceException(e);
		}
		return scopes;
    }
	
	/*
	 * Internal service methods
	 */
	
	
	
	/*
	 * Method to generate appropriate REST URLs
	 * 
	 */
	protected String resolveUrl(SearchType searchType) {
		return resolveUrl(searchType,null);
	}
	
	/*
	 * Method to generate appropriate REST URLs
	 * 
	 */
	protected String resolveUrl(SearchType searchType,Map<String, String> params) {
		StringBuilder baseUrl = new StringBuilder(_baseUrl);
		
		if (AuthUtil.INSTANCE.getAuthValue(endpoint).equalsIgnoreCase(ConnectionsConstants.OAUTH)) {
			baseUrl.append(_oauthUrl);
		}else{
			baseUrl.append(_basicUrl);
		}
		
		if(searchType != null){
			baseUrl.append(searchType.getSearchType());
		}else{
			baseUrl.append(SearchType.PUBLIC.getSearchType()); // Use public search by default
		} 
		
		// Add required parameters
		if (null != params && params.size() > 0) {
			baseUrl.append(ConnectionsConstants.INIT_URL_PARAM);
			boolean setSeparator = false;
			for (Map.Entry<String, String> param : params.entrySet()) {
				String key = param.getKey();
				if (StringUtil.isEmpty(key)) continue;
				String value = EntityUtil.encodeURLParam(param.getValue());
				if (StringUtil.isEmpty(value)) continue;
				if (setSeparator) {
					baseUrl.append(ConnectionsConstants.URL_PARAM);
				} else {
					setSeparator = true;
				}
				baseUrl.append(key).append(ConnectionsConstants.EQUALS).append(value);
			}
		}
		return baseUrl.toString();
	}
}

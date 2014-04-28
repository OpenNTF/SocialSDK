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
 * @author Carlos Manias
 */


package com.ibm.sbt.services.client.connections.search;

import static com.ibm.sbt.services.client.base.CommonConstants.COMMA;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.endpoints.Endpoint;

public class SearchService extends BaseService {
	
	private static final long serialVersionUID = -8445895408209299706L;

	
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
	 *            Creates SearchService Object with the specified endpoint
	 */
	public SearchService(String endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}
	
	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates SearchService Object with the specified endpoint
	 */
	public SearchService(Endpoint endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Return mapping key for this service
	 */
	@Override
	public String getServiceMappingKey() {
		return "search";
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
	 * 
	 * @return
	 */
	public IFeedHandler<Result> getResultFeedHandler() {
		return new AtomFeedHandler<Result>(this) {
			@Override
			protected Result entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Result(service, node, nameSpaceCtx, xpath);
			}

			@Override
			public ResultList createEntityList(Response requestData) {
				return new ResultList((Response)requestData, this);
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public IFeedHandler<Scope> getScopeFeedHandler() {
		return new AtomFeedHandler<Scope>(this) {
			@Override
			protected Scope entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Scope(service, node, nameSpaceCtx, xpath);
			}

			@Override
			public ScopeList createEntityList(Response requestData) {
				return new ScopeList((Response)requestData, this);
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public IFeedHandler<FacetValue> getFacetValueFeedHandler(final String facetId) {
		return new AtomFeedHandler<FacetValue>(this) {
			@Override
			protected XPathExpression getEntityXPath(Node node){
				return FacetValueXPath.facetId.getFacetPath(facetId);
			}
			
			@Override
			protected FacetValue entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new FacetValue(service, node, nameSpaceCtx, xpath);
			}

			@Override
			public FacetValueList createEntityList(Response requestData) {
				return new FacetValueList((Response)requestData, this);
			}
			
		};
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
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		

		String searchQry = SearchUrls.SEARCH.format(this);
		return getResultEntityList(searchQry, parameters);
	}

/*	*//**
     * Search IBM Connections for public information, tagged with the specified tags.
     * 
	 * @param tag
	 * @return
	 * @throws SearchServiceException
	 *//*
    public ResultList getResultsByTag(String tag) throws SearchServiceException {
    	return getResultsByTag(new String[]{ tag }, null);
	}
	
    *//**
     * Search IBM Connections for public information, tagged with the specified tags.
     * 
     * @param tags
     * @param parameters
     * @return
     * @throws SearchServiceException
     *//*
    public ResultList getResultsByTag(String[] tags, Map<String, String> parameters) throws SearchServiceException {
		ResultList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		try {
			parameters.put("constraint", createTagConstraint(tags));		
			String searchQry = resolveUrl(SearchType.PUBLIC);
			searchResults = (ResultList) getEntities(searchQry, parameters, new SearchFeedHandler(this));
		} catch (Exception e) {
			throw new SearchServiceException(e);
		} 
		return searchResults;
	}*/
	
	/**
	 * Search IBM Connections for public information, tagged with the specified tags.
	 * 
	 * @param String
	 *            Single tag to be search for, for multiple tags use other overloaded method
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getResultsByTag(String tag) throws SearchServiceException{
		List<String> taglist = new ArrayList<String>();
		taglist.add(tag);
		return getResultsByTag(taglist,null);
	}
	
	/**
	 * Search IBM Connections for public information, tagged with the specified tags.
	 * 
	 * @param List
	 *            of Tags to searched for
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getResultsByTag(List<String> tags) throws SearchServiceException{
		return getResultsByTag(tags,null);
	}
	
	/**
	 * Search IBM Connections for public information, tagged with the specified tags.
	 * 
	 * @param List
	 *            of Tags to searched for
	 * @return ResultList
	 * @throws SearchServiceException
	 */
	public ResultList getResultsByTag(List<String> tags,
			Map<String, String> parameters) throws SearchServiceException {
		// High level wrapper, provides a convenient mechanism for search for
		// tags, uses constraints internally
		List<String> formattedTags = new ArrayList<String>();
		List<Constraint> constraints = new ArrayList<Constraint>();
		formattedTags = createTagConstraint(tags);
		Constraint constraint = new Constraint();
		constraint.setType("category");
		constraint.setValues(formattedTags);
		constraints.add(constraint);
		return getResultsWithConstraint("", constraints,parameters);
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
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		
		String searchQry = SearchUrls.MYSEARCH.format(this);
		return getResultEntityList(searchQry, parameters);
	}
	
	/**
	 * Search IBM Connection for public information, and then return the
	 * people associated with the results.
	 * 
	 * @param query
	 *            Text to search for
	 */
	public FacetValueList getPeople(String query) throws SearchServiceException{
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
	public FacetValueList getPeople(String query,Map<String, String> parameters) throws SearchServiceException{
		FacetValueList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);		
		parameters.put("pageSize", "0");
		parameters.put("facet", "{\"id\": \"Person\"}");

		String searchQry = SearchUrls.SEARCH.format(this);
		return getFacetValueEntityList(searchQry, parameters);
	}
	
	/**
	 * Search IBM Connections for both public information and private
	 * information that you have access to. You must provide authentication
	 * information in the request to retrieve this resource.
	 * 
	 * @param query
	 *            Text to search for
	 */
	public FacetValueList getMyPeople(String query) throws SearchServiceException{
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
	public FacetValueList getMyPeople(String query,Map<String, String> parameters) throws SearchServiceException{
		ResultList searchResults;
		
		if(parameters==null){
			parameters= new HashMap<String,String>();
		}
		
		parameters.put("query", query);	
		parameters.put("pageSize", "0");
		parameters.put("facet", "{\"id\": \"Person\"}");
		
		String searchQry = SearchUrls.MYSEARCH.format(this);
		return getFacetValueEntityList(searchQry, parameters);
	}
	
    /**
     * Search IBM Connection for public information with constraints
     * A field constraint allows only results matching specific field values.
     * 
     * @param query Text to search for
     * @param Constraint
     * 
     * @return ResultList
     * @throws SearchServiceException
     * 
	   *http://www-10.IBM.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Constraints&content=pdcontent  
     */
	public ResultList getResultsWithConstraint(String query, Constraint constraint) throws SearchServiceException{
		List<Constraint> constraintList = new ArrayList<Constraint>();
		constraintList.add(constraint);
		return getResultsWithConstraint(query, constraintList,null);
	}
	
    /**
     * Search IBM Connection for public information with constraints
     * A field constraint allows only results matching specific field values.
     * 
     * @param query Text to search for
     * @param List<Constraint>
     * 
     * @return ResultList
     * @throws SearchServiceException
     * 
	   *http://www-10.IBM.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Constraints&content=pdcontent  
     */
	public ResultList getResultsWithConstraint(String query, List<Constraint> constraints) throws SearchServiceException{
		return getResultsWithConstraint(query, constraints,null);
	}
    
    /**
     * Search IBM Connection for public information with constraints
     * A field constraint allows only results matching specific field values.
     * 
     * @param query Text to search for
     * @param List<Constraint>
     * 
     * @return ResultList
     * @throws SearchServiceException
     * 
	   *http://www-10.IBM.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Constraints&content=pdcontent  
     */
	public ResultList getResultsWithConstraint(String query, List<Constraint> constraints, Map<String, String> parameters) throws SearchServiceException{
		// We can not use a map of constraints, since there could be multiple constraints but map can have only one key named constraint
		String formattedConstraints = generateConstraintParameter(constraints);
		if(parameters == null){
			parameters = new HashMap<String, String>();
		}
		parameters.put("constraint", formattedConstraints.toString());
		return getResults(query, parameters);
	}

	/**
     * Search IBM Connection for available scopes ( Applications in which search can be executed )
     * 
     * @method getScopes
     * @return ScopeList
	 * @throws SearchServiceException 
     */
     public ScopeList getScopes() throws SearchServiceException {
    	Map<String,String> params = new HashMap<String,String>();
		String searchQry = SearchUrls.SCOPES.format(this);
		return getScopeEntityList(searchQry, params);
     }

	protected Result getResultEntity(String requestUrl, Map<String, String> parameters) throws SearchServiceException {
		try {
			return (Result)getEntity(requestUrl, getParameters(parameters), getResultFeedHandler());
		} catch (IOException e) {
			throw new SearchServiceException(e);
		} catch(ClientServicesException e){
			throw new SearchServiceException(e);
		}
	}

	protected ResultList getResultEntityList(String requestUrl, Map<String, String> parameters) throws SearchServiceException {
		try {
			return (ResultList)getEntities(requestUrl, getParameters(parameters), getResultFeedHandler());
		} catch (IOException e) {
			throw new SearchServiceException(e);
		} catch(ClientServicesException e){
			throw new SearchServiceException(e);
		}
	}

	protected FacetValue getFacetValueEntity(String requestUrl, Map<String, String> parameters) throws SearchServiceException {
		try {
			return (FacetValue)getEntity(requestUrl, getParameters(parameters), getFacetValueFeedHandler("Person"));
		} catch (IOException e) {
			throw new SearchServiceException(e);
		} catch(ClientServicesException e){
			throw new SearchServiceException(e);
		}
	}

	protected FacetValueList getFacetValueEntityList(String requestUrl, Map<String, String> parameters) throws SearchServiceException {
		try {
			return (FacetValueList)getEntities(requestUrl, getParameters(parameters), getFacetValueFeedHandler("Person"));
		} catch (IOException e) {
			throw new SearchServiceException(e);
		} catch(ClientServicesException e){
			throw new SearchServiceException(e);
		}
	}
     
	protected Scope getScopeEntity(String requestUrl, Map<String, String> parameters) throws SearchServiceException {
		try {
			return (Scope)getEntity(requestUrl, getParameters(parameters), getScopeFeedHandler());
		} catch (IOException e) {
			throw new SearchServiceException(e);
		} catch(ClientServicesException e){
			throw new SearchServiceException(e);
		}
	}

	protected ScopeList getScopeEntityList(String requestUrl, Map<String, String> parameters) throws SearchServiceException {
		try {
			return (ScopeList)getEntities(requestUrl, getParameters(parameters), getScopeFeedHandler());
		} catch (IOException e) {
			throw new SearchServiceException(e);
		} catch(ClientServicesException e){
			throw new SearchServiceException(e);
		}
	}

	/*
	 * Internal service methods
	 */
	
/*    private String createTagConstraint(String[] tags) throws JsonException {
    	String[] tagValues = new String[tags.length];
    	for (int i=0; i<tags.length; i++) {
    		tagValues[i] = "Tag/" + tags[i];
    	}
    	
    	JsonObject jsonObject = (JsonObject)JsonJavaFactory.instanceEx.createObject(null, null);
    	jsonObject.putJsonProperty("type", "category");
    	jsonObject.putJsonProperty("values", tagValues);
    	return jsonObject.toString();
    }*/
     
 	private List<String> createTagConstraint(List<String> tags){
		List<String> formattedTags = new ArrayList<String>();
		String tagkey = "Tag/";
		for (Iterator<String> iterator = tags.iterator(); iterator.hasNext();) {
			String tag = iterator.next();
			formattedTags.add(tagkey+tag);			
		}
		return formattedTags;
	}
	
	
	/*
	 * Method formats the list of constraint into String as required by Search api.
	 * 
	 */
	private String generateConstraintParameter(List<Constraint> constraints){
		StringBuilder formattedConstraints = new StringBuilder();
  		if(constraints != null){
  			for (int constraintsCtr = 0; constraintsCtr < constraints.size(); constraintsCtr++) {
  				Constraint constraint = (Constraint) constraints.get(constraintsCtr);
  				StringBuilder constraintParameter = new StringBuilder("");
  				constraintParameter.append("{\"type\":\"").append(constraint.getType()).append("\"");
  				
  				if(StringUtil.isNotEmpty(constraint.getId())){
  					constraintParameter.append(",").append("\"id\":\"").append(constraint.getId()).append("\"");
  				}
  				
  				// Extract all values
  				List<String> allValues = constraint.getValues();
  				StringBuilder values = new StringBuilder();
  				
  				for (int valueCtr = 0; valueCtr< allValues.size();valueCtr++) {
  					String value = (String) allValues.get(valueCtr);
  					if(valueCtr == 0){
  						values.append("\"").append(value).append("\"");
  					}else{
  						values.append(COMMA).append("\"").append(value).append("\"");
  					}
  					
  				}
  				constraintParameter.append(",").append("\"values\":[").append(values.toString()).append("]");
  				
  				constraintParameter.append(",").append("\"exactMatch\":\"").append(constraint.isExactMatch()).append("\"");

  				if(constraintsCtr > 0){
  					formattedConstraints.append("&constraint=");
  				}
  				formattedConstraints.append(constraintParameter.toString());
  			}
  			formattedConstraints.append("}");
  			
  		}
  		return formattedConstraints.toString();
	}
}

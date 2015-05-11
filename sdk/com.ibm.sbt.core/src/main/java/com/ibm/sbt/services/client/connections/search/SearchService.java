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
import static com.ibm.sbt.services.client.base.CommonConstants.INIT_URL_PARAM;
import static com.ibm.sbt.services.client.base.CommonConstants.URL_PARAM;
import static com.ibm.sbt.services.client.base.CommonConstants.UTF8;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.endpoints.Endpoint;

public class SearchService extends ConnectionsService {
	
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

	@Override
	protected void initServiceMappingKeys(){
		serviceMappingKeys = new String[]{"search"};
	}
	
	/**
	 * Lists the elements in an Atom entry representing the result returned by a search.
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return EntityList<Result>
	 * @throws ClientServicesException
	 */
	public EntityList<Result> getResults(String query,Map<String, String> parameters) throws ClientServicesException {
		
		Map<String, List<String>> parametersList = new HashMap<String, List<String>>();
		for(Map.Entry<String, String> entry : parameters.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			List<String> valueList = new ArrayList<String>();
			valueList.add(value);
			parametersList.put(key, valueList);
		}
		return getResultsList(query, parametersList);
	}

	/**
	 * Lists the elements in an Atom entry representing the result returned by a search.
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return EntityList<Result>
	 * @throws ClientServicesException
	 */
	public EntityList<Result> getResultsList(String query,Map<String, List<String>> parameters) throws ClientServicesException {
		if(parameters==null){
			parameters= new HashMap<String,List<String>>();
		}
		List<String> queryList = new ArrayList<String>();
		queryList.add(query);
		parameters.put("query", queryList);		

		StringBuilder searchQry = new StringBuilder(SearchUrls.SEARCH.format(this));
		addUrlParameters(searchQry, parameters);
		return getResultEntityList(searchQry.toString(), new HashMap<String,String>());
	}
	
	/**
	 * Lists the elements in an Atom entry representing the result returned by a search.
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return EntityList<Result>
	 * @throws ClientServicesException
	 */
	public EntityList<Result> getMyResultsList(String query,Map<String, List<String>> parameters) throws ClientServicesException {
		if(parameters==null){
			parameters= new HashMap<String,List<String>>();
		}

		StringBuilder searchQry = new StringBuilder(SearchUrls.MYSEARCH.format(this, SearchUrls.getQuery(query)));
		addUrlParameters(searchQry, parameters);
		return getResultEntityList(searchQry.toString(), new HashMap<String,String>());
	}
	
	protected void addUrlParameters(StringBuilder b, Map<String,List<String>> parameters) throws ClientServicesException {
		
		if (parameters != null) {
			boolean first = !b.toString().contains("?");
			for (Map.Entry<String, List<String>> e : parameters.entrySet()) {
				String name = e.getKey();
				if (StringUtil.isNotEmpty(name)) {
					List<String> values = e.getValue();					
					for(String val : values){
						first = addParameter(b, first, name, val);
					}					
				}
			}
		}
	}
	

	protected boolean addParameter(StringBuilder b, boolean first, String name, String value) throws ClientServicesException {
		try {
			if (value != null) {
				b.append(first ? INIT_URL_PARAM : URL_PARAM);
				b.append(name);
				b.append('=');
				b.append(URLEncoder.encode(value, UTF8));
				return false;
			}
			return first;
		} catch (UnsupportedEncodingException ex) {
			throw new ClientServicesException(ex);
		}
	}	
	
	
	
	/**
	 * The category document identifies the tags that have been assigned to particular 
	 * items, such as blog posts or community entries. Tags are single-word keywords that 
	 * categorize a posting or entry. A tag classifies the information in the posting or 
	 * entry to make it easier to find the content later. The format of the tags document 
	 * is an Atom publishing protocol (APP) categories document.
	 * 
	 * @param String
	 *            Single tag to be search for, for multiple tags use other overloaded method
	 * @return EntityList<Result>
	 * @throws ClientServicesException
	 */
	public EntityList<Result> getResultsByTag(String tag) throws ClientServicesException {
		List<String> taglist = new ArrayList<String>();
		taglist.add(tag);
		return getResultsByTag(taglist,null);
	}
	
	/**
	 * The category document identifies the tags that have been assigned to particular 
	 * items, such as blog posts or community entries. Tags are single-word keywords that 
	 * categorize a posting or entry. A tag classifies the information in the posting or 
	 * entry to make it easier to find the content later. The format of the tags document 
	 * is an Atom publishing protocol (APP) categories document.
	 * 
	 * @param String
	 *            Single tag to be search for, for multiple tags use other overloaded method
	 * @return EntityList<Result>
	 * @throws ClientServicesException
	 */
	public EntityList<Result> getMyResultsByTag(String tag) throws ClientServicesException {
		List<String> taglist = new ArrayList<String>();
		taglist.add(tag);
		return getMyResultsByTag(taglist,null);
	}
	
	/**
	 * The category document identifies the tags that have been assigned to particular 
	 * items, such as blog posts or community entries. Tags are single-word keywords that 
	 * categorize a posting or entry. A tag classifies the information in the posting or 
	 * entry to make it easier to find the content later. The format of the tags document 
	 * is an Atom publishing protocol (APP) categories document.
	 * 
	 * @param List
	 *            of Tags to searched for
	 * @return EntityList<Result>
	 * @throws ClientServicesException
	 */
	public EntityList<Result> getResultsByTag(List<String> tags) throws ClientServicesException {
		return getResultsByTag(tags,null);
	}
	
	/**
	 * The category document identifies the tags that have been assigned to particular 
	 * items, such as blog posts or community entries. Tags are single-word keywords that 
	 * categorize a posting or entry. A tag classifies the information in the posting or 
	 * entry to make it easier to find the content later. The format of the tags document 
	 * is an Atom publishing protocol (APP) categories document.
	 * 
	 * @param List
	 *            of Tags to searched for
	 * @return EntityList<Result>
	 * @throws ClientServicesException
	 */
	public EntityList<Result> getMyResultsByTag(List<String> tags) throws ClientServicesException {
		return getMyResultsByTag(tags,null);
	}
	
	/**
	 * The category document identifies the tags that have been assigned to particular 
	 * items, such as blog posts or community entries. Tags are single-word keywords that 
	 * categorize a posting or entry. A tag classifies the information in the posting or 
	 * entry to make it easier to find the content later. The format of the tags document 
	 * is an Atom publishing protocol (APP) categories document.
	 * 
	 * @param List
	 *            of Tags to searched for
	 * @return EntityList<Result>
	 * @throws ClientServicesException
	 */
	public EntityList<Result> getResultsByTag(List<String> tags,
			Map<String, String> parameters) throws ClientServicesException {
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
	 * The category document identifies the tags that have been assigned to particular 
	 * items, such as blog posts or community entries. Tags are single-word keywords that 
	 * categorize a posting or entry. A tag classifies the information in the posting or 
	 * entry to make it easier to find the content later. The format of the tags document 
	 * is an Atom publishing protocol (APP) categories document.
	 * 
	 * @param List
	 *            of Tags to searched for
	 * @return EntityList<Result>
	 * @throws ClientServicesException
	 */
	public EntityList<Result> getMyResultsByTag(List<String> tags,
			Map<String, String> parameters) throws ClientServicesException {
		// High level wrapper, provides a convenient mechanism for search for
		// tags, uses constraints internally
		List<String> formattedTags = new ArrayList<String>();
		List<Constraint> constraints = new ArrayList<Constraint>();
		formattedTags = createTagConstraint(tags);
		Constraint constraint = new Constraint();
		constraint.setType("category");
		constraint.setValues(formattedTags);
		constraints.add(constraint);
		return getMyResultsWithConstraint("", constraints,parameters);
	}
	
	/**
	 * 
	 * @param query
	 *            Text to search for
	 * @return ResultList
	 * @throws ClientServicesException
	 */
	public EntityList<Result> getMyResults(String query) throws ClientServicesException {
		return getMyResults(query, null);
	}
	
	/**
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return EntityList<Result>
	 * @throws ClientServicesException
	 */
	public EntityList<Result> getMyResults(String query, Map<String, String> parameters) throws ClientServicesException {		
		String searchQry = SearchUrls.MYSEARCH.format(this, SearchUrls.getQuery(query));
		return getResultEntityList(searchQry, parameters);
	}
	
	/**
	 * 
	 * @param query
	 *            Text to search for
	 * @param EntityList<FacetValue>
	 * @throws ClientServicesException
	 */
	public EntityList<FacetValue> getPeople(String query) throws ClientServicesException {
		return getPeople(query, null);
	}
	
	/**
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return EntityList<FacetValue>
	 * @throws ClientServicesException
	 */
	public EntityList<FacetValue> getPeople(String query,Map<String, String> parameters) throws ClientServicesException {
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
	 * 
	 * @param query
	 *            Text to search for
	 * @return EntityList<FacetValue>
	 * @throws ClientServicesException
	 */
	public EntityList<FacetValue> getMyPeople(String query) throws ClientServicesException {
		return getMyPeople(query, null);
	}
	
	/**
	 * 
	 * @param query
	 *            Text to search for
	 * @param Map
	 *            for additional parameters
	 * @return EntityList<FacetValue>
	 * @throws ClientServicesException
	 */
	public EntityList<FacetValue> getMyPeople(String query,Map<String, String> parameters) throws ClientServicesException {		
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
     * @param query Text to search for
     * @param Constraint
     * 
     * @return EntityList<Result>
     * @throws ClientServicesException
     * 
	   *http://www-10.IBM.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Constraints&content=pdcontent  
     */
	public EntityList<Result> getResultsWithConstraint(String query, Constraint constraint) throws ClientServicesException {
		List<Constraint> constraintList = new ArrayList<Constraint>();
		constraintList.add(constraint);
		return getResultsWithConstraint(query, constraintList,null);
	}
	
    /**
     * @param query Text to search for
     * @param Constraint
     * 
     * @return EntityList<Result>
     * @throws ClientServicesException
     * 
	   *http://www-10.IBM.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Constraints&content=pdcontent  
     */
	public EntityList<Result> getMyResultsWithConstraint(String query, Constraint constraint) throws ClientServicesException {
		List<Constraint> constraintList = new ArrayList<Constraint>();
		constraintList.add(constraint);
		return getMyResultsWithConstraint(query, constraintList,null);
	}
	
    /**
     * @param query Text to search for
     * @param List<Constraint>
     * 
     * @return EntityList<Scope>
     * @throws ClientServicesException
     * 
	   *http://www-10.IBM.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Constraints&content=pdcontent  
     */
	public EntityList<Result> getResultsWithConstraint(String query, List<Constraint> constraints) throws ClientServicesException {
		return getResultsWithConstraint(query, constraints,null);
	}
    
    /**
     * @param query Text to search for
     * @param List<Constraint>
     * 
     * @return EntityList<Scope>
     * @throws ClientServicesException
     * 
	   *http://www-10.IBM.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Constraints&content=pdcontent  
     */
	public EntityList<Result> getMyResultsWithConstraint(String query, List<Constraint> constraints) throws ClientServicesException {
		return getMyResultsWithConstraint(query, constraints,null);
	}
    
    /**
     * @param query Text to search for
     * @param List<Constraint>
     * 
     * @return EntityList<Result>
     * @throws ClientServicesException
     * 
	   *http://www-10.IBM.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Constraints&content=pdcontent  
     */
	public EntityList<Result> getResultsWithConstraint(String query, List<Constraint> constraints, Map<String, String> parameters) throws ClientServicesException{
		
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		// We can not use a map of constraints, since there could be multiple constraints but map can have only one key named constraint
		List<String> formattedConstraints = generateConstraintParameter(constraints);
		if(parameters == null){
			parameters = new HashMap<String,String>();
		}
		for(Map.Entry<String, String> entry : parameters.entrySet()){
			List<String> valueList = new ArrayList<String>();
			valueList.add(entry.getValue());
			params.put(entry.getKey(), valueList);
		}
		params.put("constraint", formattedConstraints);
		return getResultsList(query, params);
	}

    /**
     * @param query Text to search for
     * @param List<Constraint>
     * 
     * @return EntityList<Result>
     * @throws ClientServicesException
     * 
	   *http://www-10.IBM.com/ldd/appdevwiki.nsf/xpDocViewer.xsp?lookupName=IBM+Connections+4.0+API+Documentation#action=openDocument&res_title=Constraints&content=pdcontent  
     */
	public EntityList<Result> getMyResultsWithConstraint(String query, List<Constraint> constraints, Map<String, String> parameters) throws ClientServicesException{
		
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		// We can not use a map of constraints, since there could be multiple constraints but map can have only one key named constraint
		List<String> formattedConstraints = generateConstraintParameter(constraints);
		if(parameters == null){
			parameters = new HashMap<String,String>();
		}
		for(Map.Entry<String, String> entry : parameters.entrySet()){
			List<String> valueList = new ArrayList<String>();
			valueList.add(entry.getValue());
			params.put(entry.getKey(), valueList);
		}
		params.put("constraint", formattedConstraints);
		return getMyResultsList(query, params);
	}

	/**
     * Search IBM Connection for available scopes ( Applications in which search can be executed )
     * 
     * @method getScopes
     * @return EntityList<Scope>
	 * @throws SearchServiceException 
     */
     public EntityList<Scope> getScopes() throws ClientServicesException {
    	Map<String,String> params = new HashMap<String,String>();
		String searchQry = SearchUrls.SCOPES.format(this);
		return getScopeEntityList(searchQry, params);
     }
     
  	
     /**
      * Lists the elements in an Atom entry representing the result returned by a search.
      * 
      * @param query Text to search for
      * @return ResultList
      * @throws ClientServicesException
      */
 	public EntityList<Result> getResults(String query) throws ClientServicesException {
 		return getResults(query, new HashMap<String, String>());
 	}
     
 	//------------------------------------------------------------------------------------------------------------------
 	// Getting Search feeds
 	//------------------------------------------------------------------------------------------------------------------

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
 		};
 	}

	protected Result getResultEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (Result)getEntity(requestUrl, parameters, getResultFeedHandler());
	}

	protected EntityList<Result> getResultEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getResultFeedHandler());
	}

	protected FacetValue getFacetValueEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (FacetValue)getEntity(requestUrl, parameters, getFacetValueFeedHandler("Person"));
	}

	protected EntityList<FacetValue> getFacetValueEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getFacetValueFeedHandler("Person"));
	}
     
	protected Scope getScopeEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (Scope)getEntity(requestUrl, parameters, getScopeFeedHandler());
	}

	protected EntityList<Scope> getScopeEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getScopeFeedHandler());
	}

	/*
	 * Internal service methods
	 */
     
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
	private List<String> generateConstraintParameter(List<Constraint> constraints){
		
		List<String> formattedConstraintsList = new ArrayList<String>();
		
  		if(constraints != null){
  			for (Constraint constraint : constraints) {
  				StringBuilder formattedConstraint = new StringBuilder();
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
  				constraintParameter.append("}");  				
  				formattedConstraint.append(constraintParameter.toString());
  				formattedConstraintsList.add(formattedConstraint.toString());
  			}  			
  			
  		}
  		return formattedConstraintsList;
	}
}

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
package com.ibm.sbt.sample.app;

import java.util.Map;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.search.Constraint;
import com.ibm.sbt.services.client.connections.search.Result;
import com.ibm.sbt.services.client.connections.search.SearchService;

/**
 * @author mwallace
 *
 */
public class SearchApp extends BaseApp {

	private SearchService searchService;
	
	public SearchApp(String url, String user, String password) {
		super(url, user, password);
	}
	
    public SearchService getSearchService() {
    	if (searchService == null) {
    		searchService = new SearchService(getBasicEndpoint());
    	}
    	return searchService;
    }
    
    EntityList<Result> getResults(String query) throws ClientServicesException {
    	return getSearchService().getResults(query);
    }
    
    EntityList<Result> getResults(String query, Map<String, String> parameters) throws ClientServicesException {
    	return getSearchService().getResults(query, parameters);
    }
    
    EntityList<Result> getResultsWithConstraint(String query, Constraint constraint) throws ClientServicesException {
    	return getSearchService().getResultsWithConstraint(query, constraint);
    }
    
    EntityList<Result> getMyResults(String query) throws ClientServicesException {
    	return getSearchService().getMyResults(query);
    }
    
    EntityList<Result> getMyResults(String query, Map<String, String> parameters) throws ClientServicesException {
    	return getSearchService().getMyResults(query, parameters);
    }
    
    EntityList<Result> getMyResultsWithConstraint(String query, Constraint constraint) throws ClientServicesException {
    	return getSearchService().getMyResultsWithConstraint(query, constraint);
    }
    
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Usage: java com.ibm.sbt.sample.app.SearchApp <url> <user> <password> <query>");
			return;
		}
		
		String url = args[0];
		String user = args[1];
		String password = args[2];
		String query = args[3];
		
		SearchApp sa = null;
		try {
			sa = new SearchApp(url, user, password);
			
			System.out.println("*** Public entities:");
			EntityList<Result> results = sa.getResults(query);
			for (Result result : results) {
				System.out.println(result.getTitle() + " " + result.getUpdated());
			}
						
			System.out.println("*** Non-public entities accessible by given user:");
			results = sa.getMyResults(query);
			for (Result result : results) {
				System.out.println(result.getTitle() + " " + result.getUpdated());
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

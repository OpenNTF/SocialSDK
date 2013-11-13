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

package com.ibm.sbt.services.client.connections.search;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

public class Scope extends BaseEntity{
	
	/**
	 * Scope model object
	 * 
	 * @author Manish Kataria 
	 */
	
	/**
	 * Constructor
	 *  
	 * @param Scope
	 * @param ResultId
	 */
	public Scope(SearchService searchService, String id) {
		setService(searchService);
		setAsString(SearchXPath.uid, id);
	}
	
	public Scope(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	public String getId(){
		String id = getAsString(SearchXPath.uid);
		return id;
	}
	
	public String getTitle(){
		String id = getAsString(SearchXPath.title);
		return id;
	}
	
	public String getSummary(){
		String id = getAsString(SearchXPath.summary);
		return id;
	}
	
	public String getUpdated(){
		String id = getAsString(SearchXPath.updated);
		return id;
	}
	
	public String getLink(){
		String id = getAsString(SearchXPath.searchLink);
		return id;
	}

}

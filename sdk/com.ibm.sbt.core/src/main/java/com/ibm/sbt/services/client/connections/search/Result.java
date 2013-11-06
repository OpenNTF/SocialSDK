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

/**
 * Result model object
 * 
 * @author Manish Kataria 
 */


public class Result extends BaseEntity{
	
	/**
	 * Constructor
	 *  
	 * @param SearchService
	 * @param ResultId
	 */
	public Result(SearchService searchService, String id) {
		setService(searchService);
		setAsString(SearchXPath.uid, id);
	}

	public Result(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	public String getId(){
		return(getAsString(SearchXPath.uid));
	}
	
	public String getTitle(){
		return(getAsString(SearchXPath.title));
	}
	
	public String getTags(){
		return(getAsString(SearchXPath.tags));
	}
	
	public String getSummary(){
		return(getAsString(SearchXPath.summary));
	}
	
	public String getScore(){
		return(getAsString(SearchXPath.score));
	}
	
	public String getRank(){
		return(getAsString(SearchXPath.rank));
	}
	
	public String getUpdated(){
		return(getAsString(SearchXPath.updated));
	}
	
	public String getApplication(){
		return(getAsString(SearchXPath.application));
	}
}
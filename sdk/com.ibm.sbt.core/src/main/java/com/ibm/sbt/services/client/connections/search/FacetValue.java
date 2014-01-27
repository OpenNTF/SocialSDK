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
 * Facet model object
 * 
 * @author Mark Wallace 
 */


public class FacetValue extends BaseEntity{
	
	/**
	 * Constructor
	 *  
	 * @param SearchService
	 * @param FacetId
	 */
	public FacetValue(SearchService searchService, String id) {
		setService(searchService);
		setAsString(FacetValueXPath.uid, id);
	}
	
	public FacetValue(){}

	public FacetValue(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	public String getId(){
		String id = getAsString(FacetValueXPath.id);
		int index = id.indexOf('/');
		return (index == -1) ? id : id.substring(index+1);
	}
	
	public String getLabel(){
		return getAsString(FacetValueXPath.label);
	}
	
	public float getWeight(){
		return getAsFloat(FacetValueXPath.weight);
	}

}
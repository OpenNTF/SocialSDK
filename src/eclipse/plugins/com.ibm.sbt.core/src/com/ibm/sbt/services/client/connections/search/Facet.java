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

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;


/**
 * Result model object
 * 
 * @author Manish Kataria 
 */

public class Facet extends BaseEntity {
	
	/**
	 * Constructor
	 *  
	 * @param ForumService
	 * @param ForumId
	 */

	public Facet(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}
	
	public Facet(){}
	
	public 
	
	String id;
	String type;
	String label;
	String weight;

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getWeight() {
		return weight;
	}


	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	public void getFacetsValue(){
		List<Node> nodes = (List<Node>) getDataHandler().getEntries("ibmsc:facetValue/@id");
		for (Iterator iterator = nodes.iterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();
			System.err.println("id : "+node.getNodeValue());
		}
		System.err.println("print over");
	}

}

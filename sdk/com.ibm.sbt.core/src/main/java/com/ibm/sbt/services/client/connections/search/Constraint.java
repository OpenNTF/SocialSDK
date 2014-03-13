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

import java.util.ArrayList;
import java.util.List;

/**
 * Class used for setting of Constraint values in Search service
 * 
 * Examples for constraints: 
 * constraint={"type": "category", "values":["Tag/tag1"]}
 * constraint={"type": "category", "values":["Tag/tag1","Tag/tag2"]}
 * constraint={"type": "category", "values":["Source/forums","Source/profiles","Source/wikis","Source/status_updates"]}
 * constraint={"type": "category", "values":["Tag/tag1"]}&constraint={"type": "category", "values":["Tag/tag2"]}
 * 
 * @author Manish Kataria 
 */

public class Constraint {
	
	private String type;
	private String id;
	private List<String> values = new ArrayList<String>();
	private boolean exactMatch;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the values
	 */
	public List<String> getValues() {
		List<String> retValues = new ArrayList<String>();
		retValues.addAll(this.values);
		return retValues;
	}
	/**
	 * @param values the values to set
	 */
	public void setValues(List<String> values) {
		this.values.addAll(values);
	}
	/**
	 * @param value
	 */
	public void addValue(String value) {
		this.values.add(value);
	}
	/**
	 * @return the exactMatch
	 */
	public boolean isExactMatch() {
		return exactMatch;
	}
	/**
	 * @param exactMatch the exactMatch to set
	 */
	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}

}

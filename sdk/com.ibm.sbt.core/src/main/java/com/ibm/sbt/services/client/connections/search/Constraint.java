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

import java.util.List;

/**
 * Class used for setting of Constraint values in Search service
 * @author Manish Kataria
 */

public class Constraint {
	
	/*
	 * Examples for constraints
	 * constraint={"type": "category", "values":["Tag/tag1"]}
	 * constraint={"type": "category", "values":["Tag/tag1","Tag/tag2"]}
	 * constraint={"type": "category", "values":["Source/forums","Source/profiles","Source/wikis","Source/status_updates"]}
	 * constraint={"type": "category", "values":["Tag/tag1"]}&constraint={"type": "category", "values":["Tag/tag2"]} 
	 */
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getExactMatch() {
		return exactMatch;
	}
	public void setExactMatch(String exactMatch) {
		this.exactMatch = exactMatch;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	
	public String type;
	public String id;
	public List<String> values;
	public String exactMatch;
	

}

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

package com.ibm.sbt.services.client.connections.forums;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

/**
 * tag Entry Class - representing a tag associated with a Forum.
 *
 * @author Swati Singh
 */
public class Tag extends BaseEntity{


	public Tag(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}

	public String getTerm() {
		return getAsString(ForumsXPath.term);
	}

	public void setTerm(String term) {
		setAsString(ForumsXPath.term,term);
	}

	public int getFrequency(){
		return getAsInt(ForumsXPath.frequency);
	}
	public int getIntensity() {
		return getAsInt(ForumsXPath.intensity);
	}

	public int getVisibility() {
		return getAsInt(ForumsXPath.visibility);
	}

}
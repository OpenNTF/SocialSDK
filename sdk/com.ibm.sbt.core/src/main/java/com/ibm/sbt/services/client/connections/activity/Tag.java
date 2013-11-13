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

package com.ibm.sbt.services.client.connections.activity;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.activity.model.ActivityXPath;

/**
 * This class represents a Tag
 * @author Vimal Dhupar
 */
public class Tag extends BaseEntity{


	public Tag(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}

	public String getTerm() {
		return getAsString(ActivityXPath.TagTerm);
	}

	public void setTerm(String term) {
		setAsString(ActivityXPath.TagTerm,term);
	}

	public int getFrequency(){
		return getAsInt(ActivityXPath.TagFrequency);
	}
}
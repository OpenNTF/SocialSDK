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
import com.ibm.sbt.services.client.connections.forums.model.Author;

/**
 * Recommendation Entry Class - represents forum Recommendation Atom entry.
 *
 * @author Swati Singh
 */
public class Recommendation extends BaseEntity{


	public Recommendation(BaseService svc, DataHandler<?> handler) {
		super(svc,handler);
	}

	public String getId() {
		return getAsString(ForumsXPath.uid);
	}

	public String getTitle() {
		return getAsString(ForumsXPath.title);
	}

	public Author getAuthor(){
		return new Author(super.dataHandler);
	}

	public String getName() {
		return this.getAuthor().getName();
	}

	public String getEmail() {
		return this.getAuthor().getEmail();
	}

	public String getUserId() {
		return this.getAuthor().getUserid();
	}

	public String getUserState() {
		return this.getAuthor().getState();
	}
}
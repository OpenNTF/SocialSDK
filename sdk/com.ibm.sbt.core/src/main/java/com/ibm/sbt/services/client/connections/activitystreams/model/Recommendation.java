/*
 *  Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.activitystreams.model;

/**
 * Actor class for persisting Recommendation (Like/UnLike) information from Activity Stream Entry 
 * @author Manish Kataria
 */

public class Recommendation {
	
	private Actor Author;
	private String id;
	
	
	public Actor getAuthor() {
		return Author;
	}
	public void setAuthor(Actor author) {
		Author = author;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	

}

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
package com.ibm.sbt.services.client.connections.follow.model;

/**
 * Class used in specifying source for which updates are required in FollowService
 * @author Manish Kataria
 */


public enum Type {
	
	ACTIVITIES("activity"),
	BLOGS("blog"),
	COMMUNITIES("community"),
	FILE("file"),
	FILEFOLDER("file_folder"),
	FORUMS("forum"),
	FORUMSTOPIC("forum_topic"),
	PROFILES("profile"),
	WIKIS("wiki"),
	WIKIPAGE("wiki_page"),
	TAGS("tag");
	
	String type;
	private Type(String type) {
		this.type = type;
	}
	
	/**
	 * Wrapper method to return source type
	 * <p>
	 */
	public String getType(){
		return type;
	}

}

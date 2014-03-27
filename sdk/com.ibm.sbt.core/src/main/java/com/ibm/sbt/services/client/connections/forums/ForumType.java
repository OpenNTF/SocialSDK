/*
 * � Copyright IBM Corp. 2013
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

import com.ibm.sbt.services.client.base.NamedUrlPart;

/**
 * Class used in constructing URL for Forums service
 * @author Manish Kataria
 * @author Carlos Manias
 */
public enum ForumType {
	FORUMS,
	FORUM,
	TOPICS,
	REPLIES,
	REPLY,
	TOPIC,
	RESOURCES,
	REPORTS,
	ACL,
	TAGS,
	RECOMMENDATION; 
	
	public NamedUrlPart get(){
		return new NamedUrlPart("forumType", name().toLowerCase());
	}

}

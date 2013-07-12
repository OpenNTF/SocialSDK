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

package com.ibm.sbt.services.client.connections.activitystreams.model;

import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
/**
 * Actor class for persisting Reply information from Activity Stream Entry 
 * @author Manish Kataria
 */

public class Reply {
	private String content;
	private String replyId;
	private String updated;
	
	
	private String authorName;
	private String authorId;
	private String authorObjectType;

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getAuthorObjectType() {
		return authorObjectType;
	}
	public void setAuthorObjectType(String authorObjectType) {
		this.authorObjectType = authorObjectType;
	}
	
	public static List<Reply> getReplies(DataHandler<JsonJavaObject> handler){
		List<Reply> replyList = new ArrayList<Reply>();
		List<JsonJavaObject> replies = null;
		replies = handler.getEntries(ASJsonPath.Comments);
		if(replies == null){
			// When there are further operation after comments, they move from target to object section
			replies = handler.getEntries(ASJsonPath.ObjectComments);
		}
			
		if(replies!=null){
			for (JsonJavaObject reply: replies) {
				Reply replyObj = new Reply();
				replyObj.setContent(reply.getString(ASJsonPath.ReplyContent.getPath()));
				replyObj.setReplyId(reply.getString(ASJsonPath.ReplyId.getPath()));
				replyObj.setUpdated(reply.getString(ASJsonPath.ReplyUpdated.getPath()));
				JsonJavaObject authorObject = reply.getJsonObject(ASJsonPath.ReplyAuthor.getPath());
				replyObj.setAuthorId(authorObject.getString(ASJsonPath.ReplyAuthorId.getPath()));
				replyObj.setAuthorName(authorObject.getString(ASJsonPath.ReplyAuthorName.getPath()));
				replyObj.setAuthorObjectType(authorObject.getString(ASJsonPath.ReplyAuthorObjectType.getPath()));
				
				replyList.add(replyObj);
			}
		}
		return replyList;
		
	}

}

/*
 * © Copyright IBM Corp. 2012
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
package com.ibm.sbt.services.client.connections.files.model;

import org.w3c.dom.Node;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.connections.files.FileEntryXPath;
import com.ibm.sbt.services.client.connections.files.FileService;

/**
 * Comment Entry Class - representing a Comment Entry of the File.
 * 
 * @author Vimal Dhupar
 */
public class CommentEntry extends BaseEntity {
	private String		commentId;
	private String		comment;

	public CommentEntry() {
	}

	public CommentEntry(String id) {
		this.commentId = id;
	}
	
	public CommentEntry(FileService svc, DataHandler<?> dh) {
        super(svc, dh);
    }
	
	public String getCommentId() {
		if (!StringUtil.isEmpty(commentId)) {
			return commentId;
		}
		if (!getAsString(FileEntryXPath.CategoryFromEntry).equals("comment")) {
			return null;
		}
		return getAsString(FileEntryXPath.UuidFromEntry);
	}

	private void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getComment() {
		if (!StringUtil.isEmpty(comment)) {
			return comment;
		}
		if (!getAsString(FileEntryXPath.CategoryFromEntry).equals("comment")) {
			return null;
		}
		return getAsString(FileEntryXPath.CommentFromEntry);
	}

	private void setComment(String comment) {
		this.comment = comment;
	}

	public Node getData() {
		return (Node)dataHandler.getData();
	}

	public void setData(Node data) {
		dataHandler.setData(data);
	}

	public CommentEntry getCommentEntry() {
		setComment(getComment());
		setCommentId(getCommentId());
		return this;
	}
}

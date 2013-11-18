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
package com.ibm.sbt.services.client.connections.files;

import java.util.Date;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.files.model.Author;
import com.ibm.sbt.services.client.connections.files.model.FileEntryXPath;
import com.ibm.sbt.services.client.connections.files.model.Modifier;

/**
 * Comment Entry Class - representing a Comment Entry of the File.
 * 
 * @author Vimal Dhupar
 */
public class Comment extends BaseEntity {
	private String		commentId;
	private String		comment;
	private Author		authorEntry;
	private Modifier	modifierEntry;
	
	public Comment() {
	}

	public Comment(String id) {
		this.commentId = id;
	}
	
	public Comment(FileService svc, DataHandler<?> dh) {
        super(svc, dh);
        authorEntry = new Author(getService(), new XmlDataHandler((Node)this.getDataHandler().getData(), 
        		ConnectionsConstants.nameSpaceCtx, (XPathExpression)AtomXPath.author.getPath()));
        modifierEntry = new Modifier(getService(), new XmlDataHandler((Node)this.getDataHandler().getData(), 
        		ConnectionsConstants.nameSpaceCtx, (XPathExpression)AtomXPath.modifier.getPath()));
    }
	
	public String getCommentId() {
		if (!StringUtil.isEmpty(commentId)) {
			return commentId;
		}
		if (!getAsString(FileEntryXPath.Category).equals("comment")) {
			return null;
		}
		return getAsString(FileEntryXPath.Uuid);
	}
	
	public String getComment() {
		if (!StringUtil.isEmpty(comment)) {
			return comment;
		}
		if (!getAsString(FileEntryXPath.Category).equals("comment")) {
			return null;
		}
		return getAsString(FileEntryXPath.Comment);
	}
	
	public String getTitle() {
		return this.getAsString(FileEntryXPath.Title);
	}
	
	public String getContent() {
		return this.getAsString(FileEntryXPath.Content);
	}

	public Date getCreated() {
		return this.getAsDate(FileEntryXPath.Created);
	}

	public Date getModified() {
		return this.getAsDate(FileEntryXPath.Modified);
	}

	public String getVersionLabel() {
		return this.getAsString(FileEntryXPath.VersionLabel);
	}

	public Date getUpdated() {
		return this.getAsDate(FileEntryXPath.Updated);
	}

	public Date getPublished() {
		return this.getAsDate(FileEntryXPath.Published);
	}

	public Modifier getModifier() {
		return modifierEntry;
	}

	public Author getAuthor() {
		return authorEntry;
	}

	public String getLanguage() {
		return this.getAsString(FileEntryXPath.Language);
	}
	
	public boolean getDeleteWithRecord() {
		return this.getAsBoolean(FileEntryXPath.DeleteWithRecord);
	}
	
	public Comment getCommentEntry() {
		setComment(getComment());
		setCommentId(getCommentId());
		return this;
	}
	
	private void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	private void setComment(String comment) {
		this.comment = comment;
	}
}

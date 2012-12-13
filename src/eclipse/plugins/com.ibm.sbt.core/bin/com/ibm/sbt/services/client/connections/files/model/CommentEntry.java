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

import org.w3c.dom.Document;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.connections.files.utils.ContentMapFiles;
import com.ibm.sbt.services.client.connections.files.utils.NamespacesConnections;
/**
 *	@author Vimal Dhupar
 */
public class CommentEntry {
	private String commentId;
	private String comment;
	private Document data;
	
	public CommentEntry() {
		// TODO Auto-generated constructor stub
	}
	public CommentEntry(String id) {
		this.commentId = id;
	}
	public String getCommentId() {
		if(!StringUtil.isEmpty(commentId))
			return commentId;
		if(!get("categoryFromEntry").equals("comment"))
			return null;
		return get("uuidFromEntry");
	}
	private void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getComment() {
		if(!StringUtil.isEmpty(comment))
			return comment;
		if(!get("categoryFromEntry").equals("comment"))
			return null;
		return get("commentFromEntry");
	}
	private void setComment(String comment) {
		this.comment = comment;
	}
	public Document getData() {
		return data;
	}
	public void setData(Document data) {
		this.data = data;
	}
	public CommentEntry getCommentEntry()
	{
		setComment(getComment());
		setCommentId(getCommentId());
		return this;
	}
	/**
     * get
     * @param fieldName
     * @return
     */
    private String get(String fieldName)
    {
    	String xpQuery = getXPathQuery(fieldName);
    	return getFieldUsingXPath(xpQuery);
    }
    /**
     * getXPathQuery
     * @return xpath query for specified field. Field names follow IBM Connections naming convention
     */
    private String getXPathQuery(String fieldName)
    {	
    	return ContentMapFiles.xpathMap.get(fieldName);
    }
    /**
     * getFieldUsingXPath
     * @return Execute xpath query on Profile XML
     */
    private String getFieldUsingXPath(String xpathQuery) 
    {
    	String result = null;
    	try {
			result = DOMUtil.value(this.data, xpathQuery, NamespacesConnections.nameSpaceCtx);
		} 
    	catch (XMLException e){
//    		System.err.println("Error in getFieldUsingXPath .. xpathQuery is : " + xpathQuery);	
		}	
    	return result;
    }
}

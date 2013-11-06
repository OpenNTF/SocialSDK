package com.ibm.sbt.services.client.connections.blogs.feedhandler;

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

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.blogs.BlogService;
import com.ibm.sbt.services.client.connections.blogs.Comment;
import com.ibm.sbt.services.client.connections.blogs.CommentList;
import com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntity;
import com.ibm.sbt.services.client.connections.blogs.model.BlogXPath;



/**
 * Feed handler for Blogs Service
 * 
 * @author Swati Singh
 */

public class CommentsFeedHandler extends BlogsFeedHandler  {
	
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public CommentsFeedHandler(BlogService service){
		super(service);
	}



	@Override
	public BaseEntity createEntityFromData(Object data) {
		Node node = (Node)data;
		XPathExpression expr = (data instanceof Document) ? (XPathExpression)BlogXPath.singleEntry.getPath() : null;
		XmlDataHandler handler = new XmlDataHandler(node, ConnectionsConstants.nameSpaceCtx, expr);
		BaseBlogEntity comment = new Comment(getService(), handler);
		return comment;
	}

	@Override
	public EntityList<? extends BaseEntity> createEntityList(Response requestData) {
		return new CommentList((Response)requestData, this);
	}

	@Override
	public BaseService getService() {
		return super.getService();
	}

}

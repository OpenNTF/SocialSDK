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
package com.ibm.sbt.services.client.connections.files.feedHandler;

import org.w3c.dom.Node;

import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.files.CommentList;
import com.ibm.sbt.services.client.connections.files.FileService;
import com.ibm.sbt.services.client.connections.files.Comment;

public class CommentFeedHandler implements IFeedHandler {
	
	private FileService service ; 
	
	public CommentFeedHandler() {
		// TODO Auto-generated constructor stub
	}
	
	public CommentFeedHandler(FileService service) {
		this.service = service;
	}
	
	/**
	 * @param dataHolder
	 * @return Comment
	 */
	@Override
    public Comment createEntity(Response dataHolder) {
        return createEntityFromData((Node)dataHolder.getData());
    }

	/**
	 * @param data
	 * @return Comment
	 */
    @Override
    public Comment createEntityFromData(Object data) {
        XmlDataHandler handler = new XmlDataHandler((Node)data, ConnectionsConstants.nameSpaceCtx);
        Comment comment = new Comment(service , handler);
        return comment;
    }

    /**
	 * @param dataHolder
	 * @return Comment
	 */
    @Override
    public CommentList createEntityList(Response dataHolder) {
        return new CommentList((Response) dataHolder, this);
    }
    
    /**
	 * @return service
	 */
    @Override
    public FileService getService() {
        return this.service;
    }
}

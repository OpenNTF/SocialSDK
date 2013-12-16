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
package com.ibm.sbt.services.client.connections.cmisfiles.feedHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.ConnectionsConstants;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;
import com.ibm.sbt.services.client.connections.cmisfiles.CMISFile;
import com.ibm.sbt.services.client.connections.cmisfiles.CMISFileList;
import com.ibm.sbt.services.client.connections.cmisfiles.CMISFileService;

public class CMISFileFeedHandler implements IFeedHandler{

	private CMISFileService service; 
	
	public CMISFileFeedHandler() {
		// TODO Auto-generated constructor stub
	} 
	
	public CMISFileFeedHandler(CMISFileService service) {
		this.service = service;
	}
	
	/**
	 * @param dataHolder
	 * @return file
	 */
	@Override
    public CMISFile createEntity(Response dataHolder) {
        return createEntityFromData((Node)dataHolder.getData());
    }

	/**
	 * @param data
	 * @return file
	 */
    @Override
    public CMISFile createEntityFromData(Object data) {
    	XPathExpression xpath = (data instanceof Document) ? (XPathExpression)AtomXPath.singleEntry.getPath() : null;
    	XmlDataHandler handler = new XmlDataHandler((Node)data, ConnectionsConstants.nameSpaceCtx, xpath);
    	CMISFile file = new CMISFile(service, handler);
        return file;
    }

    /**
	 * @param dataHolder
	 * @return filelist
	 */
    @Override
    public CMISFileList createEntityList(Response dataHolder) {
        return new CMISFileList((Response) dataHolder, this);
    }

    /**
	 * @return service
	 */
    @Override
    public CMISFileService getService() {
        return service;
    }
}

/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.connections.activities;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

/**
 * @author mwallace
 *
 */
public class InReplyTo extends BaseEntity {

	/**
	 * Default constructor
	 */
	public InReplyTo() {
	}

	/**
	 * Construct to be in reply to the specified node
	 */
	public InReplyTo(ActivityNode node) {
		setRef(node.getId());
		setSource(node.getActivityUuid());
		setHref(node.getSelfUrl());
	}

	/**
	 * Construct InReplyTo based on the specified node
	 * 
	 * @param service
	 * @param dataHandler
	 */
	public InReplyTo(BaseService svc, DataHandler<?> dataHandler) {
		super(svc, dataHandler);
	}
	
	/**
	 * 
	 * @return ref
	 */
	public String getRef() {
		return getAsString(ActivityXPath.attr_ref);
	}
	
	/**
	 * 
	 * @param ref
	 */
	public void setRef(String ref) {
		setAsString(ActivityXPath.attr_ref, ref);
	}
	
	/**
	 * 
	 * @return source
	 */
	public String getSource() {
		return getAsString(ActivityXPath.attr_source);
	}
	
	/**
	 * 
	 * @param source
	 */
	public void setSource(String source) {
		setAsString(ActivityXPath.attr_source, source);
	}
	
	/**
	 * 
	 * @return href
	 */
	public String getHref() {
		return getAsString(ActivityXPath.attr_href);
	}
	
	/**
	 * 
	 * @param href
	 */
	public void setHref(String href) {
		setAsString(ActivityXPath.attr_href, href);
	}
	
}

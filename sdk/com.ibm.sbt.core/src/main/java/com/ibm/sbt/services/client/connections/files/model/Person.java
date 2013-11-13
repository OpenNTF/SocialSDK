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

/**
 * Person Entry Class - representing a Person Entry of the File.
 * 
 * @author Vimal Dhupar
 */
import org.w3c.dom.Node;

import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.DataHandler;

public class Person extends BaseEntity {
	
	public Person(BaseService svc, DataHandler<?> dataHandler) {
		super(svc, dataHandler);
	}
	
	public Node getData() {
		return (Node)dataHandler.getData();
	}

	public void setData(Node data) {
		dataHandler.setData(data);
	}
}

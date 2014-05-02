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
package com.ibm.sbt.services.client.base;

import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * 
 * @author Carlos Manias
 *
 */
public abstract class ConnectionsService extends BaseService {

	private static final long serialVersionUID = -1444389644260625300L;

	public ConnectionsService(String endpoint) {
		super(endpoint);
	}

	public ConnectionsService(Endpoint endpoint) {
		super(endpoint);
	}

	public ConnectionsService(String endpoint, int defaultCacheSize) {
		super(endpoint, defaultCacheSize);
	}

	public ConnectionsService(Endpoint endpoint, int defaultCacheSize) {
		super(endpoint, defaultCacheSize);
	}

	public ConnectionsService() {
		super();
	}

	@Override
	public String getServiceMappingKey() {
		return "connections";
	}

}

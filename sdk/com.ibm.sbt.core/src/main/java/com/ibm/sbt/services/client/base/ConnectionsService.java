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

import static com.ibm.sbt.services.client.base.CommonConstants.APPLICATION_ATOM_XML;
import static com.ibm.sbt.services.client.base.CommonConstants.BINARY_OCTET_STREAM;
import static com.ibm.sbt.services.client.base.CommonConstants.CONTENT_TYPE;
import static com.ibm.sbt.services.client.base.CommonConstants.MULTIPART_ATOM;

import java.util.HashMap;
import java.util.Map;

import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * 
 * @author Carlos Manias
 *
 */
public abstract class ConnectionsService extends BaseService {

	private static final long serialVersionUID = -1444389644260625300L;

	private static Map<String,String> ATOM_HEADERS = new HashMap<String, String>();
	static {
		ATOM_HEADERS.put(CONTENT_TYPE, APPLICATION_ATOM_XML);
	}

	private static Map<String,String> MULTIPART_HEADERS = new HashMap<String, String>();
	static {
		MULTIPART_HEADERS.put(CONTENT_TYPE, MULTIPART_ATOM);
	}

	private static Map<String,String> BINARY_HEADERS = new HashMap<String, String>();
	static {
		BINARY_HEADERS.put(CONTENT_TYPE, BINARY_OCTET_STREAM);
	}
	
	public ConnectionsService(String endpoint) {
		super(endpoint);
		initServiceMappingKeys();
	}

	public ConnectionsService(Endpoint endpoint) {
		super(endpoint);
		initServiceMappingKeys();
	}

	public ConnectionsService(String endpoint, int defaultCacheSize) {
		super(endpoint, defaultCacheSize);
		initServiceMappingKeys();
	}

	public ConnectionsService(Endpoint endpoint, int defaultCacheSize) {
		super(endpoint, defaultCacheSize);
		initServiceMappingKeys();
	}

	public ConnectionsService() {
		super();
		initServiceMappingKeys();
	}
	
	protected void initServiceMappingKeys() {
		serviceMappingKeys = new String[]{"connections"};
	}

	protected Map<String,String> getAtomHeaders() {
		Map<String, String> headers = getDefaultHeaders();
		headers.putAll(ATOM_HEADERS);
		return headers;
	}

	protected Map<String,String> getMultipartHeaders() {
		Map<String, String> headers = getDefaultHeaders();
		headers.putAll(MULTIPART_HEADERS);
		return headers;
	}

	protected Map<String,String> getBinaryHeaders() {
		Map<String, String> headers = getDefaultHeaders();
		headers.putAll(BINARY_HEADERS);
		return headers;
	}
}

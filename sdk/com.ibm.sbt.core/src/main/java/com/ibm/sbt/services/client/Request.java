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
package com.ibm.sbt.services.client;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.ClientService.HandlerInputStream;
import com.ibm.sbt.services.client.ClientService.HandlerJson;
import com.ibm.sbt.services.client.ClientService.HandlerString;
import com.ibm.sbt.services.client.ClientService.HandlerXml;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * @author mwallace
 *
 */
public class Request {

	private ClientService clientService;
	private String method;
	private String serviceUrl;
	private Map<String, String> parameters = new HashMap<String, String>();
	private Map<String, String> headers = new HashMap<String, String>();
	private Handler handler = null;
	private HttpEntity body = null; 
	private List<BodyPart> bodyParts = new ArrayList<Request.BodyPart>();
	
	public Request(ClientService clientService, String method, String serviceUrl) {
		if (clientService == null) {
			throw new IllegalStateException("ClientService must not be null.");
		}
		
		this.clientService = clientService;
		this.method = method;
		this.serviceUrl = serviceUrl;
	}
	
	public Request basicAuth(String user, String password) {
		Endpoint endpoint = clientService.getEndpoint();
		if (endpoint instanceof BasicEndpoint) {
			((BasicEndpoint)endpoint).setUser(user);
			((BasicEndpoint)endpoint).setPassword(password);
		} else {
			throw new IllegalStateException("The login method is only supported when using basic authentication.");
		}
			
		return this;
	}
	
	public Request header(String name, String value) {
		this.headers.put(name.trim(), value);
		return this;
	}
	
	public Request headers(Map<String, String> headers) {
		if (headers != null) {
			for(Map.Entry<String, String> entry : headers.entrySet()) {
				header(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}
	
	public Request parameter(String name, String value) {
		this.parameters.put(name.trim(), value);
		return this;
	}
	
	public Request parameters(Map<String, String> parameters) {
		if (parameters != null) {
			for(Map.Entry<String, String> entry : headers.entrySet()) {
				parameter(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}
	
	public Request body(String body, String mimeType) throws UnsupportedEncodingException {
		this.body = new StringEntity(body, ContentType.create(mimeType));
		return this;
	}
	
	public Request bodyPart(String name, String body, String mimeType) throws UnsupportedEncodingException {
		bodyParts.add(new BodyPart(name, body, mimeType));
		return this;
	}
	
	public Request bodyPart(String name, File file, String mimeType) throws UnsupportedEncodingException {
		bodyParts.add(new BodyPart(name, file, mimeType));
		return this;
	}
	
	public HttpEntity getBody() {
		if (body != null) {
			return body;
		}
		if (!bodyParts.isEmpty()) {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			for (BodyPart bodyPart : bodyParts) {
				bodyPart.addPart(builder);
			}
			return builder.build();
		}
		return null;
	}
	
	public Response response() throws ClientServicesException {
		return response(handler);
	}
	
	public Response asJson() throws ClientServicesException {
		return response(new HandlerJson());
	}

	public Response asString() throws ClientServicesException {
		return response(new HandlerString());
	}

	public Response asXml() throws ClientServicesException {
		return response(new HandlerXml());
	}

	public Response asInputStream() throws ClientServicesException {
		return response(new HandlerInputStream());
	}

	//
	// Internals
	//

	public Response response(Handler handler) throws ClientServicesException {
		if (ClientService.METHOD_GET.equals(method)) {
			return clientService.get(serviceUrl, parameters, headers, handler);
		} else if (ClientService.METHOD_POST.equals(method)) {
			return clientService.post(serviceUrl, parameters, headers, getBody(), handler);
		} else if (ClientService.METHOD_PUT.equals(method)) {
			return clientService.put(serviceUrl, parameters, headers, getBody(), handler);
		} else if (ClientService.METHOD_DELETE.equals(method)) {
			return clientService.delete(serviceUrl, parameters, headers, handler);
		} else if (ClientService.METHOD_DELETE_BODY.equals(method)) {
			return clientService.delete(serviceUrl, parameters, headers, getBody(), handler);
		} else {
			throw new IllegalStateException("Unknown method: "+method);
		}
	}
	
	private class BodyPart {
		
		String name;
		String stringData;
		File fileData;
		String mimeType;
		
		BodyPart(String name, String stringData, String mimeType) {
			this.name = name;
			this.stringData = stringData;
			this.mimeType = mimeType;
		}

		BodyPart(String name, File fileData, String mimeType) {
			this.name = name;
			this.fileData = fileData;
			this.mimeType = mimeType;
		}
		
		void addPart(MultipartEntityBuilder builder) {
			if (stringData != null) {
				builder.addPart(name, new StringBody(stringData, ContentType.create(mimeType)));
			}
			if (fileData != null) {
				builder.addPart(name, new FileBody(fileData, ContentType.create(mimeType)));
			}
			
		}
	}
	
}

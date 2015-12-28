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
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ibm.commons.util.io.json.JsonJavaArray;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.ClientService.HandlerInputStream;
import com.ibm.sbt.services.client.ClientService.HandlerJson;
import com.ibm.sbt.services.client.ClientService.HandlerString;
import com.ibm.sbt.services.client.ClientService.HandlerXml;
import com.ibm.sbt.services.client.base.AtomXPath;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.rest.atom.AtomEntry;
import com.ibm.sbt.services.rest.atom.AtomFeed;

/**
 * @author mwallace
 *
 */
public class Request {

	private BaseService baseService;
	private String method;
	private String serviceUrl;
	private Map<String, String> parameters = new HashMap<String, String>();
	private Map<String, String> headers = new HashMap<String, String>();
	private Handler handler = null;
	private Object body = null; 
	private List<BodyPart> bodyParts = new ArrayList<Request.BodyPart>();
	
	/**
	 * Constructor for the Request class.
	 * @param baseService An instance of BaseService  
	 * @param method The HTTP Method, i.e. GET ,POST
	 * @param serviceUrl the Url of the resource 
	 */
	public Request(BaseService baseService, String method, String serviceUrl) {
		if (baseService == null) {
			throw new IllegalStateException("BaseService must not be null.");
		}
		
		this.baseService = baseService;
		this.method = method;
		this.serviceUrl = serviceUrl;
	}
	
	/**
	 * Method to set basic authentication.
	 * @param user The username to authenticate with. 
	 * @param password The Password to authenticate with.
	 * @return The Request Object.
	 */
	public Request basicAuth(String user, String password) {
		Endpoint endpoint = baseService.getEndpoint();
		if (endpoint instanceof BasicEndpoint) {
			((BasicEndpoint)endpoint).setUser(user);
			((BasicEndpoint)endpoint).setPassword(password);
		} else {
			throw new IllegalStateException("The login method is only supported when using basic authentication.");
		}
			
		return this;
	}
	
	
	/**
	 * Method to add a header to the request.
	 * @param name The name of the header to add to the request, i.e Content-Type
	 * @param value The value of the header
	 * @return Request the Request object.
	 */
	public Request header(String name, String value) {
		this.headers.put(name.trim(), value);
		return this;
	}
	
	/**
	 * Method to add a map of headers to the request.
	 * @param headers A map of headers to add to the request
	 * @return the Request object
	 */
	public Request headers(Map<String, String> headers) {
		if (headers != null) {
			for(Map.Entry<String, String> entry : headers.entrySet()) {
				header(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}
	
	/**
	 * Method to add a URL parameter to the request.
	 * @param name the name of URL parameter
	 * @param value the value of the parameter
	 * @return the Reuqest object
	 */
	public Request parameter(String name, String value) {
		this.parameters.put(name.trim(), value);
		return this;
	}
	
	/**
	 * Method to add a map of URL parameters.
	 * @param parameters the map of parameters to add to the request
	 * @return the Request object
	 */
	public Request parameters(Map<String, String> parameters) {
		if (parameters != null) {
			for(Map.Entry<String, String> entry : parameters.entrySet()) {
				parameter(entry.getKey(), entry.getValue());
			}
		}
		return this;
	}
	
	/**
	 * Method to set the request input body.
	 * @param body the request body to add to the request.
	 * @param mimeType the Content type of the body i.e application/atom+xml
	 * @return The Request object
	 * @throws UnsupportedEncodingException
	 */
	public Request body(String body, String mimeType) throws UnsupportedEncodingException {
		this.body = body;
		this.headers.put("Content-Type", mimeType);
		return this;
	}

	/**
	 * Method to set the request input body.
	 * @param body the request body to add to the request.
	 * @param mimeType the Content type of the body i.e application/atom+xml
	 * @return The Request object
	 * @throws UnsupportedEncodingException
	 */
	public Request body(InputStream body, String mimeType) throws UnsupportedEncodingException {
		this.body = body;
		this.headers.put("Content-Type", mimeType);
		return this;
	}
	
	/**
	 * Method to build the request body in parts, or to add to the existing request body.
	 * @param name 
	 * @param body
	 * @param mimeType
	 * @return {Request}
	 * @throws UnsupportedEncodingException
	 */
	public Request bodyPart(String name, String body, String mimeType) throws UnsupportedEncodingException {
		bodyParts.add(new BodyPart(name, body, mimeType));
		return this;
	}
	
	/**
	 * Method to build the request body in parts, or to add to the existing request body.
	 * @param name
	 * @param file
	 * @param mimeType
	 * @return {Request}
	 * @throws UnsupportedEncodingException
	 */
	public Request bodyPart(String name, File file, String mimeType) throws UnsupportedEncodingException {
		bodyParts.add(new BodyPart(name, file, mimeType));
		return this;
	}

	/**
	 * Method to build the request body in parts, or to add to the existing request body.
	 * @param name
	 * @param inputStream
	 * @param mimeType
	 * @return {Request}
	 * @throws UnsupportedEncodingException
	 */
	public Request bodyPart(String name, InputStream inputStream, String mimeType) throws UnsupportedEncodingException {
		bodyParts.add(new BodyPart(name, inputStream, mimeType));
		return this;
	}
	
	/**
	 * Method to build the request body in parts, or to add to the existing request body.
	 * @param name
	 * @param byteArrayData
	 * @param mimeType
	 * @return {Request}
	 * @throws UnsupportedEncodingException
	 */
	/*
	public Request bodyPart(String name, byte[] byteArrayData, String mimeType) throws UnsupportedEncodingException {
		bodyParts.add(new BodyPart(name, byteArrayData, mimeType));
		return this;
	}
	*/

	/**
	 * Method to retrieve the request body.
	 * @return The request body
	 */
	public Object getBody() {
		if (body != null) {
			return body;
		}
		if (!bodyParts.isEmpty()) {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			for (BodyPart bodyPart : bodyParts) {
				builder.addPart(bodyPart.getName(), bodyPart.getData());
			}
			return builder.build();
		}
		return null;
	}
	
	/**
	 * Method to retrieve the reponse as an Object.
	 * @return an Object representation of the reponse
	 * @throws ClientServicesException
	 */
	public Response<Object> response() throws ClientServicesException {
		return response(handler, Object.class);
	}
	
	/**
	 * Method to retrieve the response as JSON.
	 * @return A JSON representation of the Response
	 * @throws ClientServicesException
	 */
	public Response<JsonJavaObject> asJson() throws ClientServicesException {
		return response(new HandlerJson(), JsonJavaObject.class);
	}
	
	/**
	 * Method to retrieve the response as a JSON Array.
	 * @return A JSON Array representation of the response
	 * @throws ClientServicesException
	 */
	public Response<JsonJavaArray> asJsonArray() throws ClientServicesException {
		return response(new HandlerJson(), JsonJavaArray.class);
	}

	/**
	 * Method to retrieve the response as a String.
	 * @return String representation of the response 
	 * @throws ClientServicesException
	 */
	public Response<String> asString() throws ClientServicesException {
		return response(new HandlerString(), String.class);
	}

	/**
	 * Method to retrieve the response as XML.
	 * @return an XML representation of the response 
	 * @throws ClientServicesException
	 */
	public Response<Node> asXml() throws ClientServicesException {
		return response(new HandlerXml(), Node.class);
	}

	/**
	 * Method to retrieve the response as an Input Stream.
	 * @return The response as an input stream
	 * @throws ClientServicesException
	 */
	public Response<InputStream> asInputStream() throws ClientServicesException {
		return response(new HandlerInputStream(), InputStream.class);
	}

	/**
	 * Method to retrieve the response as an Atom Entry.
	 * @return The response as an Atom Entry
	 * @throws ClientServicesException
	 */
	public Response<AtomEntry> asAtomEntry() throws ClientServicesException {
		return response(new HandlerAtomEntry(), AtomEntry.class);
	}
	
	/**
	 * Method to get the response as an Atom Feed.
	 * @return The response as an Atom Feed
	 * @throws ClientServicesException
	 */
	public Response<AtomFeed> asAtomFeed() throws ClientServicesException {
		return response(new HandlerAtomFeed(), AtomFeed.class);
	}
	
	//
	// Internals
	//

	private <T> Response<T> response(Handler handler, Class<T> responseClass) throws ClientServicesException {
		if (ClientService.METHOD_GET.equals(method)) {
			return baseService.getClientService().get(serviceUrl, parameters, headers, handler);
		} else if (ClientService.METHOD_POST.equals(method)) {
			return baseService.getClientService().post(serviceUrl, parameters, headers, getBody(), handler);
		} else if (ClientService.METHOD_PUT.equals(method)) {
			return baseService.getClientService().put(serviceUrl, parameters, headers, getBody(), handler);
		} else if (ClientService.METHOD_DELETE.equals(method)) {
			return baseService.getClientService().delete(serviceUrl, parameters, headers, handler);
		} else if (ClientService.METHOD_DELETE_BODY.equals(method)) {
			return baseService.getClientService().delete(serviceUrl, parameters, headers, getBody(), handler);
		} else {
			throw new IllegalStateException("Unknown method: "+method);
		}
	}
	
	private class HandlerAtomEntry extends HandlerXml {
		@Override
		public Object parseContent(HttpRequestBase request, HttpResponse response, HttpEntity entity) throws ClientServicesException, IOException {
			Document document = (Document)super.parseContent(request, response, entity);
			return new AtomEntry(document);
		}
	}
	
	private class HandlerAtomFeed extends HandlerXml {
		@Override
		public Object parseContent(HttpRequestBase request, HttpResponse response, HttpEntity entity) throws ClientServicesException, IOException {
			Document document = (Document)super.parseContent(request, response, entity);
			return new AtomFeed(document);
		}
	}
	
	private  XPathExpression getAtomXPath(Node node, boolean isFeed) {
		if (node instanceof Document){
			return (XPathExpression)(isFeed ? AtomXPath.entry.getPath() : AtomXPath.singleEntry.getPath());
		} else {
			 return null;
		}
	}

	private class BodyPart {
		private final String name;
		private final ContentBody data;
		
		BodyPart(String name, String stringData, String mimeType) {
			this.name = name;
			this.data = new StringBody(stringData, ContentType.create(mimeType));
		}

		BodyPart(String name, File fileData, String mimeType) {
			this.name = name;
			this.data = new FileBody(fileData, ContentType.create(mimeType));
		}

		BodyPart(String name, InputStream inputStreamData, String mimeType) {
			this.name = name;
			this.data = new InputStreamBody(inputStreamData, ContentType.create(mimeType));
		}

		BodyPart(String name, byte[] byteArrayData, String mimeType) {
			this.name = name;
			this.data = new ByteArrayBody(byteArrayData, ContentType.create(mimeType), name);
		}
		
		public final String getName(){
			return name;
		}
		
		public final ContentBody getData(){
			return data;
		}
	}
	
	/*
	private class BodyPart {
		private final String name;
		private final Object data;
		private final String mimeType;
		
		BodyPart(String name, Object data, String mimeType) {
			this.name = name;
			this.data = data;
			this.mimeType = mimeType;
		}

		public final String getName(){
			return name;
		}

		public final String getMimeType(){
			return mimeType;
		}
		
		public final Object getData(){
			return data;
		}
	}
	*/
}

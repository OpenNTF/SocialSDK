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

package com.ibm.sbt.services.client;

import java.io.IOException;
import java.io.Serializable;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * This class encapsulates the request and response data 
 * @author Carlos Manias
 *
 */
public class Response<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private T data;
	private transient HttpResponse response;
	private transient HttpRequestBase request;
	private transient HttpClient httpClient;
	private Handler handler;
	private Args args;
	private Header[] headers;
	
	/**
	 * Constructor for the Response class.
	 * @param httpClient
	 * @param response 
	 * @param request
	 * @param args
	 * @param handler The handler to handle the response data. 
	 * @throws ClientServicesException
	 */
	public Response(HttpClient httpClient, HttpResponse response, HttpRequestBase request, Args args, Handler handler) throws ClientServicesException {
		this.httpClient = httpClient;
		this.response = response;
		this.headers = response.getAllHeaders();
		this.request = request;
		this.handler = handler;
		this.args = args;
		this.data = parseContent();
	}
	
	/**
	 * Constructor Method
	 * @param data The Response data
	 */
	public Response(T data) {
		this.data = data;
	}
	
	/**
	 * Set the headers for the response
	 * @param h And array of Headers 
	 */
	public void setHeaders(Header[] h) {
		this.headers = h;
	}
	
	/**
	 * Method to get the Response Data
	 * @return The Response Data
	 */
	public T getData() {
		return data;
	}
	
	/**
	 * Method to get the Response
	 * @return A HttpResponse 
	 */
	public HttpResponse getResponse() {
		return response;
	}
	
	/**
	 * Method to get the Request 
	 * @return HttpRequestBase.
	 */
	public HttpRequestBase getRequest() {
		return request;
	}
	
	public Args getArgs() {
		return args;
	}
	
	/**
	 * Get the Http Client
	 * @return The Http Client
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	/**
	 * Methjod to get the response headers
	 * @return An array of response headers.
	 */
	public Header[] getResponseHeaders() {
		return headers;
	}

	/**
	 * Method to get a specific response header
	 * @param name The name of the header to retrieve
	 * @return The value of the specified response header if found
	 */
	public String getResponseHeader(String name) {
		for (Header header : headers) {
			if (header.getName().equalsIgnoreCase(name)) {
				return header.getValue();
			}
		}
		return null;
	}

	/**
	 * Method to check the response code from the server is what is expected
	 * @param expectedCode The HTTP Response code that is expected to be returned from the server
	 * @return The Response
	 * @throws ClientServicesException
	 */
   public Response<T> checkResponseCode(int expectedCode) throws ClientServicesException {
	   if (response != null && getResponse() != null && getRequest()!=null) {
		   if (getResponse().getStatusLine() != null && getResponse().getStatusLine().getStatusCode() == expectedCode) {
			   return this;
		   } else {
			   throw new ClientServicesException(getResponse(), getRequest());
		   }
	   } else {
		   throw new ClientServicesException(null, "Response is null");
	   }
   }

	protected T parseContent() throws ClientServicesException {
		try {
			T data = (T)handler.parseContent(null, response, response.getEntity());
			return data;
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}

	}
}

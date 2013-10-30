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
public class Response {

	private Object data;
	private HttpResponse response;
	private HttpRequestBase request;
	private HttpClient httpClient;
	private Handler handler;
	private Args args;
	private Header[] headers;
	
	public Response(HttpClient httpClient, HttpResponse response, HttpRequestBase request, Args args, Handler handler) throws ClientServicesException {
		this.httpClient = httpClient;
		this.response = response;
		this.headers = response.getAllHeaders();
		this.request = request;
		this.handler = handler;
		this.args = args;
		this.data = parseContent();
	}
	
	public Response(Object data){
		this.data = data;
	}
	public void setHeaders(Header[] h) {
		this.headers = h;
	}
	
	public Object getData(){
		return data;
	}
	
	public HttpResponse getResponse(){
		return response;
	}
	
	public HttpRequestBase getRequest(){
		return request;
	}
	
	public Args getArgs(){
		return args;
	}
	
	public HttpClient getHttpClient(){
		return httpClient;
	}
	
	protected Object parseContent() throws ClientServicesException {
		try {
			Object data = null;
			data = handler.parseContent(null, response, response.getEntity());
			return data;
		} catch (IOException e) {
			throw new ClientServicesException(e);
		}

	}

	public Header[] getResponseHeaders() {
		// TODO Auto-generated method stub
		return headers;
	}
}

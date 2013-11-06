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

package com.ibm.sbt.services.client.base.datahandlers;

import java.util.AbstractList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.BaseEntity;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.IFeedHandler;


public abstract class EntityList<Entity extends BaseEntity> extends AbstractList<Entity> implements ResponseProvider {

	private final Response requestData;
	private final List<Entity> entities;
	
	//Make it final
	private IFeedHandler<Entity> feedHandler;
	
	//TO REMOVE
	private BaseService<Entity> service;
	
	public EntityList(Response requestData, IFeedHandler<Entity> feedHandler) {
		this.requestData = requestData;
        this.feedHandler = feedHandler;
        //in case of 401 errors  client service returns null
        if (requestData!=null)
		this.entities = createEntities();
        else {
        	this.entities = null;
        }
    }
	
	//TO REMOVE
	public EntityList(Response requestData, BaseService<Entity> service) {
		this.requestData = requestData;
        this.service = service;
		this.entities = createEntities();
    }
	
	abstract protected List<Entity> createEntities();
	
	@Override
	public Object getData() {
		return requestData.getData();
	}
	
	@Override
	public List<Cookie> getCookies() {
		DefaultHttpClient httpClient = (DefaultHttpClient)requestData.getHttpClient();
		return httpClient.getCookieStore().getCookies();
	}

	@Override
	public Header[] getRequestHeaders() {
		return requestData.getRequest().getAllHeaders();
	}
	
	@Override
	public Header[] getResponseHeaders() {
		return requestData.getResponse().getAllHeaders();
	}
	
	private Header getHeaderByName(String headerName, Header[] headers) {
		for (Header header: headers){
			if (header.getName().equalsIgnoreCase(headerName)){
				return header;
			}
		}
		return null;
	}
	
	@Override
	public Header getRequestHeader(String headerName) {
		Header[] headers = getRequestHeaders();
		return getHeaderByName(headerName, headers);
	}
	
	@Override
	public Args getArgs(){
		return requestData.getArgs();
	}

	@Override
	public Header getResponseHeader(String headerName) {
		Header[] headers = getResponseHeaders();
		return getHeaderByName(headerName, headers);
	}
	
	@Override
	public HttpResponse getHttpResponse() {
		return requestData.getResponse();
	}

	@Override
	public HttpRequest getHttpRequest() {
		return requestData.getRequest();
	}
	
	public IFeedHandler<Entity> getFeedHandler() {
		return feedHandler;
	}
	
	protected Entity getEntity(Object data){
		return getFeedHandler().createEntityFromData(data);
	}
	
	/*UNCOMMENT
	public BaseService getService() {
		return feedHandler.getService();
	}
	*/
	
	public BaseService<Entity> getService() {
		return service;
	}

	@Override
	public Entity get(int index) {
		return entities.get(index);
	}

	@Override
	public int size() {
		return entities.size();
	}

}

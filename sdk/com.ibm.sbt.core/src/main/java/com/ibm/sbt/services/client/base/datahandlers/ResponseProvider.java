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

import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;
import com.ibm.sbt.services.client.ClientService.Args;

public interface ResponseProvider {
	
	Object getData();
	Header[] getRequestHeaders();
	Header[] getResponseHeaders();
	Header getRequestHeader(String headerName);
	Header getResponseHeader(String headerName);
	HttpResponse getHttpResponse();
	HttpRequest getHttpRequest();
	List<Cookie> getCookies();
	Args getArgs();
	int getTotalResults();
	int getStartIndex();
	int getItemsPerPage();
	int getCurrentPage();
	
}
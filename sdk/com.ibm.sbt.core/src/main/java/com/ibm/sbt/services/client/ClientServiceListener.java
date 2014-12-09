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

import com.ibm.sbt.services.client.ClientService.Args;

/**
 * @author mwallace
 *
 */
public interface ClientServiceListener {
	
	/**
	 * Called before xhr()
	 * 
	 * @param method
	 * @param args
	 * @param content
	 * @return
	 * @throws ClientServicesException
	 */
	public boolean preXhr(String method, Args args, Object content) throws ClientServicesException;

	/**
	 * Called after xhr()
	 * 
	 * @param method
	 * @param args
	 * @param content
	 * @param response
	 * @return
	 * @throws ClientServicesException
	 */
	public Response postXhr(String method, Args args, Object content, Response response) throws ClientServicesException;
		
}
